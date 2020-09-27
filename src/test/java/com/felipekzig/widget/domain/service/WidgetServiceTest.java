package com.felipekzig.widget.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WidgetServiceTest {

    private WidgetService service;

    private WidgetRepository repo;

    private static Widget build(UUID id, Integer zIndex) {
        return Widget.builder().id(id).coords(new Coordinates(10, 20)).width(30).height(30).zIndex(zIndex).build();
    }

    private static Widget build(Integer zIndex) {
        return Widget.builder().coords(new Coordinates(10, 20)).width(30).height(30).zIndex(zIndex).build();
    }

    @BeforeEach()
    void beforeEach() {
        repo = mock(WidgetRepository.class);
        service = new WidgetService(repo);
    }

    @Test
    void givenWidget_whenCreate_thenPersistToDatabaseAndAssignId() {
        when(repo.getByZIndex(10)).thenReturn(Optional.empty());
        Widget w = Widget.builder().coords(new Coordinates(10, 20)).width(30).height(30).zIndex(10).build();
        service.create(w);
        verify(repo, times(1)).save(w);
        assertNotNull(w.getId());
    }

    @Test
    void givenZIndexIsNullAndFirstWidget_whenCreate_thenPlaceInZIndexOne() {
        Widget widget = Widget.builder().coords(new Coordinates(10, 20)).width(30).height(30).build();

        when(repo.getForegroundWidget()).thenReturn(Optional.empty());
        service.create(widget);
        verify(repo, times(1)).getForegroundWidget();
        assertEquals(1, widget.getZIndex());
    }

    @Test
    void givenZIndexIsNull_whenCreate_thenPlaceInForeground() {
        Widget foregroundWidget = Widget.builder().coords(new Coordinates(10, 20)).width(30).height(30).zIndex(100)
                .build();

        when(repo.getForegroundWidget()).thenReturn(Optional.of(foregroundWidget));

        Widget toBeSaved = Widget.builder().coords(new Coordinates(10, 20)).width(30).height(30).build();
        service.create(toBeSaved);
        verify(repo, times(1)).getForegroundWidget();
        assertEquals(101, toBeSaved.getZIndex());
    }

    @Test
    void givenOverlappingZIndex_whenUpdate_thenIncreaseExistingWidgetByOne() {
        Widget overlappingWidget = build(10);
        when(repo.getWidgetsWithZIndexGreaterThanOrEqualTo(10))
                .thenReturn(Stream.of(overlappingWidget, build(11), build(13)));

        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        Widget toBeSaved = build(id, 10);
        service.update(id, toBeSaved);

        verify(repo, times(1)).getWidgetsWithZIndexGreaterThanOrEqualTo(10);
        assertEquals(10, toBeSaved.getZIndex());
        assertEquals(11, overlappingWidget.getZIndex());
    }

    @Test
    void givenDifferentIdFromBody_whenUpdate_throwRuntimeException() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        Widget toBeSaved = build(id, 10);
        assertThrows(RuntimeException.class, () -> {
            service.update(UUID.randomUUID(), toBeSaved);
        });
    }

    @Test
    void givenValidId_whenGetById_thenReturnWidget() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        Widget expected = build(id, 10);

        when(repo.getById(id)).thenReturn(Optional.of(expected));
        Optional<Widget> actual = service.getById(id);

        assertEquals(true, actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void givenInvalidId_whenGetById_thenReturnWidget() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");

        when(repo.getById(id)).thenReturn(Optional.empty());
        Optional<Widget> actual = service.getById(id);

        assertEquals(false, actual.isPresent());
    }

    @Test
    void givenPageAndSize_whenGetAll_thenReturnPage() {
        int page = 1;
        int size = 10;
        when(repo.count()).thenReturn(0);
        when(repo.list(null, null, page, size))
                .thenReturn(new WidgetRepository.Page(Collections.emptyList(), page, size, 0));

        WidgetRepository.Page p = service.getAll(page, size);

        assertEquals(page, p.getPage());
        assertEquals(size, p.getSize());
        assertEquals(0, p.getWidgets().size());
        assertEquals(0, p.getTotalItems());
        assertEquals(0, p.getTotalPages());
    }

    @Test
    void givenId_whenDelete_thenCallDeleteFromRepo() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        service.delete(id);
        verify(repo, times(1)).delete(id);
    }

}

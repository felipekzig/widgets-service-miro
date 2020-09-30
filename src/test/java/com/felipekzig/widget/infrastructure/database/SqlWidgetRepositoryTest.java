package com.felipekzig.widget.infrastructure.database;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.repository.WidgetRepository.Page;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = "sql")
public class SqlWidgetRepositoryTest {

    @Autowired
    private JpaWidgetRepository jpaRepo;

    private WidgetRepository repository;

    private static Widget build(UUID id, Integer zIndex) {
        return Widget.builder().id(id).coords(new Coordinates(10, 20)).width(240).height(400).zIndex(zIndex).build();
    }

    @BeforeEach
    void beforeEach() {
        repository = new SqlWidgetRepository(jpaRepo);

        jpaRepo.save(build(UUID.fromString("00000000-000-0000-0000-000000000001"), 1));
        jpaRepo.save(build(UUID.randomUUID(), 2));
        jpaRepo.save(build(UUID.randomUUID(), 3));
        jpaRepo.save(build(UUID.randomUUID(), 4));
        jpaRepo.save(build(UUID.randomUUID(), 5));
        jpaRepo.save(build(UUID.randomUUID(), 6));
        jpaRepo.save(build(UUID.randomUUID(), 7));
        jpaRepo.save(build(UUID.randomUUID(), 8));
        jpaRepo.save(build(UUID.randomUUID(), 9));
        jpaRepo.save(build(UUID.randomUUID(), 10));
    }

    @Test
    void givenSqlRepo_whenCount_thenReturnCorrectCount() {
        assertEquals(10L, repository.count());
    }

    @Test
    void givenWidget_whenSave_thenCallJPASave() {
        Widget w = build(UUID.randomUUID(), null);
        repository.save(w);
        assertEquals(11L, repository.count());
    }

    @Test
    void givenId_whenDelete_thenCallJPADelete() {
        repository.delete(UUID.fromString("00000000-000-0000-0000-000000000001"));
        assertEquals(9L, repository.count());
    }

    @Test
    void givenInitializedDatabase_whenListPaginated_thenReturnSortedStream() {
        List<Widget> result = repository.list(null, null, 2, 4).getWidgets();
        Integer[] zIndexes = new Integer[] { 5, 6, 7, 8 };
        assertArrayEquals(zIndexes, result.stream().map(w -> w.getZIndex()).toArray());
    }

    @Test
    void givenFilteredCoordinates_whenList_thenReturnFilteredPageResulst() {
        jpaRepo.deleteAll();

        Coordinates[] coords = new Coordinates[] { new Coordinates(50, 50), new Coordinates(50, 100),
                new Coordinates(100, 100), new Coordinates(-30, 20) };
        int z = 0;

        for (Coordinates c : coords) {
            Widget w = Widget.builder().id(UUID.randomUUID()).coords(c).width(100).height(100).zIndex(++z).build();
            repository.save(w);
        }

        Page page = repository.list(new Coordinates(0, 0), new Coordinates(100, 150), 1, 4);
        List<Widget> results = page.getWidgets();

        assertEquals(2, results.size());
        assertArrayEquals(new Coordinates[] { new Coordinates(50, 50), new Coordinates(50, 100) },
                results.stream().map(w -> w.getCoords()).toArray());
    }

    @Test
    void givenZIndex_whenRetrieveGreatedThanOrEqual_thenReturnStream() {
        Stream<Widget> result = repository.getWidgetsWithZIndexGreaterThanOrEqualTo(4);

        Integer[] zIndexes = new Integer[] { 4, 5, 6, 7, 8, 9, 10 };
        assertArrayEquals(zIndexes, result.map(w -> w.getZIndex()).toArray());
    }

    @Test
    void givenInvalidId_whenGetById_thenReturnWidget() {
        Optional<Widget> widget = repository.getById(UUID.randomUUID());
        assertEquals(false, widget.isPresent());
    }

    @Test
    void givenId_whenGetById_thenReturnWidget() {
        UUID id = UUID.randomUUID();
        repository.save(build(id, 10));

        Widget widget = repository.getById(id).get();

        assertEquals(id, widget.getId());
    }

    @Test
    void givenInitializedRepository_whenGetForeground_thenReturnWidgetWithGreaterZIndex() {
        Optional<Widget> opWidget = repository.getForegroundWidget();

        assertEquals(true, opWidget.isPresent());
        assertEquals(10, opWidget.get().getZIndex());
    }

    @Test
    void givenEmptyRepository_whenGetForeground_thenReturnEmptyOptional() {
        jpaRepo.deleteAll();
        Optional<Widget> opWidget = repository.getForegroundWidget();
        assertEquals(false, opWidget.isPresent());
    }

    @Test
    void givenExistingZIndex_whenGetByZIndex_thenReturnWidget() {
        jpaRepo.deleteAll();
        UUID id = UUID.randomUUID();
        repository.save(build(UUID.randomUUID(), 9));
        repository.save(build(id, 10));
        repository.save(build(UUID.randomUUID(), 11));

        Optional<Widget> opWidget = repository.getByZIndex(10);

        assertEquals(true, opWidget.isPresent());
        assertEquals(id, opWidget.get().getId());
    }

    @Test
    void givenNonExistingZIndex_whenGetByZIndex_thenEmpty() {
        Optional<Widget> opWidget = repository.getByZIndex(50);
        assertEquals(false, opWidget.isPresent());
    }

}

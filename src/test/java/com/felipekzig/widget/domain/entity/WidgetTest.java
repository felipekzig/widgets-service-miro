package com.felipekzig.widget.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.UUID;

import com.felipekzig.widget.domain.vo.Coordinates;

import org.junit.jupiter.api.Test;

public class WidgetTest {

    @Test
    void givenDefaultAttributes_whenBuild_thenReturnWidget() {
        Widget w = Widget.builder().coords(new Coordinates(10, 10)).width(100).height(40).build();

        assertEquals(100, w.getWidth());
        assertEquals(40, w.getHeight());
        assertEquals(new Coordinates(10, 10), w.getCoords());

        assertNull(w.getId());
        assertNull(w.getZIndex());
        assertNotNull(w.getModifiedAt());
    }

    @Test
    void givenWidgetWithEmptyId_whenAssignId_thenUpdateId() {
        Widget w = Widget.builder().coords(new Coordinates(10, 10)).width(100).height(40).build();

        w.assignId();
        assertNotNull(w.getId());
    }

    @Test
    void givenWidgetWithId_whenAssignId_thenDoNothing() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        Widget w = Widget.builder().id(id).coords(new Coordinates(10, 10)).width(100).height(40).build();

        w.assignId();
        assertEquals(id, w.getId());
    }

    @Test
    void givenAllAttributes_whenBuild_thenReturnWidget() {
        UUID id = UUID.fromString("00000000-000-0000-0000-000000000001");
        Instant now = Instant.ofEpochSecond(1550000002);

        Widget w = Widget.builder().id(id).zIndex(10).modifiedAt(now).coords(new Coordinates(10, 10)).width(100)
                .height(40).build();

        assertEquals(100, w.getWidth());
        assertEquals(40, w.getHeight());
        assertEquals(new Coordinates(10, 10), w.getCoords());
        assertEquals(id, w.getId());
        assertEquals(10, w.getZIndex());
        assertEquals(now, w.getModifiedAt());
    }

    @Test
    void givenWidget_whenIncreaseZIndex_thenUpdateZIndex() {
        Widget w = Widget.builder().coords(new Coordinates(10, 100)).width(100).height(200).zIndex(1).build();
        w.increaseZIndex(10);

        assertEquals(11, w.getZIndex());
    }

    @Test
    void givenTheSameWidgetOrNull_whenPlaceInFront_thenDoNothing() {
        Widget foreground = Widget.builder().coords(new Coordinates(10, 100)).width(100).height(200).zIndex(10).build();
        foreground.placeInFrontOf(foreground);
        assertEquals(10, foreground.getZIndex());

        foreground.placeInFrontOf(null);
        assertEquals(10, foreground.getZIndex());
    }

    @Test
    void givenAnyOtherWidget_whenPlaceInFront_thenUpdateZIndexUpdated() {
        Widget foreground = Widget.builder().coords(new Coordinates(10, 100)).width(100).height(200).zIndex(10).build();
        Widget widget = Widget.builder().coords(new Coordinates(10, 100)).width(100).height(200).zIndex(1).build();

        widget.placeInFrontOf(foreground);
        assertEquals(11, widget.getZIndex());
    }

    @Test
    void givenNullCoords_whenBuild_throwException() {
        assertThrows(NullPointerException.class, () -> {
            Widget.builder().coords(null).width(100).height(200).zIndex(10).build();
        });
    }

    @Test
    void givenNullWidth_whenBuild_throwException() {
        assertThrows(NullPointerException.class, () -> {
            Widget.builder().coords(new Coordinates(10, 10)).width(null).height(200).zIndex(10).build();
        });
    }

    @Test
    void givenNullHeight_whenBuild_throwException() {
        assertThrows(NullPointerException.class, () -> {
            Widget.builder().coords(new Coordinates(10, 10)).width(200).height(null).zIndex(10).build();
        });
    }

}

package com.felipekzig.widget.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.UUID;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.junit.jupiter.api.Test;

public class WidgetDTOTest {

    @Test
    void givenDTO_whenBasicDataMethods_thenValidateLombok() {
        WidgetDTO dto1 = new WidgetDTO(UUID.randomUUID(), 10, 10, null, 1, Instant.now());
        WidgetDTO dto2 = new WidgetDTO(UUID.randomUUID(), 10, 10, null, 1, Instant.now());

        assertNotNull(dto1.toString());
        assertNotEquals(0, dto1.hashCode());
        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenEntity_whenConvertToDTO_thenReturnValidDTO() {
        Widget entity = Widget.builder().id(UUID.randomUUID()).coords(new Coordinates(10, 10)).width(100).height(40)
                .zIndex(10).build();
        WidgetDTO dto = WidgetDTO.fromEntity(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCoords(), dto.getCoords());
        assertEquals(entity.getWidth(), dto.getWidth());
        assertEquals(entity.getHeight(), dto.getHeight());
        assertEquals(entity.getZIndex(), dto.getZIndex());
        assertEquals(entity.getModifiedAt(), dto.getModifiedAt());
    }

    @Test
    void givenDTO_whenConvertToEntity_thenReturnValidEntity() {
        WidgetDTO dto = new WidgetDTO();
        dto.setId(UUID.randomUUID());
        dto.setCoords(new Coordinates(10, 10));
        dto.setWidth(10);
        dto.setHeight(100);
        dto.setZIndex(1);
        dto.setModifiedAt(Instant.now());

        Widget entity = dto.toEntity();

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getCoords(), entity.getCoords());
        assertEquals(dto.getWidth(), entity.getWidth());
        assertEquals(dto.getHeight(), entity.getHeight());
        assertEquals(dto.getZIndex(), entity.getZIndex());
        assertEquals(dto.getModifiedAt(), entity.getModifiedAt());
    }
}
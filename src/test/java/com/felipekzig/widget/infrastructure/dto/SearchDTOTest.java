package com.felipekzig.widget.infrastructure.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.felipekzig.widget.domain.vo.Coordinates;

import org.junit.jupiter.api.Test;

public class SearchDTOTest {

    @Test
    void givenSearchDTO_whenBasicDataMethods_thenValidateLombok() {
        SearchDTO dto1 = new SearchDTO(new Coordinates(), new Coordinates(), 1, 10);
        SearchDTO dto2 = new SearchDTO(1, 10);

        assertNotEquals(0, dto1.hashCode());
        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenInsufficientParameters_whenConstructing_thenThrowException() {
        assertThrows(NullPointerException.class, () -> {
            new SearchDTO(null, null, 1, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new SearchDTO(null, null, null, 10);
        });
    }

    @Test
    void givenSearchDTO_whenGetting_thenReturnValidInfo() {
        SearchDTO dto = new SearchDTO(1, 10);
        assertEquals(null, dto.getBottom());
        assertEquals(null, dto.getTop());
        assertEquals(1, dto.getPage());
        assertEquals(10, dto.getSize());
    }
}
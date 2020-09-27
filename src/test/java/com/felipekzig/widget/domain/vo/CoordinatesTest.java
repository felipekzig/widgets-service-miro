package com.felipekzig.widget.domain.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CoordinatesTest {

    @Test
    void givenTwoExactCoords_whenEquals_thenReturnTrue() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(0, 0);
        assertEquals(c1, c2);
    }

    @Test
    void givenExactCoordinates_whenCompare_thenReturnValidInteger() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(0, 0);
        assertEquals(0, c1.compareTo(c2));
    }

    @Test
    void givenNullCoordinates_whenCompare_thenReturnZero() {
        Coordinates c1 = new Coordinates(0, 0);
        assertEquals(0, c1.compareTo(null));
    }

    @Test
    void givenTwoCoordinates_whenCompare_thenReturnValidInteger() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(50, 50);

        assertEquals(-1, c1.compareTo(c2));
        assertEquals(1, c2.compareTo(c1));
    }

    @Test
    void givenTwoCoordinatesWithSameX_whenCompare_thenReturnValidInteger() {
        Coordinates c1 = new Coordinates(50, 0);
        Coordinates c2 = new Coordinates(50, 50);

        assertEquals(-1, c1.compareTo(c2));
        assertEquals(1, c2.compareTo(c1));
    }

    @Test
    void givenTwoCoordinatesWithSameY_whenCompare_thenReturnValidInteger() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(50, 0);

        assertEquals(-1, c1.compareTo(c2));
        assertEquals(1, c2.compareTo(c1));
    }

}

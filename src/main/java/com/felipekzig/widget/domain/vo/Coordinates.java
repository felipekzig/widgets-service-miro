package com.felipekzig.widget.domain.vo;

import lombok.Value;

@Value
public class Coordinates implements Comparable<Coordinates> {
    private Integer x;
    private Integer y;

    @Override
    public int compareTo(Coordinates o) {
        if (o == null)
            return 0;
        return x == o.x ? y.compareTo(o.y) : x.compareTo(o.x);
    }
}

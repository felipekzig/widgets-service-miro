package com.felipekzig.widget.domain.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Coordinates implements Comparable<Coordinates> {
    private Integer x;
    private Integer y;

    @Override
    public int compareTo(Coordinates o) {
        if (o == null)
            return 0;

        if (x.compareTo(o.x) == 0 && y.compareTo(o.y) == 0) {
            return 0;
        } else if (x.compareTo(o.x) > 0 && y.compareTo(o.y) > 0) {
            return 1;
        } else {
            return x == o.x ? y.compareTo(o.y) : x.compareTo(o.x);
        }
    }
}

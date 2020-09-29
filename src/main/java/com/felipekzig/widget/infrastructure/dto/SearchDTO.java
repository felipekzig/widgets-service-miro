package com.felipekzig.widget.infrastructure.dto;

import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class SearchDTO {

    private Coordinates bottom;
    private Coordinates top;

    @NonNull
    private Integer page;

    @NonNull
    private Integer size;
}

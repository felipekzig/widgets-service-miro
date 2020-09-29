package com.felipekzig.widget.infrastructure.dto;

import java.time.Instant;
import java.util.UUID;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetDTO {
    private UUID id;

    private Integer width;

    private Integer height;

    private Coordinates coords;

    private Integer zIndex;

    private Instant modifiedAt;

    public Widget toEntity() {
        return Widget.builder().id(id).width(width).height(height).coords(coords).zIndex(zIndex).modifiedAt(modifiedAt)
                .build();
    }

    public static WidgetDTO fromEntity(Widget entity) {
        return new WidgetDTO(entity.getId(), entity.getWidth(), entity.getHeight(), entity.getCoords(),
                entity.getZIndex(), entity.getModifiedAt());
    }
}

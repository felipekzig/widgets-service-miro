package com.felipekzig.widget.domain.entity;

import java.time.Instant;
import java.util.UUID;

import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "WidgetBuilder")
public class Widget {

    private UUID id;

    @NonNull
    private Integer width;

    @NonNull
    private Integer height;

    @NonNull
    private Coordinates coords;

    private Integer zIndex;

    private Instant modifiedAt;

    public void assignId() {
        if (id == null) {
            id = UUID.randomUUID();
            this.updateModifiedAt();
        }
    }

    public void increaseZIndex(Integer amount) {
        if (zIndex == null)
            zIndex = 0;
        this.zIndex += amount;
        this.updateModifiedAt();
    }

    public void placeInFrontOf(Widget w) {
        if (w != null && !w.equals(this)) {
            zIndex = w.zIndex + 1;
            this.updateModifiedAt();
        }
    }

    public void updateModifiedAt() {
        modifiedAt = Instant.now();
    }

    public static class WidgetBuilder {
        private Instant modifiedAt = Instant.now();
    }
}
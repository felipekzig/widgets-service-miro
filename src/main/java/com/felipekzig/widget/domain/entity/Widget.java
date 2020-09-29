package com.felipekzig.widget.domain.entity;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "widgets")
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "WidgetBuilder")
public class Widget {

    @Id
    private UUID id;

    @NonNull
    private Integer width;

    @NonNull
    private Integer height;

    @NonNull
    @Embedded
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

    public boolean isInsideSquare(Coordinates bottomLeft, Coordinates topRight) {
        Coordinates bottom = new Coordinates(getCoords().getX() - getWidth() / 2, getCoords().getY() - getHeight() / 2);
        Coordinates top = new Coordinates(getCoords().getX() + getWidth() / 2, getCoords().getY() + getHeight() / 2);

        return bottom.compareTo(bottomLeft) >= 0 && top.compareTo(topRight) <= 0;
    }

    public static class WidgetBuilder {
        private Instant modifiedAt = Instant.now();
    }
}
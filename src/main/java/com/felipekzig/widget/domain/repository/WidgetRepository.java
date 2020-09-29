package com.felipekzig.widget.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface WidgetRepository {

    @Getter
    @AllArgsConstructor
    public static class Page {
        private List<Widget> widgets;
        private Integer page;
        private Integer size;
        private long totalItems;

        @Getter(lazy = true)
        private final long totalPages = calculateTotalPages();

        private long calculateTotalPages() {
            return (long) Math.ceil(totalItems / size);
        }
    }

    void save(Widget widget);

    void delete(UUID id);

    Optional<Widget> getByZIndex(Integer zIndex);

    Optional<Widget> getById(UUID id);

    Optional<Widget> getForegroundWidget();

    Long count();

    WidgetRepository.Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer widgetsPerPage);

    Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer zIndex);

}

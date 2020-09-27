package com.felipekzig.widget.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.vo.Coordinates;

import lombok.Getter;
import lombok.Value;

public interface WidgetRepository {

    @Value
    public static class Page {
        List<Widget> widgets;
        Integer page;
        Integer size;
        long totalItems;

        @Getter(lazy = true)
        long totalPages = calculateTotalPages();

        private long calculateTotalPages() {
            return (long) Math.ceil(totalItems / size);
        }
    }

    void save(Widget widget);

    void delete(UUID id);

    Optional<Widget> getByZIndex(Integer zIndex);

    Optional<Widget> getById(UUID id);

    Optional<Widget> getForegroundWidget();

    Integer count();

    WidgetRepository.Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer widgetsPerPage);

    Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer zIndex);

}

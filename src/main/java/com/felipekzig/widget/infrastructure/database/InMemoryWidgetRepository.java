package com.felipekzig.widget.infrastructure.database;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!sql")
@Component
public class InMemoryWidgetRepository implements WidgetRepository {

    private Map<UUID, Widget> database = new ConcurrentHashMap<>();

    @Override
    public void save(Widget widget) {
        database.put(widget.getId(), widget);
    }

    @Override
    public void delete(UUID id) {
        if (database.containsKey(id)) {
            database.remove(id);
        }
    }

    @Override
    public Optional<Widget> getByZIndex(Integer zIndex) {
        for (Widget w : database.values()) {
            if (w.getZIndex() == zIndex)
                return Optional.of(w);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Widget> getById(UUID id) {
        return Optional.ofNullable(database.getOrDefault(id, null));
    }

    @Override
    public Optional<Widget> getForegroundWidget() {
        Widget foreground = null;
        for (Widget w : database.values()) {
            if (foreground == null || w.getZIndex() > foreground.getZIndex()) {
                foreground = w;
            }
        }
        return Optional.ofNullable(foreground);
    }

    @Override
    public Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer size) {
        if (page == null)
            page = 1;
        if (size == null)
            size = 10;

        // @formatter:off
        List<Widget> results = database.values().parallelStream()
            .filter(w -> w.isInsideSquare(bottomLeft, topRight))
            .sorted((w1, w2) -> w1.getZIndex().compareTo(w2.getZIndex()))
            .skip((page - 1) * size)
            .limit(size)
            .collect(Collectors.toList());
        // @formatter:on

        return new Page(results, page, size, count());
    }

    @Override
    public Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer startAt) {
        return database.values().parallelStream().filter(widget -> widget.getZIndex() >= startAt)
                .sorted((w1, w2) -> w1.getZIndex().compareTo(w2.getZIndex()));
    }

    @Override
    public Long count() {
        return Long.valueOf(database.values().size());
    }
}

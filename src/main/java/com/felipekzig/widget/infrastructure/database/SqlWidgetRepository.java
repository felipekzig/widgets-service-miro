package com.felipekzig.widget.infrastructure.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.criteria.Predicate;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Profile("sql")
@Component
public class SqlWidgetRepository implements WidgetRepository {

    private JpaWidgetRepository jpaRepository;

    public SqlWidgetRepository(JpaWidgetRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Widget widget) {
        jpaRepository.save(widget);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Widget> getByZIndex(Integer zIndex) {
        return jpaRepository.findByZIndex(zIndex);
    }

    @Override
    public Optional<Widget> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Widget> getForegroundWidget() {
        return jpaRepository.findFirstByOrderByZIndexDesc();
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer size) {
        // @formatter:off
        Specification<Widget> spec = Specification
            .where(greaterThanOrEqualToCoord(bottomLeft))
            .and(lessThanOrEqualToCoord(topRight))
            .and((root, query, builder) -> {
                query.orderBy(builder.asc(root.get("zIndex")));
                return null;
            });
        // @formatter:on

        List<Widget> widgets = jpaRepository.search(spec, PageRequest.of(page, size));
        return new Page(widgets, page, size, widgets.size());
    }

    @Override
    public Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer zIndex) {
        return jpaRepository.findByZIndexGreaterThanOrEqualTo(zIndex).stream();
    }

    // JPA Specs for filtering
    private static Specification<Widget> greaterThanOrEqualToCoord(Coordinates coord) {
        return (root, query, builder) -> {
            Predicate predicateX = builder.greaterThanOrEqualTo(root.get("coords").get("x"), coord.getX());
            Predicate predicateY = builder.greaterThanOrEqualTo(root.get("coords").get("y"), coord.getY());
            return builder.and(predicateX, predicateY);
        };
    }

    private static Specification<Widget> lessThanOrEqualToCoord(Coordinates coord) {
        return (root, query, builder) -> {
            Predicate predicateX = builder.lessThanOrEqualTo(root.get("coords").get("x"), coord.getX());
            Predicate predicateY = builder.lessThanOrEqualTo(root.get("coords").get("y"), coord.getY());
            return builder.and(predicateX, predicateY);
        };
    }

}

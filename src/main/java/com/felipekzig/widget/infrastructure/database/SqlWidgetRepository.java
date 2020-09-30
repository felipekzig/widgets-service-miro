package com.felipekzig.widget.infrastructure.database;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.ExpressionImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Profile("sql")
@Component
public class SqlWidgetRepository implements WidgetRepository {

    private JpaWidgetRepository jpaRepository;

    private static final String ZINDEX_FIELD = "zIndex";
    private static final String COORDS_FIELD = "coords";

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
        return jpaRepository.findByzIndex(zIndex);
    }

    @Override
    public Optional<Widget> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Widget> getForegroundWidget() {
        // @formatter:off
        Specification<Widget> spec = Specification
            .where((root, query, builder) -> {
                query.orderBy(builder.desc(root.get(ZINDEX_FIELD)));
                return null;
            });
        // @formatter:on

        org.springframework.data.domain.Page<Widget> jpaPage = jpaRepository.findAll(spec, PageRequest.of(0, 1));
        return jpaPage.get().findFirst();
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer size) {
        // @formatter:off
        Specification<Widget> spec = Specification
            .where(bottomLeft == null ? null : greaterThanOrEqualToCoord(bottomLeft))
            .and(topRight == null ? null : lessThanOrEqualToCoord(topRight))
            .and((root, query, builder) -> {
                query.orderBy(builder.asc(root.get(ZINDEX_FIELD)));
                return null;
            });
        // @formatter:on

        org.springframework.data.domain.Page<Widget> jpaPage = jpaRepository.findAll(spec,
                PageRequest.of(page - 1, size));

        return new Page(jpaPage.getContent(), page, size, jpaPage.getTotalElements());
    }

    @Override
    public Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer zIndex) {
        // @formatter:off
        Specification<Widget> spec = Specification
            .where((root, query, builder) -> builder.greaterThanOrEqualTo(root.get(ZINDEX_FIELD), zIndex));
        // @formatter:on
        return jpaRepository.findAll(spec).stream();
    }

    // JPA Specs for filtering
    private static Specification<Widget> greaterThanOrEqualToCoord(Coordinates coord) {

        return (root, query, builder) -> {
            Expression<Integer> coordX = root.get(COORDS_FIELD).get("x");
            Expression<Integer> coordY = root.get(COORDS_FIELD).get("y");

            Expression<Integer> expX = builder.diff(coordX, builder.quot(root.get("width"), 2)).as(Integer.class);
            Expression<Integer> expY = builder.diff(coordY, builder.quot(root.get("height"), 2)).as(Integer.class);

            Predicate predicateX = builder.greaterThanOrEqualTo(expX, coord.getX());
            Predicate predicateY = builder.greaterThanOrEqualTo(expY, coord.getY());
            return builder.and(predicateX, predicateY);
        };
    }

    private static Specification<Widget> lessThanOrEqualToCoord(Coordinates coord) {
        return (root, query, builder) -> {
            Expression<Integer> coordX = root.get(COORDS_FIELD).get("x");
            Expression<Integer> coordY = root.get(COORDS_FIELD).get("y");

            Expression<Integer> expX = builder.sum(coordX, builder.quot(root.get("width"), 2)).as(Integer.class);
            Expression<Integer> expY = builder.sum(coordY, builder.quot(root.get("height"), 2)).as(Integer.class);

            Predicate predicateX = builder.lessThanOrEqualTo(expX, coord.getX());
            Predicate predicateY = builder.lessThanOrEqualTo(expY, coord.getY());

            return builder.and(predicateX, predicateY);
        };
    }

}

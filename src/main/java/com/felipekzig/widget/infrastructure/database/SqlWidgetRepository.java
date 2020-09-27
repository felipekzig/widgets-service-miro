package com.felipekzig.widget.infrastructure.database;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("sql")
@Component
public class SqlWidgetRepository implements WidgetRepository {

    @Override
    public void save(Widget widget) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(UUID id) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Widget> getByZIndex(Integer zIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Widget> getById(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Widget> getForegroundWidget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer count() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page list(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer widgetsPerPage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stream<Widget> getWidgetsWithZIndexGreaterThanOrEqualTo(Integer zIndex) {
        // TODO Auto-generated method stub
        return null;
    }

}

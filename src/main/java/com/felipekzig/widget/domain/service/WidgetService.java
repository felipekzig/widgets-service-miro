package com.felipekzig.widget.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.repository.WidgetRepository.Page;
import com.felipekzig.widget.domain.vo.Coordinates;

import org.springframework.stereotype.Service;

@Service
public class WidgetService {

    private WidgetRepository repository;

    public WidgetService(WidgetRepository repository) {
        this.repository = repository;
    }

    public Widget create(Widget widget) {
        widget.assignId();
        return save(widget);
    }

    public Widget update(UUID id, Widget widget) {
        if (!widget.getId().equals(id)) {
            throw new RuntimeException("Route ID is different from body");
        }

        return save(widget);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }

    public Optional<Widget> getById(UUID id) {
        return repository.getById(id);
    }

    public Page getAll(Coordinates bottomLeft, Coordinates topRight, Integer page, Integer size) {
        return repository.list(bottomLeft, topRight, page, size);
    }

    public Page getAll(Integer page, Integer size) {
        return getAll(null, null, page, size);
    }

    private Widget save(Widget widget) {
        synchronized (repository) {
            if (widget.getZIndex() == null) {
                Optional<Widget> foreground = repository.getForegroundWidget();
                if (foreground.isPresent()) {
                    widget.placeInFrontOf(foreground.get());
                } else {
                    widget.increaseZIndex(1);
                }
            } else {
                int startAt = widget.getZIndex();

                List<Widget> widgets = repository.getWidgetsWithZIndexGreaterThanOrEqualTo(startAt)
                        .collect(Collectors.toList());

                for (Widget w : widgets) {
                    if (w.getZIndex() == startAt) {
                        w.increaseZIndex(1);
                        repository.save(w);
                        startAt++;
                    } else {
                        break;
                    }
                }
            }

            widget.updateModifiedAt();
            repository.save(widget);
            return widget;
        }
    }
}

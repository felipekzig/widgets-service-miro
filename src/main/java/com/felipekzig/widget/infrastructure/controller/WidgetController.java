package com.felipekzig.widget.infrastructure.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.service.WidgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WidgetController {

    @Autowired
    private WidgetService service;

    @PostMapping("/widgets")
    public ResponseEntity<Widget> create(@RequestBody Widget resource) {
        Widget createdWidget = service.create(resource);
        return ResponseEntity.created(URI.create("/widget/" + createdWidget.getId())).body(createdWidget);
    }

    @PutMapping("/widgets/{uuid}")
    public ResponseEntity<Widget> update(@PathVariable("uuid") UUID uuid, @RequestBody Widget widget) {
        // widget.setId(uuid);
        Widget updatedWidget = service.update(uuid, widget);

        return ResponseEntity.ok(updatedWidget);
    }

    @DeleteMapping("/widgets/{uuid}")
    public ResponseEntity<Widget> delete(@PathVariable("uuid") UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/widgets")
    public ResponseEntity<WidgetRepository.Page> list(@RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        if (size == null) {
            size = 10;
        }
        if (page == null) {
            page = 1;
        }
        return ResponseEntity.ok(service.getAll(page, size));
    }

    @GetMapping("/widgets/{uuid}")
    public ResponseEntity<Widget> getById(@PathVariable("uuid") UUID uuid) {
        Optional<Widget> widget = service.getById(uuid);

        if (!widget.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(widget.get());
    }

}

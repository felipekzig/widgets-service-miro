package com.felipekzig.widget.infrastructure.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.repository.WidgetRepository;
import com.felipekzig.widget.domain.service.WidgetService;
import com.felipekzig.widget.infrastructure.dto.SearchDTO;
import com.felipekzig.widget.infrastructure.dto.WidgetDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, value = "/widgets")
public class WidgetController {

    @Autowired
    private WidgetService service;

    @GetMapping()
    public ResponseEntity<WidgetRepository.Page> search(@RequestBody() SearchDTO search) {
        return ResponseEntity
                .ok(service.getAll(search.getBottom(), search.getTop(), search.getPage(), search.getSize()));
    }

    @PostMapping()
    public ResponseEntity<WidgetDTO> create(@RequestBody WidgetDTO dto) {
        Widget createdWidget = service.create(dto.toEntity());
        return ResponseEntity.created(URI.create("/widget/" + createdWidget.getId()))
                .body(WidgetDTO.fromEntity(createdWidget));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Object> update(@PathVariable("uuid") UUID uuid, @RequestBody WidgetDTO dto) {
        try {
            Widget updatedWidget = service.update(uuid, dto.toEntity());
            return ResponseEntity.ok(WidgetDTO.fromEntity(updatedWidget));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable("uuid") UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<WidgetDTO> getById(@PathVariable("uuid") UUID uuid) {
        Optional<Widget> widget = service.getById(uuid);

        if (!widget.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(WidgetDTO.fromEntity(widget.get()));
    }

}

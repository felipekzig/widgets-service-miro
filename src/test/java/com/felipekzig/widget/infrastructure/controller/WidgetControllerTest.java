package com.felipekzig.widget.infrastructure.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.felipekzig.widget.domain.entity.Widget;
import com.felipekzig.widget.domain.service.WidgetService;
import com.felipekzig.widget.domain.vo.Coordinates;
import com.felipekzig.widget.infrastructure.dto.WidgetDTO;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(WidgetController.class)
public class WidgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WidgetService service;

    @Test
    void givenUUID_whenGetById_thenReturnWidget() throws Exception {
        UUID id = UUID.randomUUID();
        Widget w = Widget.builder().id(id).coords(new Coordinates(10, 10)).width(100).height(100).build();

        when(service.getById(id)).thenReturn(Optional.of(w));

        mvc.perform(get("/widgets/" + id.toString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())));
    }

    @Test
    void givenUUID_whenDelete_thenReturnOk() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/widgets/" + id.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenNewWidget_whenCreate_thenReturnWidgetFullDesc() throws Exception {
        WidgetDTO dto = new WidgetDTO(null, 300, 20, new Coordinates(1, 1), 0, null);
        Widget w = dto.toEntity();
        w.assignId();

        when(service.create(dto.toEntity())).thenReturn(w);

        mvc.perform(post("/widgets").contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(w.getId().toString())))
                .andExpect(jsonPath("$.width", is(w.getWidth())))
                .andExpect(jsonPath("$.modifiedAt", is(w.getModifiedAt().toString())));
    }

    @Test
    void givenExistingWidgetAndMismatchingId_whenUpdate_thenReturnBadRequest() throws Exception {
        WidgetDTO dto = new WidgetDTO(UUID.randomUUID(), 300, 20, new Coordinates(1, 1), 0, null);

        mvc.perform(put("/widgets/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExistingWidgetAndMatchingId_whenUpdate_thenReturnUpdatedInstance() throws Exception {
        WidgetDTO dto = new WidgetDTO(UUID.randomUUID(), 300, 20, new Coordinates(1, 1), 0, null);

        when(service.update(ArgumentMatchers.eq(dto.getId()), ArgumentMatchers.any(Widget.class)))
                .thenReturn(dto.toEntity());

        // @formatter:off
        mvc.perform(
            put("/widgets/" + dto.getId().toString()).contentType(MediaType.APPLICATION_JSON).content(toJSON(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId().toString())))
                .andExpect(jsonPath("$.coords.x", is(dto.getCoords().getX())))
                .andExpect(jsonPath("$.zindex", is(dto.getZIndex())));
        // @formatter:on
    }

    private static String toJSON(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}

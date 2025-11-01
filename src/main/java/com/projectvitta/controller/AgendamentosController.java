package com.projectvitta.controller;

import com.projectvitta.api.AgendamentosApi;
import com.projectvitta.model.AgendamentosEntity;
import com.projectvitta.service.AgendamentosService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AgendamentosController implements AgendamentosApi {

    private final AgendamentosService agendamentosService;

    public AgendamentosController(AgendamentosService agendamentosService) {
        this.agendamentosService = agendamentosService;
    }

    @Override
    public List<AgendamentosEntity> getAll() {
        return agendamentosService.getAll();
    }

    @Override
    public AgendamentosEntity getById(Long id) {
        return agendamentosService.getById(id).orElseThrow();
    }

    @Override
    public List<AgendamentosEntity> getByUsuario(Long idUsuario) {
        return agendamentosService.getByUsuario(idUsuario);
    }

    @Override
    public AgendamentosEntity create(AgendamentosEntity agendamento) {
        return agendamentosService.create(agendamento);
    }

    @Override
    public AgendamentosEntity update(Long id, AgendamentosEntity agendamento) {
        return agendamentosService.update(id, agendamento);
    }

    @Override
    public void delete(Long id) {
        agendamentosService.delete(id);
    }
}

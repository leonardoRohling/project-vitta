package com.projectvitta.api;

import com.projectvitta.model.AgendamentosEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AgendamentosApi {

    @GetMapping("/api/agendamentos")
    List<AgendamentosEntity> getAll();

    @GetMapping("/api/agendamentos/{id}")
    AgendamentosEntity getById(@PathVariable Long id);

    @GetMapping("/api/agendamentos/usuario/{idUsuario}")
    List<AgendamentosEntity> getByUsuario(@PathVariable Long idUsuario);

    @PostMapping("/api/agendamentos")
    AgendamentosEntity create(@RequestBody AgendamentosEntity agendamento);

    @PutMapping("/api/agendamentos/{id}")
    AgendamentosEntity update(@PathVariable Long id, @RequestBody AgendamentosEntity agendamento);

    @DeleteMapping("/api/agendamentos/{id}")
    void delete(@PathVariable Long id);
}

package com.projectvitta.service;

import com.projectvitta.model.AgendamentosEntity;
import com.projectvitta.repository.AgendamentosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendamentosService {

    private final AgendamentosRepository agendamentosRepository;

    public AgendamentosService(AgendamentosRepository agendamentosRepository) {
        this.agendamentosRepository = agendamentosRepository;
    }

    public AgendamentosEntity create(AgendamentosEntity agendamento) {
        return agendamentosRepository.save(agendamento);
    }

    public Optional<AgendamentosEntity> getById(Long id) {
        return agendamentosRepository.findById(id);
    }

    public List<AgendamentosEntity> getAll() {
        return agendamentosRepository.findAll();
    }

    public List<AgendamentosEntity> getByUsuario(Long idUsuario) {
        return agendamentosRepository.findByIdUsuario(idUsuario);
    }

    public void delete(Long id) {
        agendamentosRepository.deleteById(id);
    }

    public AgendamentosEntity update(Long id, AgendamentosEntity novoAgendamento) {
        return agendamentosRepository.findById(id)
                .map(a -> {
                    a.setIdUsuario(novoAgendamento.getIdUsuario());
                    a.setDataConsulta(novoAgendamento.getDataConsulta());
                    a.setHoraConsulta(novoAgendamento.getHoraConsulta());
                    a.setDescricao(novoAgendamento.getDescricao());
                    a.setStatus(novoAgendamento.getStatus());
                    return agendamentosRepository.save(a);
                })
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }
}

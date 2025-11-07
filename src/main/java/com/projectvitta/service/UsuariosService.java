package com.projectvitta.service;

import com.projectvitta.model.UsuariosEntity;
import com.projectvitta.repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    // Criar novo usuário (cadastro)
    public UsuariosEntity create(UsuariosEntity usuario) {
        return usuariosRepository.save(usuario);
    }

    // Buscar usuário por ID
    public Optional<UsuariosEntity> getById(Long id) {
        return usuariosRepository.findById(id);
    }

    // Listar todos
    public List<UsuariosEntity> getAll() {
        return usuariosRepository.findAll();
    }

    public List<UsuariosEntity> getPacientes() {
        return usuariosRepository.findAll()
                .stream()
                .filter(u -> "PACIENTE".equalsIgnoreCase(u.getTipoUsuario()))
                .toList();
    }

    // Deletar
    public void delete(Long id) {
        usuariosRepository.deleteById(id);
    }

    // Login
    public Optional<UsuariosEntity> login(String login, String senha) {
        Optional<UsuariosEntity> usuario = usuariosRepository.findByLogin(login);
        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return usuario;
        }
        return Optional.empty();
    }
}

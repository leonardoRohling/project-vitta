package com.projectvitta.controller;

import com.projectvitta.api.UsuariosApi;
import com.projectvitta.model.UsuariosEntity;
import com.projectvitta.service.UsuariosService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuariosController implements UsuariosApi {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @Override
    public List<UsuariosEntity> getAll() {
        return usuariosService.getAll();
    }

    @Override
    public UsuariosEntity getById(Long id) {
        return usuariosService.getById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public UsuariosEntity create(UsuariosEntity usuario) {
        return usuariosService.create(usuario);
    }

    @Override
    public UsuariosEntity login(String login, String senha) {
        return usuariosService.login(login, senha)
                .orElseThrow(() -> new RuntimeException("Login ou senha incorretos"));
    }

    @Override
    public void delete(Long id) {
        usuariosService.delete(id);
    }
}

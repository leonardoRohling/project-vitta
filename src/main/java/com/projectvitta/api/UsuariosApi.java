package com.projectvitta.api;

import com.projectvitta.model.UsuariosEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UsuariosApi {

    @GetMapping("/api/usuarios")
    List<UsuariosEntity> getAll();

    @GetMapping("/api/usuarios/{id}")
    UsuariosEntity getById(@PathVariable Long id);

    @PostMapping("/api/usuarios")
    UsuariosEntity create(@RequestBody UsuariosEntity usuario);

    @PostMapping("/api/usuarios/login")
    UsuariosEntity login(@RequestParam String login, @RequestParam String senha);

    @DeleteMapping("/api/usuarios/{id}")
    void delete(@PathVariable Long id);
}

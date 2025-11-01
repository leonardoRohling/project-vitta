package com.projectvitta.repository;

import com.projectvitta.model.AgendamentosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentosRepository extends JpaRepository<AgendamentosEntity, Long> {
    List<AgendamentosEntity> findByIdUsuario(Long idUsuario);
}

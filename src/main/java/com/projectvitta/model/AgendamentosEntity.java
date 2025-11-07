package com.projectvitta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agendamentos")
public class AgendamentosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario; // FK simples (sem JOIN obrigatório)

    private LocalDate dataConsulta;

    private LocalTime horaConsulta;

    private String descricao;

    private String status = "AGENDADA"; // AGENDADA, CONFIRMADA, CANCELADA
}

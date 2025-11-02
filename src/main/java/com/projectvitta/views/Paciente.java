package com.projectvitta.views;

import com.projectvitta.model.AgendamentosEntity;
import com.projectvitta.model.UsuariosEntity;
import com.projectvitta.service.AgendamentosService;
import com.projectvitta.service.UsuariosService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Paciente {

    private final UsuariosService usuariosService;
    private final AgendamentosService agendamentosService;

    public Paciente(UsuariosService usuariosService, AgendamentosService agendamentosService) {
        this.usuariosService = usuariosService;
        this.agendamentosService = agendamentosService;
    }

    public void menuPaciente(Scanner sc, UsuariosEntity paciente) {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("         MENU PACIENTE - VITTA");
            System.out.println("=====================================");
            System.out.println("1 - Listar minhas consultas");
            System.out.println("2 - Solicitar consulta");
            System.out.println("3 - Cancelar consulta");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (opcao) {
                case 1 -> listarConsultas(paciente);
                case 2 -> solicitarConsulta(sc, paciente);
                case 3 -> cancelarConsulta(sc, paciente);
                case 0 -> {
                    System.out.println("Saindo do menu paciente...");
                    return; // volta para menu principal
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void buscarProfissional(Scanner sc, UsuariosEntity paciente) {
        List<UsuariosEntity> todos = usuariosService.getAll();

        for (UsuariosEntity u : todos) {
            if (u.getTipoUsuario().equalsIgnoreCase("ADMIN")){

                System.out.println("COD.:" + u.getId());
                System.out.println("NOME: " + u.getNome());

            }
        }
    }

    private void listarConsultas(UsuariosEntity paciente) {
        List<AgendamentosEntity> todos = agendamentosService.getByUsuario(paciente.getId());

        if (todos == null || todos.isEmpty()) {
            System.out.println("\nNenhuma consulta encontrada.");
            return;
        }

        System.out.println("===== AGENDAMENTOS =====\n");

        String[] statusList = {"SOLICITADA", "AGENDADA", "REALIZADA", "CANCELADA", "RECUSADA"};

        for (String status : statusList) {
            System.out.println(">>> " + status + " <<<");
            boolean encontrou = false;

            for (AgendamentosEntity a : todos) {
                if (status.equalsIgnoreCase(a.getStatus())) {

                    System.out.println("PACIENTE: " + paciente.getNome());
                    System.out.println("DESCRIÇÃO: " + a.getDescricao());
                    System.out.println("DATA:   " + a.getDataConsulta());
                    System.out.println("HORA:   " + a.getHoraConsulta());
                    System.out.println("-------------------------------");
                    encontrou = true;
                }
            }

            if (!encontrou) {
                System.out.println("\nNenhuma consulta com status " + status.toLowerCase() + ".");
            }

            System.out.println();
        }
    }


    private void solicitarConsulta(Scanner sc, UsuariosEntity paciente) {
        System.out.println("\n---- SOLICITAR CONSULTA ----");

        System.out.print("Digite a descrição da consulta: ");
        String descricao = sc.nextLine();

        System.out.print("Digite a data da consulta (AAAA-MM-DD): ");
        String dataStr = sc.nextLine();

        System.out.print("Digite a hora da consulta (HH:MM): ");
        String horaStr = sc.nextLine();

        try {
            LocalDate data = LocalDate.parse(dataStr);
            LocalTime hora = LocalTime.parse(horaStr);

            AgendamentosEntity novo = new AgendamentosEntity();
            novo.setIdUsuario(paciente.getId());
            novo.setDescricao(descricao);
            novo.setDataConsulta(data);
            novo.setHoraConsulta(hora);
            novo.setStatus("SOLICITADA");

            agendamentosService.create(novo);

            System.out.println("\n✅ Sua solicitação foi enviada com sucesso. Aguarde a confirmação da sua consulta.");
            System.out.println("\nDados da consulta:\n");
            System.out.println("Descrição: " + descricao);
            System.out.println("Data: " + data);
            System.out.println("Hora: " + hora);

        } catch (Exception e) {
            System.out.println("\n❌ Erro ao solicitar consulta. Verifique os dados digitados.");
        }
    }


    private void cancelarConsulta(Scanner sc, UsuariosEntity paciente) {
        System.out.println("\n---- CANCELAR CONSULTA ----");

        List<AgendamentosEntity> todos = agendamentosService.getByUsuario(paciente.getId());

        if (todos.isEmpty()) {
            System.out.println("\nNenhuma consulta encontrada para cancelar.");
            return;
        }

        for (AgendamentosEntity a : todos) {
            System.out.println("ID: " + a.getId() + " | Descrição: " + a.getDescricao() +
                    " | Data: " + a.getDataConsulta() + " | Status: " + a.getStatus());
        }

        // ✅ Pergunta de confirmação antes de prosseguir
        System.out.print("\nDeseja realmente cancelar uma consulta? (S/N): ");
        String continuar = sc.nextLine();
        if (!continuar.equalsIgnoreCase("S")) {
            System.out.println("\nVoltando ao menu anterior...");
            return;
        }

        System.out.print("Digite o ID da consulta que deseja cancelar: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Optional<AgendamentosEntity> opt = agendamentosService.getById(id);

        if (opt.isEmpty()) {
            System.out.println("\nConsulta não encontrada.");
            return;
        }

        AgendamentosEntity agendamento = opt.get();

        if (agendamento.getStatus().equalsIgnoreCase("CANCELADO")) {
            System.out.println("\nEsta consulta já está cancelada.");
            return;
        }

        agendamento.setStatus("CANCELADA");
        agendamentosService.update(id, agendamento);

        System.out.println("\n✅ Consulta cancelada com sucesso!");
    }


}

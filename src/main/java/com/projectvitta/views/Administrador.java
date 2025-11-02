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
public class Administrador {

    private final UsuariosService usuariosService;
    private final AgendamentosService agendamentosService;

    public Administrador(UsuariosService usuariosService, AgendamentosService agendamentosService) {
        this.usuariosService = usuariosService;
        this.agendamentosService = agendamentosService;
    }

    public void menuAdmin(Scanner sc, UsuariosEntity admin) {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("         MENU ADMINISTRADOR - VITTA");
            System.out.println("=====================================");
            System.out.println("1 - Cadastrar nova consulta");
            System.out.println("2 - Listar consultas");
            System.out.println("3 - Confirmar consultas solicitas");
            System.out.println("4 - Cancelar consulta");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> cadastrarConsulta(sc, admin);
                case 2 -> listarConsultas();
                case 3 -> confirmarConsulta(sc);
                case 4 -> cancelarConsulta(sc);
                case 0 -> {
                    System.out.println("Saindo do menu administrador...");
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarConsulta(Scanner sc, UsuariosEntity admin) {
        System.out.println("\n---- CADASTRAR CONSULTA ----");

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Data (AAAA-MM-DD): ");
        String dataStr = sc.nextLine();

        System.out.print("Hora (HH:MM): ");
        String horaStr = sc.nextLine();

        System.out.print("Status (AGENDADA / CANCELADA / REALIZADA): ");
        String status = sc.nextLine();

        try {
            LocalDate data = LocalDate.parse(dataStr);
            LocalTime hora = LocalTime.parse(horaStr);

            AgendamentosEntity nova = new AgendamentosEntity();
            nova.setIdUsuario(admin.getId()); // profissional
            nova.setDescricao(descricao);
            nova.setDataConsulta(data);
            nova.setHoraConsulta(hora);
            nova.setStatus(status);

            agendamentosService.create(nova);

            System.out.println("\n✅ Consulta cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao cadastrar consulta. Verifique os dados digitados.");
        }
    }

    private void listarConsultas() {
        List<AgendamentosEntity> todos = agendamentosService.getAll();

        if (todos == null || todos.isEmpty()) {
            System.out.println("\nNenhuma consulta encontrada.");
            return;
        }

        System.out.println("\n===== CONSULTAS =====\n");

        String[] statusList = {"AGENDADA", "REALIZADA", "CANCELADA", "RECUSADA"};

        for (String status : statusList) {
            System.out.println(">>> " + status + " <<<");
            boolean encontrou = false;

            for (AgendamentosEntity a : todos) {
                if (status.equalsIgnoreCase(a.getStatus())) {

                    Optional<UsuariosEntity> usuarioOpt = usuariosService.getById(a.getIdUsuario());
                    String nomeUsuario = usuarioOpt.map(UsuariosEntity::getNome).orElse("Usuário não encontrado");

                    System.out.println("ID: " + a.getId());
                    System.out.println("PACIENTE: " + nomeUsuario);
                    System.out.println("DESCRIÇÃO: " + a.getDescricao());
                    System.out.println("DATA: " + a.getDataConsulta());
                    System.out.println("HORA: " + a.getHoraConsulta());
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

    private void confirmarConsulta(Scanner sc) {
        List<AgendamentosEntity> todas = agendamentosService.getAll();
        List<AgendamentosEntity> solicitadas = new ArrayList<>();

        System.out.println("\n===== CONSULTAS SOLICITADAS =====\n");

        for (AgendamentosEntity a : todas) {
            if ("SOLICITADA".equalsIgnoreCase(a.getStatus())) {
                solicitadas.add(a);

                Optional<UsuariosEntity> usuarioOpt = usuariosService.getById(a.getIdUsuario());
                String nomeUsuario = usuarioOpt.map(UsuariosEntity::getNome).orElse("Usuário não encontrado");

                System.out.println("ID: " + a.getId());
                System.out.println("PACIENTE: " + nomeUsuario);
                System.out.println("DATA: " + a.getDataConsulta());
                System.out.println("HORA: " + a.getHoraConsulta());
                System.out.println("---------------------------");
            }
        }

        if (solicitadas.isEmpty()) {
            System.out.println("\nNenhuma consulta encontrada.");
            return;
        }

        System.out.print("\nDeseja continuar para confirmar/recusar uma consulta? (S/N): ");
        String continuar = sc.nextLine();
        if (!continuar.equalsIgnoreCase("S")) {
            System.out.println("Voltando ao menu anterior...");
            return;
        }

        System.out.print("\nDigite o ID da consulta: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Optional<AgendamentosEntity> opt = agendamentosService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("\nConsulta não encontrada.");
            return;
        }

        AgendamentosEntity consulta = opt.get();
        if (!"SOLICITADA".equalsIgnoreCase(consulta.getStatus())) {
            System.out.println("\nApenas consultas com status 'SOLICITADA' podem ser alteradas.");
            return;
        }

        System.out.print("\nDeseja CONFIRMAR (C) ou RECUSAR (R) esta consulta? ");
        String escolha = sc.nextLine();

        if (escolha.equalsIgnoreCase("C")) {
            consulta.setStatus("AGENDADA");
            agendamentosService.update(id, consulta);
            System.out.println("\n✅ Consulta confirmada!");
        } else if (escolha.equalsIgnoreCase("R")) {
            consulta.setStatus("RECUSADA");
            agendamentosService.update(id, consulta);
            System.out.println("\n❌ Consulta recusada!");
        } else {
            System.out.println("\nOpção inválida. Nenhuma alteração feita.");
        }
    }

    private void cancelarConsulta(Scanner sc) {
        System.out.println("\n---- CANCELAR CONSULTA ----\n");

        List<AgendamentosEntity> todas = agendamentosService.getAll();
        List<AgendamentosEntity> agendadas = new ArrayList<>();

        for (AgendamentosEntity a : todas) {
            if ("AGENDADA".equalsIgnoreCase(a.getStatus())) {
                agendadas.add(a);

                Optional<UsuariosEntity> usuarioOpt = usuariosService.getById(a.getIdUsuario());
                String nomeUsuario = usuarioOpt.map(UsuariosEntity::getNome).orElse("Usuário não encontrado");

                System.out.println("ID: " + a.getId());
                System.out.println("USUÁRIO: " + nomeUsuario);
                System.out.println("DATA: " + a.getDataConsulta());
                System.out.println("HORA: " + a.getHoraConsulta());
                System.out.println("---------------------------");
            }
        }

        if (agendadas.isEmpty()) {
            System.out.println("\nNenhuma consulta agendada para cancelar.");
            return;
        }

        System.out.print("\nDeseja realmente cancelar uma consulta? (S/N): ");
        String continuar = sc.nextLine();
        if (!continuar.equalsIgnoreCase("S")) {
            System.out.println("\nVoltando ao menu anterior...");
            return;
        }

        System.out.print("\nDigite o ID da consulta que deseja cancelar: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Optional<AgendamentosEntity> opt = agendamentosService.getById(id);

        if (opt.isEmpty()) {
            System.out.println("\nConsulta não encontrada.");
            return;
        }

        AgendamentosEntity agendamento = opt.get();

        if (!"AGENDADA".equalsIgnoreCase(agendamento.getStatus())) {
            System.out.println("\nEsta consulta não está mais agendada.");
            return;
        }

        agendamento.setStatus("CANCELADA");
        agendamentosService.update(id, agendamento);

        System.out.println("\n✅ Consulta cancelada com sucesso!");
    }

}

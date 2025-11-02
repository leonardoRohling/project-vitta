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
            System.out.println("1 - Buscar profissional");
            System.out.println("2 - Listar meus agendamentos");
            System.out.println("3 - Cancelar agendamento");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> buscarProfissional(sc, paciente);
                case 2 -> listarAgendamentos(paciente);
                case 3 -> cancelarAgendamento(sc);
                case 4 -> criarAgendamento(sc);
                case 0 -> {
                    System.out.println("Saindo do menu paciente...");
                    return; // volta para menu principal
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // VERIFICAR ISSO SE REALMENTE É LISTAR OS PROFISSIONAIS
    // OU SE É PRA BUSCAR SOMENTE OS DO PACIENTE
    private void buscarProfissional(Scanner sc, UsuariosEntity paciente) {
        List<UsuariosEntity> todos = usuariosService.getAll();

        for (UsuariosEntity u : todos) {
            if (u.getTipoUsuario().equalsIgnoreCase("ADMIN")){

                System.out.println("COD.:" + u.getId());
                System.out.println("NOME: " + u.getNome());
                System.out.println();

            }
        }
    }

    //NÃO FOI TESTADO AINDA
    private void listarAgendamentos(UsuariosEntity paciente) {
        List<AgendamentosEntity> todos = agendamentosService.getByUsuario(paciente.getId());

        if (todos == null || todos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }

        System.out.println("===== AGENDAMENTOS =====\n");

        String[] statusList = {"AGENDADO", "CONFIRMADO", "CANCELADO"};

        for (String status : statusList) {
            System.out.println(">>> " + status + " <<<");
            boolean encontrou = false;

            for (AgendamentosEntity a : todos) {
                if (status.equalsIgnoreCase(a.getStatus())) {
                    System.out.println("DESCRIÇÃO: " + a.getDescricao());
                    System.out.println("DATA:   " + a.getDataConsulta());
                    System.out.println("HORA:   " + a.getHoraConsulta());
                    System.out.println("-------------------------------");
                    encontrou = true;
                }
            }

            if (!encontrou) {
                System.out.println("Nenhum agendamento com status " + status.toLowerCase() + ".");
            }

            System.out.println();
        }
    }


    //Testar
    private void cancelarAgendamento(Scanner sc) {
        System.out.println("\n---- CANCELAR AGENDAMENTO ----");

        List<AgendamentosEntity> todos = agendamentosService.getAll();

        if (todos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado para cancelar.");
            return;
        }

        for (AgendamentosEntity a : todos) {
            System.out.println("ID: " + a.getId() + " | Descrição: " + a.getDescricao() +
                    " | Data: " + a.getDataConsulta() + " | Status: " + a.getStatus());
        }

        System.out.print("\nDigite o ID do agendamento que deseja cancelar: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Optional<AgendamentosEntity> opt = agendamentosService.getById(id);

        if (opt.isEmpty()) {
            System.out.println("Agendamento não encontrado.");
            return;
        }

        AgendamentosEntity agendamento = opt.get();

        if (agendamento.getStatus().equalsIgnoreCase("CANCELADO")) {
            System.out.println("Este agendamento já está cancelado.");
            return;
        }

        agendamento.setStatus("CANCELADO");
        agendamentosService.update(id, agendamento);

        System.out.println("\n✅ Agendamento cancelado com sucesso!");
    }

    //Verificar tabela do banco (tem apenas um id, precisa pro paciente e médico)
    private void criarAgendamento(Scanner sc) {
        //Agendamento...
    }










//    /**
//     * Busca profissionais que começam com o termo fornecido.
//     */
//    public List<UsuariosEntity> buscarProfissionais(String termo) {
//        List<UsuariosEntity> todos = usuariosService.getAll();
//        List<UsuariosEntity> encontrados = new ArrayList<>();
//
//        for (UsuariosEntity u : todos) {
//            if (u.getTipoUsuario().equalsIgnoreCase("ADMIN") && u.getNome().toLowerCase().startsWith(termo.toLowerCase())) {
//                encontrados.add(u);
//            }
//        }
//
//        return encontrados;
//    }
//
//    /**
//     * Retorna horários fixos disponíveis para um profissional (exemplo).
//     */
//    public List<LocalTime> selecionarHorarios(Long idProfissional) {
//        // Aqui você poderia buscar horários reais do banco, por enquanto é fixo
//        List<LocalTime> horarios = new ArrayList<>();
//        horarios.add(LocalTime.of(10, 30));
//        horarios.add(LocalTime.of(15, 0));
//        horarios.add(LocalTime.of(18, 15));
//        return horarios;
//    }
//
//    /**
//     * Agenda a consulta para o paciente.
//     */
//    public AgendamentosEntity agendarConsulta(Long idPaciente, Long idProfissional, LocalDate data, LocalTime hora, String descricao) {
//        AgendamentosEntity agendamento = new AgendamentosEntity();
//        agendamento.setIdUsuario(idPaciente); // paciente
//        agendamento.setDataConsulta(data);
//        agendamento.setHoraConsulta(hora);
//        agendamento.setDescricao(descricao);
//        agendamento.setStatus("AGENDADO");
//
//        return agendamentosService.create(agendamento);
//    }
//
//    /**
//     * Lista todos os agendamentos de um paciente
//     */
//    public List<AgendamentosEntity> listarAgendamentos(Long idPaciente) {
//        return agendamentosService.getByUsuario(idPaciente);
//    }
//
//    /**
//     * Cancela um agendamento pelo ID
//     */
//    public void cancelarAgendamento(Long idAgendamento) {
//        Optional<AgendamentosEntity> agendamentoOpt = agendamentosService.getById(idAgendamento);
//        if (agendamentoOpt.isPresent()) {
//            AgendamentosEntity agendamento = agendamentoOpt.get();
//            agendamento.setStatus("CANCELADO");
//            agendamentosService.update(idAgendamento, agendamento);
//        }
//    }
}

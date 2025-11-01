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
        //Comentário de teste
    }

    private void listarAgendamentos(UsuariosEntity paciente) {
        // implementação de listagem
    }

    private void cancelarAgendamento(Scanner sc) {
        // implementação de cancelamento
    }

    private void criarAgendamento(Scanner sc) {
        // implementação de cancelamento
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

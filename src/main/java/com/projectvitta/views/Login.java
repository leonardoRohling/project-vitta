package com.projectvitta.views;

import com.projectvitta.model.UsuariosEntity;
import com.projectvitta.service.UsuariosService;
import com.projectvitta.service.AgendamentosService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class Login implements CommandLineRunner {

    private final UsuariosService usuariosService;
    private final AgendamentosService agendamentosService;
    private final Paciente telaPaciente;

    public Login(UsuariosService usuariosService, AgendamentosService agendamentosService, Paciente telaPaciente) {
        this.usuariosService = usuariosService;
        this.agendamentosService = agendamentosService;
        this.telaPaciente = telaPaciente;
    }


    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=====================================");
            System.out.println("      SISTEMA DE LOGIN - VITTA");
            System.out.println("=====================================");
            System.out.println("1 - Realizar Login");
            System.out.println("2 - Cadastrar Usuário");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> realizarLogin(sc);
                case 2 -> cadastrarUsuario(sc);
                case 0 -> {
                    System.out.println("\nEncerrando o sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 0);

        sc.close();
    }

    private void realizarLogin(Scanner sc) {
        System.out.println("\n---- LOGIN ----");
        System.out.print("Usuário (login): ");
        String usuario = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        if (usuario.isEmpty() || senha.isEmpty()) {
            System.out.println("Os campos devem ser preenchidos.");
            return;
        }

        Optional<UsuariosEntity> usuarioEncontrado = usuariosService.login(usuario, senha);

        if (usuarioEncontrado.isPresent()) {
            UsuariosEntity u = usuarioEncontrado.get();
            System.out.println("\n✅ Login realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + u.getNome() + " (" + u.getTipoUsuario() + ")");

            if (u.getTipoUsuario().equalsIgnoreCase("PACIENTE")) {
                telaPaciente.menuPaciente(sc, u);
            } else {
                System.out.println("Tela de administrador ainda não implementada.");
            }
        } else {
            System.out.println("\n❌ Usuário ou senha incorretos.");
        }
    }

    private void cadastrarUsuario(Scanner sc) {
        System.out.println("\n---- CADASTRO ----");

        System.out.print("Tipo de usuário (PACIENTE / ADMIN): ");
        String tipoUsuario = sc.nextLine();
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Login (usuário): ");
        String usuario = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        System.out.print("Confirme a senha: ");
        String confirmaSenha = sc.nextLine();

        // 1. Campos obrigatórios
        if (tipoUsuario.isEmpty() || nome.isEmpty() || usuario.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
            System.out.println("Todos os campos devem ser preenchidos.");
            return;
        }

        // 2. Tipo de usuário válido
        if (!tipoUsuario.equalsIgnoreCase("PACIENTE") && !tipoUsuario.equalsIgnoreCase("ADMIN")) {
            System.out.println("Tipo de usuário inválido. Escolha PACIENTE ou ADMIN.");
            return;
        }

        // 3. Senhas iguais
        if (!senha.equals(confirmaSenha)) {
            System.out.println("As senhas não conferem.");
            return;
        }

        // 4. Verificar se login já existe
        Optional<UsuariosEntity> existente = usuariosService.getAll().stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(usuario))
                .findFirst();

        if (existente.isPresent()) {
            System.out.println("Este nome de usuário já está em uso. Escolha outro.");
            return;
        }

        // 5. Criar novo usuário
        UsuariosEntity novoUsuario = new UsuariosEntity();
        novoUsuario.setNome(nome);
        novoUsuario.setLogin(usuario);
        novoUsuario.setSenha(senha);
        novoUsuario.setTipoUsuario(tipoUsuario.equalsIgnoreCase("PACIENTE") ? "PACIENTE" : "ADMIN");

        usuariosService.create(novoUsuario);
        System.out.println("\n✅ Usuário cadastrado com sucesso!");
    }
}

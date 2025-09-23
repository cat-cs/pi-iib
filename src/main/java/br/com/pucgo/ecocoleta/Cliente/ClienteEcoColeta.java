package br.com.pucgo.ecocoleta.Cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteEcoColeta {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // localhost
    private static final int SERVER_PORT = 55555;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                exibirMenu();
                System.out.print("Escolha uma opção: ");
                String escolha = scanner.nextLine();

                if (escolha.equals("0")) {
                    System.out.println("Saindo...");
                    break;
                }

                String request = construirRequisicao(escolha, scanner);
                if (request != null) {
                    String response = enviarRequisicao(request);
                    System.out.println("\n--- RESPOSTA DO SERVIDOR ---\n" + formatarResposta(response) + "\n----------------------------\n");
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n*** EcoColeta - Menu Principal ***");
        System.out.println("1. Cadastrar Ponto de Coleta");
        System.out.println("2. Listar Todos os Pontos");
        System.out.println("3. Consultar por Tipo de Resíduo");
        System.out.println("4. Editar Ponto de Coleta");
        System.out.println("5. Excluir Ponto de Coleta");
        System.out.println("0. Sair");
    }

    private static String construirRequisicao(String escolha, Scanner scanner) {
        switch (escolha) {
            case "1":
                System.out.print("Endereço: ");
                String endereco = scanner.nextLine();
                System.out.print("Tipos de Resíduos (separados por vírgula): ");
                String tipo = scanner.nextLine();
                System.out.print("Horário de Funcionamento: ");
                String horario = scanner.nextLine();
                return "CADASTRAR;" + endereco + ";" + tipo + ";" + horario;
            case "2":
                return "LISTAR";
            case "3":
                System.out.print("Digite o tipo de resíduo para buscar: ");
                String tipoBusca = scanner.nextLine();
                return "CONSULTAR;" + tipoBusca;
            case "4":
                System.out.print("ID do ponto a editar: ");
                String idEdit = scanner.nextLine();
                System.out.print("Novo Endereço: ");
                String novoEndereco = scanner.nextLine();
                System.out.print("Novos Tipos de Resíduos: ");
                String novoTipo = scanner.nextLine();
                System.out.print("Novo Horário: ");
                String novoHorario = scanner.nextLine();
                return "EDITAR;" + idEdit + ";" + novoEndereco + ";" + novoTipo + ";" + novoHorario;
            case "5":
                System.out.print("ID do ponto a excluir: ");
                String idDel = scanner.nextLine();
                return "EXCLUIR;" + idDel;
            default:
                return null;
        }
    }

    private static String enviarRequisicao(String request) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println(request);
            return in.readLine();
        } catch (UnknownHostException e) {
            return "ERRO;Servidor não encontrado: " + e.getMessage();
        } catch (IOException e) {
            return "ERRO;Não foi possível conectar ao servidor. Ele está online?";
        }
    }

    private static String formatarResposta(String response) {
        if (response == null) return "Servidor não respondeu.";
        // O servidor usa "||" como separador de linhas para evitar problemas com \n
        return response.replace("||", "\n");
    }
}
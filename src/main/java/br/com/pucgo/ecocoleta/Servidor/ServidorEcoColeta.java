package br.com.pucgo.ecocoleta.Servidor;


import java.io.*;
import java.net.*;


public class ServidorEcoColeta {

    public static void main(String[] args) {
        int porta = 55555;
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor EcoColeta aguardando conexões na porta " + porta + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Bloqueia até um cliente conectar
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Cria uma nova Thread para cada cliente, permitindo múltiplas conexões
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Recebido de " + socket.getInetAddress() + ": " + request);
                String response = processaRequisicao(request);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Comunicação com o cliente falhou: " + e.getMessage());
        }
    }

    // Metodo central que processa os comandos do protocolo
    private static synchronized String processaRequisicao(String requisicao) {

        String[] partesRequisicao = requisicao.split(";", -1);
        String comando = partesRequisicao[0].toUpperCase();

        try {
            // Direciona para o método específico de cada comando
            return switch (comando) {
                case "CADASTRAR" -> Service.processarCadastro(partesRequisicao);
                case "LISTAR" -> Service.processarListagem();
                case "CONSULTAR" -> Service.processarConsulta(partesRequisicao);
                case "EDITAR" -> Service.processarEdicao(partesRequisicao);
                case "EXCLUIR" -> Service.processarExclusao(partesRequisicao);
                default -> "ERRO;" + "Comando desconhecido: " + comando;
            };
        } catch (NumberFormatException e) {
            return "ERRO;" + "ID inválido. Deve ser um número.";
        } catch (Exception e) {
            return "ERRO;" + "Ocorreu um erro inesperado no servidor.";
        }
    }
}
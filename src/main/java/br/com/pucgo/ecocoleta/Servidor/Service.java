package br.com.pucgo.ecocoleta.Servidor;

import br.com.pucgo.ecocoleta.Entidade.PontoColeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Service {

    private static final AtomicInteger contadorId = new AtomicInteger(0);
    private static final Map<Integer, PontoColeta> pontosDeColeta = new ConcurrentHashMap<>();

    static String processarCadastro(String[] partes) {
        // Valida se tem todas as informações necessárias
        if (!Validador.validarParametrosCadastro(partes)) {
            return "ERRO;" + " Formato: CADASTRAR;endereco;tipo;horario";
        }

        // Cria novo ponto de coleta
        int novoId = contadorId.incrementAndGet();
        String endereco = partes[1];
        String tipoResiduo = partes[2];
        String horarioFuncionamento = partes[3];

        PontoColeta novoPonto = new PontoColeta(novoId, endereco, tipoResiduo, horarioFuncionamento);
        pontosDeColeta.put(novoId, novoPonto);

        return "OK;" + " Ponto de coleta cadastrado com ID: " + novoId;
    }

    /**
     * Processa o comando LISTAR
     * Retorna todos os pontos de coleta cadastrados
     */
    static String processarListagem() {
        if (pontosDeColeta.isEmpty()) {
            return "INFO;" + " Nenhum ponto de coleta cadastrado.";
        }

        StringBuilder listaPontos = new StringBuilder();
        pontosDeColeta.values().forEach(ponto ->
                listaPontos.append(ponto.toString()).append("||")
        );

        return listaPontos.toString();
    }

    /**
     * Processa o comando CONSULTAR
     * Formato esperado: CONSULTAR;tipo_residuo
     */
    static String processarConsulta(String[] partes) {
        // Valida se tem o tipo de resíduo para buscar
        if (!Validador.validarParametrosConsulta(partes)) {
            return "ERRO;" + " Formato: CONSULTAR;tipo_residuo";
        }

        String tipoBuscado = partes[1].toLowerCase().strip();
        StringBuilder resultadoBusca = new StringBuilder();

        // Busca pontos que atendem o tipo de resíduo
        pontosDeColeta.values().stream()
                .filter(ponto -> ponto.getTipoResiduo().toLowerCase().contains(tipoBuscado))
                .forEach(ponto -> resultadoBusca.append(ponto.toString()).append("||"));

        // Retorna resultado ou mensagem informativa
        if (resultadoBusca.isEmpty()) {
            return resultadoBusca.toString();
        } else {
            return "INFO;" + " Nenhum ponto encontrado para o tipo: " + partes[1];
        }
    }

    /**
     * Processa o comando EDITAR
     * Formato esperado: EDITAR;id;endereco;tipo;horario
     */
    static String processarEdicao(String[] partes) {
        // Valida se tem todas as informações necessárias
        if (!Validador.validarParametrosEdicao(partes)) {
            return "ERRO;" + " Formato: EDITAR;id;endereco;tipo;horario";
        }

        int idParaEditar = Integer.parseInt(partes[1]);
        PontoColeta pontoExistente = pontosDeColeta.get(idParaEditar);

        // Verifica se o ponto existe
        if (pontoExistente == null) {
            return "ERRO;" + " Ponto de coleta com ID " + idParaEditar + " não encontrado.";
        }

        // Atualiza as informações do ponto
        pontoExistente.setEndereco(partes[2]);
        pontoExistente.setTipoResiduo(partes[3]);
        pontoExistente.setHorarioFuncionamento(partes[4]);

        return "OK;" + " Ponto de coleta ID " + idParaEditar + " atualizado com sucesso.";
    }

    static String processarExclusao(String[] partes) {
        // Valida se tem o ID para excluir
        if (!Validador.validarParametrosExclusao(partes)) {
            return "ERRO;" + " Formato: EXCLUIR;id";
        }

        int idParaExcluir = Integer.parseInt(partes[1]);

        // Tenta remover o ponto
        if (pontosDeColeta.remove(idParaExcluir) == null) {
            return "ERRO;" + " Ponto de coleta com ID " + idParaExcluir + " não encontrado.";
        }

        return "OK;" + " Ponto de coleta ID " + idParaExcluir + " removido.";
    }
}

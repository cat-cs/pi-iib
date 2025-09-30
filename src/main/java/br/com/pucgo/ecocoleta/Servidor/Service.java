package br.com.pucgo.ecocoleta.Servidor;

import br.com.pucgo.ecocoleta.Entidade.PontoColeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Service {

    private static final AtomicInteger contadorId = new AtomicInteger(0);
    private static final Map<Integer, PontoColeta> pontosDeColeta = new ConcurrentHashMap<>();

    static String processarCadastro(String[] partes) {
   
        if (!Validador.validarParametrosCadastro(partes)) {
            return "ERRO;" + " Formato: CADASTRAR;endereco;tipo;horario";
        }

        int novoId = contadorId.incrementAndGet();
        String endereco = partes[1];
        String tipoResiduo = partes[2];
        String horarioFuncionamento = partes[3];

        PontoColeta novoPonto = new PontoColeta(novoId, endereco, tipoResiduo, horarioFuncionamento);
        pontosDeColeta.put(novoId, novoPonto);

        return "OK;" + " Ponto de coleta cadastrado com ID: " + novoId;
    }

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

    static String processarConsulta(String[] partes) {
        if (!Validador.validarParametrosConsulta(partes)) {
            return "ERRO;" + " Formato: CONSULTAR;tipo_residuo";
        }

        String tipoBuscado = partes[1].toLowerCase().strip();
        StringBuilder resultadoBusca = new StringBuilder();

        pontosDeColeta.values().stream()
                .filter(ponto -> ponto.getTipoResiduo().toLowerCase().contains(tipoBuscado))
                .forEach(ponto -> resultadoBusca.append(ponto.toString()).append("||"));

        if (resultadoBusca.length() > 0) {
            return resultadoBusca.toString();
        } else {
            return "INFO;" + " Nenhum ponto encontrado para o tipo: " + partes[1];
        }
    }

    static String processarEdicao(String[] partes) {
        if (!Validador.validarParametrosEdicao(partes)) {
            return "ERRO;" + " Formato: EDITAR;id;endereco;tipo;horario";
        }

        int idParaEditar = Integer.parseInt(partes[1]);
        PontoColeta pontoExistente = pontosDeColeta.get(idParaEditar);

        if (pontoExistente == null) {
            return "ERRO;" + " Ponto de coleta com ID " + idParaEditar + " não encontrado.";
        }

        pontoExistente.setEndereco(partes[2]);
        pontoExistente.setTipoResiduo(partes[3]);
        pontoExistente.setHorarioFuncionamento(partes[4]);

        return "OK;" + " Ponto de coleta ID " + idParaEditar + " atualizado com sucesso.";
    }

    static String processarExclusao(String[] partes) {
        if (!Validador.validarParametrosExclusao(partes)) {
            return "ERRO;" + " Formato: EXCLUIR;id";
        }

        int idParaExcluir = Integer.parseInt(partes[1]);
        if (pontosDeColeta.remove(idParaExcluir) == null) {
            return "ERRO;" + " Ponto de coleta com ID " + idParaExcluir + " não encontrado.";
        }

        return "OK;" + " Ponto de coleta ID " + idParaExcluir + " removido.";
    }
}


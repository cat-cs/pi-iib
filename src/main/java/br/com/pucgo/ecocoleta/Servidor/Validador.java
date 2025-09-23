package br.com.pucgo.ecocoleta.Servidor;

public class Validador {
    public static boolean validarParametrosCadastro(String[] partes) {
        return partes.length >= 4;
    }

    public static boolean validarParametrosConsulta(String[] partes) {
        return partes.length >= 2;
    }

    public static boolean validarParametrosEdicao(String[] partes) {
        return partes.length >= 5;
    }

    public static boolean validarParametrosExclusao(String[] partes) {
        return partes.length >= 2;
    }
}

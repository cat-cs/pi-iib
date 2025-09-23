package br.com.pucgo.ecocoleta.Entidade;

public class PontoColeta {
    private int id;
    private String endereco;
    private String tipoResiduo;
    private String horarioFuncionamento;

    public PontoColeta(int id, String endereco, String tipoResiduo, String horarioFuncionamento) {
        this.id = id;
        this.endereco = endereco;
        this.tipoResiduo = tipoResiduo;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTipoResiduo() { return tipoResiduo; }
    public void setTipoResiduo(String tipoResiduo) { this.tipoResiduo = tipoResiduo; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }

    @Override
    public String toString() {
        return "ID: " + id + " | Endereço: " + endereco + " | Resíduos: " + tipoResiduo + " | Horário: " + horarioFuncionamento;
    }
}
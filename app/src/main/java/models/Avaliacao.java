package models;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    public Integer id;
    public Docente docente;
    public Turma turma;
    public Integer qtd_discentes;
    public Double postura_profissional;
    public Double postura_profissional_DP;
    public Double atuacao_profissional;
    public Double atuacao_profissional_DP;
    public Double media_aprovados;
    public Double aprovados;

    public Avaliacao() {
    }

    public Avaliacao(Integer id, Docente docente, Turma turma, Integer qtd_discentes, Double postura_profissional, Double postura_profissional_DP, Double atuacao_profissional, Double atuacao_profissional_DP, Double media_aprovados, Double aprovados) {
        this.id = id;
        this.docente = docente;
        this.turma = turma;
        this.qtd_discentes = qtd_discentes;
        this.postura_profissional = postura_profissional;
        this.postura_profissional_DP = postura_profissional_DP;
        this.atuacao_profissional = atuacao_profissional;
        this.atuacao_profissional_DP = atuacao_profissional_DP;
        this.media_aprovados = media_aprovados;
        this.aprovados = aprovados;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Integer getQtd_discentes() {
        return qtd_discentes;
    }

    public void setQtd_discentes(Integer qtd_discentes) {
        this.qtd_discentes = qtd_discentes;
    }

    public Double getPostura_profissional() {
        return postura_profissional;
    }

    public void setPostura_profissional(Double postura_profissional) {
        this.postura_profissional = postura_profissional;
    }

    public Double getPostura_profissional_DP() {
        return postura_profissional_DP;
    }

    public void setPostura_profissional_DP(Double postura_profissional_DP) {
        this.postura_profissional_DP = postura_profissional_DP;
    }

    public Double getAtuacao_profissional() {
        return atuacao_profissional;
    }

    public void setAtuacao_profissional(Double atuacao_profissional) {
        this.atuacao_profissional = atuacao_profissional;
    }

    public Double getAtuacao_profissional_DP() {
        return atuacao_profissional_DP;
    }

    public void setAtuacao_profissional_DP(Double atuacao_profissional_DP) {
        this.atuacao_profissional_DP = atuacao_profissional_DP;
    }

    public Double getMedia_aprovados() {
        return media_aprovados;
    }

    public void setMedia_aprovados(Double media_aprovados) {
        this.media_aprovados = media_aprovados;
    }

    public Double getAprovados() {
        return aprovados;
    }

    public void setAprovados(Double aprovados) {
        this.aprovados = aprovados;
    }
}

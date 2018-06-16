package models;

import java.io.Serializable;

public class Turma implements Serializable{

    public Integer id_turma;
    public String ano;
    public String nivel;
    public String periodo;
    public ComponenteCurricular componente;

    public Turma() {
    }

    public Turma(Integer id_turma, String ano, String nivel, String periodo, ComponenteCurricular componente) {
        this.id_turma = id_turma;
        this.ano = ano;
        this.nivel = nivel;
        this.periodo = periodo;
        this.componente = componente;
    }

    public Integer getId_turma() {
        return id_turma;
    }

    public void setId_turma(Integer id_turma) {
        this.id_turma = id_turma;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public ComponenteCurricular getComponente() {
        return componente;
    }
    public void setComponente(ComponenteCurricular componente) {
        this.componente = componente;
    }
}

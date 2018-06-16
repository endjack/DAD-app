package models;

import java.io.Serializable;
import java.util.List;

public class ComponenteCurricular implements Serializable{

    public Integer id_componente;
    public String codigo;
    public String nome;
    public Unidade unidade;
    public List<Turma> turmas;

    public ComponenteCurricular() {
    }

    public ComponenteCurricular(Integer id_componente, String codigo, String nome, Unidade unidade, List<Turma> turmas) {
        this.id_componente = id_componente;
        this.codigo = codigo;
        this.nome = nome;
        this.unidade = unidade;
        this.turmas = turmas;
    }

    public Integer getId_componente() {
        return id_componente;
    }

    public void setId_componente(Integer id_componente) {
        this.id_componente = id_componente;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    @Override
    public String toString() {
        return nome;
    }
}

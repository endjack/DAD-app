package models;

import java.util.List;

public class ComponenteCurricular {

    public Integer id;
    public String codigo;
    public String nomeComponenteCurricular;
    public Unidade unidade;
    public List<Turma> turmas;

    public ComponenteCurricular() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeComponenteCurricular() {
        return nomeComponenteCurricular;
    }

    public void setNomeComponenteCurricular(String nomeComponenteCurricular) {
        this.nomeComponenteCurricular = nomeComponenteCurricular;
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
}

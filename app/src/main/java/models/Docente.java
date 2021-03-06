package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Docente implements Serializable{

    public Integer id_docente;
    public String nome;
    public String formacao;
    public Unidade unidade;
    public Date data_admissao;

    public Docente() {
    }

    public Docente(Integer id, String nome, String formacao, Unidade unidade, Date data_admissao) {
        this.id_docente = id;
        this.nome = nome;
        this.formacao = formacao;
        this.unidade = unidade;
        this.data_admissao = data_admissao;
    }

    public Integer getId_docente() {
        return id_docente;
    }

    public void setId_docente(Integer id_docente) {
        this.id_docente = id_docente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Date getData_admissao() {
        return data_admissao;
    }

    public void setData_admissao(Date data_admissao) {
        this.data_admissao = data_admissao;
    }


    @Override
    public String toString() {
        return this.nome;
    }
}

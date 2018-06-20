package dto;

import java.io.Serializable;

public class DocenteMediasDTO implements Serializable{

    public Integer id_docente;
    public String nome;
    public Double aprovados;
    public Double media_aprovados;
    public Double atuacao_profissional;
    public Double postura_profissional;

    public DocenteMediasDTO() {
    }

    public DocenteMediasDTO(Integer id_docente, String nome, Double aprovados, Double media_aprovados, Double atuacao_profissional, Double postura_profissional) {
        this.id_docente = id_docente;
        this.nome = nome;
        this.aprovados = aprovados;
        this.media_aprovados = media_aprovados;
        this.atuacao_profissional = atuacao_profissional;
        this.postura_profissional = postura_profissional;
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

    public Double getAprovados() {
        return aprovados;
    }

    public void setAprovados(Double aprovados) {
        this.aprovados = aprovados;
    }

    public Double getMedia_aprovados() {
        return media_aprovados;
    }

    public void setMedia_aprovados(Double media_aprovados) {
        this.media_aprovados = media_aprovados;
    }

    public Double getAtuacao_profissional() {
        return atuacao_profissional;
    }

    public void setAtuacao_profissional(Double atuacao_profissional) {
        this.atuacao_profissional = atuacao_profissional;
    }

    public Double getPostura_profissional() {
        return postura_profissional;
    }

    public void setPostura_profissional(Double postura_profissional) {
        this.postura_profissional = postura_profissional;
    }
}

package models;

import java.util.List;

public class Unidade {

    public Integer id;
    public String lotacao;
    public List<ComponenteCurricular> componentes;
    public List<Docente> docentes;

    public Unidade(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotacao() {
        return lotacao;
    }

    public void setLotacao(String lotacao) {
        this.lotacao = lotacao;
    }

    public List<ComponenteCurricular> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<ComponenteCurricular> componentes) {
        this.componentes = componentes;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }
}

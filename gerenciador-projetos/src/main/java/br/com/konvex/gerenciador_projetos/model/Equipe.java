package br.com.konvex.gerenciador_projetos.model;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public class Equipe {

    private Long id;
    @NotBlank 
    private String lider;
    private List<String> membros;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }

}

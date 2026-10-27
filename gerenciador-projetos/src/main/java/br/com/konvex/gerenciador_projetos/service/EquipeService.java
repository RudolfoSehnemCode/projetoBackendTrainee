package br.com.konvex.gerenciador_projetos.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.konvex.gerenciador_projetos.model.Equipe;

@Service
public class EquipeService {

    private final Map<Long, Equipe> equipes = new LinkedHashMap<>();
    private long proximoId = 1;

    public Equipe criar(Equipe equipe) {
        equipe.setId(proximoId++);
        if (equipe.getMembros() == null) {
            equipe.setMembros(new ArrayList<>());
        }
        equipes.put(equipe.getId(), equipe);
        return equipe;
    }

    public List<Equipe> listar() {
        return new ArrayList<>(equipes.values());
    }

    public Optional<Equipe> buscarPorId(Long id) {
        return Optional.ofNullable(equipes.get(id));
    }
    public Optional<Equipe> atualizar(Long id, Equipe dados) {
    Equipe existente = equipes.get(id);
    if (existente == null) {
        return Optional.empty();
    }
    if (dados.getLider() != null) {
        existente.setLider(dados.getLider());
    }
    if (dados.getMembros() != null) {
        existente.setMembros(dados.getMembros());
    }
    return Optional.of(existente);
}

public boolean remover(Long id) {
    return equipes.remove(id) != null;
}
}
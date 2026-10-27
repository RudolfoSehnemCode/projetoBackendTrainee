package br.com.konvex.gerenciador_projetos.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.konvex.gerenciador_projetos.model.Projeto;

@Service
public class ProjetoService {

    public static final String STATUS_INICIAL = "nao iniciado";
    public static final List<String> STATUS_VALIDOS =
            List.of("nao iniciado", "em andamento", "concluido");

    private final Map<Long, Projeto> projetos = new LinkedHashMap<>();
    private long proximoId = 1;

    public Projeto criar(Projeto projeto) {
        projeto.setId(proximoId++);
        projeto.setStatus(STATUS_INICIAL);
        projetos.put(projeto.getId(), projeto);
        return projeto;
    }

    public List<Projeto> listar() {
        return new ArrayList<>(projetos.values());
    }

    public Optional<Projeto> buscarPorId(Long id) {
        return Optional.ofNullable(projetos.get(id));
    }

    public Optional<Projeto> atualizar(Long id, Projeto dados) {
        Projeto existente = projetos.get(id);
        if (existente == null) {
            return Optional.empty();
        }
        if (dados.getNome() != null) {
            existente.setNome(dados.getNome());
        }
        if (dados.getCliente() != null) {
            existente.setCliente(dados.getCliente());
        }
        if (dados.getDataInicio() != null) {
            existente.setDataInicio(dados.getDataInicio());
        }
        if (dados.getDataEntrega() != null) {
            existente.setDataEntrega(dados.getDataEntrega());
        }
        if (dados.getStatus() != null) {
            existente.setStatus(dados.getStatus());
        }
        if (dados.getIdEquipe() != null) {
            existente.setIdEquipe(dados.getIdEquipe());
        }
        return Optional.of(existente);
    }

    public void desvincularEquipe(Long idEquipe) {
    for (Projeto projeto : projetos.values()) {
        if (idEquipe.equals(projeto.getIdEquipe())) {
            projeto.setIdEquipe(null);
        }
    }
}

    public boolean remover(Long id) {
        return projetos.remove(id) != null;
    }
}
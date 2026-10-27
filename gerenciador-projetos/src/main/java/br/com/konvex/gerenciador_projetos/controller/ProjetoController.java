package br.com.konvex.gerenciador_projetos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.konvex.gerenciador_projetos.model.Projeto;
import br.com.konvex.gerenciador_projetos.service.EquipeService;
import br.com.konvex.gerenciador_projetos.service.ProjetoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final EquipeService equipeService;

    public ProjetoController(ProjetoService projetoService, EquipeService equipeService) {
        this.projetoService = projetoService;
        this.equipeService = equipeService;
    }

    @PostMapping
    public ResponseEntity<Projeto> criar(@Valid @RequestBody Projeto projeto) {
        if (equipeInexistente(projeto.getIdEquipe())) {
            return ResponseEntity.badRequest().build();
        }
        Projeto criado = projetoService.criar(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public List<Projeto> listar() {
        return projetoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscar(@PathVariable Long id) {
        return projetoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projeto> atualizar(@PathVariable Long id, @RequestBody Projeto dados) {
        if (dadosInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }
        return projetoService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (projetoService.remover(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private boolean equipeInexistente(Long idEquipe) {
        return idEquipe != null && equipeService.buscarPorId(idEquipe).isEmpty();
    }

    private boolean dadosInvalidos(Projeto dados) {
        boolean nomeVazio = dados.getNome() != null && dados.getNome().isBlank();
        boolean clienteVazio = dados.getCliente() != null && dados.getCliente().isBlank();
        boolean statusInvalido = dados.getStatus() != null
                && !ProjetoService.STATUS_VALIDOS.contains(dados.getStatus());
        return nomeVazio || clienteVazio || statusInvalido
                || equipeInexistente(dados.getIdEquipe());
    }
}
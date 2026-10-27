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

import br.com.konvex.gerenciador_projetos.model.Equipe;
import br.com.konvex.gerenciador_projetos.service.EquipeService;
import br.com.konvex.gerenciador_projetos.service.ProjetoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService equipeService;
    private final ProjetoService projetoService;

    public EquipeController(EquipeService equipeService, ProjetoService projetoService) {
        this.equipeService = equipeService;
        this.projetoService = projetoService;
    }

    @PostMapping
    public ResponseEntity<Equipe> criar(@Valid @RequestBody Equipe equipe) {
        Equipe criada = equipeService.criar(equipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public List<Equipe> listar() {
        return equipeService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipe> buscar(@PathVariable Long id) {
        return equipeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipe> atualizar(@PathVariable Long id, @RequestBody Equipe dados) {
        if (dados.getLider() != null && dados.getLider().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return equipeService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (equipeService.remover(id)) {
            projetoService.desvincularEquipe(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
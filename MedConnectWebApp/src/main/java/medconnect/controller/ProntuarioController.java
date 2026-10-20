package medconnect.controller;

import jakarta.validation.Valid;
import java.util.List;
import medconnect.model.Prontuario;
import medconnect.service.ProntuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    private final ProntuarioService service;

    public ProntuarioController(ProntuarioService service) { this.service = service; }

    @GetMapping
    public List<Prontuario> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Prontuario buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prontuario criar(@Valid @RequestBody Prontuario obj) { return service.salvar(obj); }

    @PutMapping("/{id}")
    public Prontuario atualizar(@PathVariable Long id, @Valid @RequestBody Prontuario obj) { return service.atualizar(id, obj); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}

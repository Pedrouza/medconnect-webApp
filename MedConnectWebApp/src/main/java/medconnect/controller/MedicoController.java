package medconnect.controller;

import jakarta.validation.Valid;
import java.util.List;
import medconnect.model.Medico;
import medconnect.service.MedicoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) { this.service = service; }

    @GetMapping
    public List<Medico> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Medico buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medico criar(@Valid @RequestBody Medico obj) { return service.salvar(obj); }

    @PutMapping("/{id}")
    public Medico atualizar(@PathVariable Long id, @Valid @RequestBody Medico obj) { return service.atualizar(id, obj); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}

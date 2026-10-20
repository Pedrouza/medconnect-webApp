package medconnect.controller;

import jakarta.validation.Valid;
import java.util.List;
import medconnect.model.Consulta;
import medconnect.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) { this.service = service; }

    @GetMapping
    public List<Consulta> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Consulta buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consulta criar(@Valid @RequestBody Consulta obj) { return service.salvar(obj); }

    @PutMapping("/{id}")
    public Consulta atualizar(@PathVariable Long id, @Valid @RequestBody Consulta obj) { return service.atualizar(id, obj); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}

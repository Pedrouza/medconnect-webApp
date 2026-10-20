package medconnect.controller;

import java.time.LocalDateTime;
import java.util.Map;
import medconnect.repository.ConsultaRepository;
import medconnect.repository.MedicoRepository;
import medconnect.repository.PacienteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final PacienteRepository pacientes;
    private final MedicoRepository medicos;
    private final ConsultaRepository consultas;

    public DashboardController(PacienteRepository pacientes, MedicoRepository medicos, ConsultaRepository consultas) {
        this.pacientes = pacientes;
        this.medicos = medicos;
        this.consultas = consultas;
    }

    /** Totais reais do banco (sem valores fixos). */
    @GetMapping
    public Map<String, Object> resumo() {
        LocalDateTime agora = LocalDateTime.now();
        return Map.of(
                "pacientes", pacientes.count(),
                "medicos", medicos.count(),
                "consultasFuturas", consultas.countByDataHoraAfter(agora),
                "proximas", consultas.findTop5ByDataHoraAfterOrderByDataHoraAsc(agora));
    }
}

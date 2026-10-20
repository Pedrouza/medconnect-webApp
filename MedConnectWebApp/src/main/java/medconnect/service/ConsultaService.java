package medconnect.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import medconnect.exception.RegraNegocioException;
import medconnect.model.Consulta;
import medconnect.repository.ConsultaRepository;
import medconnect.repository.MedicoRepository;
import medconnect.repository.PacienteRepository;
import medconnect.util.AgendaRegras;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService extends CrudService<Consulta> {

    private final ConsultaRepository consultas;
    private final PacienteRepository pacientes;
    private final MedicoRepository medicos;

    public ConsultaService(ConsultaRepository consultas, PacienteRepository pacientes, MedicoRepository medicos) {
        super(consultas);
        this.consultas = consultas;
        this.pacientes = pacientes;
        this.medicos = medicos;
    }

    @Override
    protected void validar(Consulta c) {
        Long idPaciente = c.paciente.getId();
        Long idMedico = c.medico.getId();
        if (idPaciente == null) throw new RegraNegocioException("Paciente é obrigatório");
        if (idMedico == null) throw new RegraNegocioException("Médico é obrigatório");
        c.paciente = pacientes.findById(idPaciente).orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
        c.medico = medicos.findById(idMedico).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));

        if (!c.dataHora.isAfter(LocalDateTime.now()))
            throw new RegraNegocioException("Não é possível agendar consultas no passado");

        List<LocalDateTime> ocupados = consultas.findByMedicoId(idMedico).stream()
                .filter(o -> !Objects.equals(o.getId(), c.getId()))
                .map(o -> o.dataHora).toList();
        if (AgendaRegras.existeConflitoHorario(ocupados, c.dataHora))
            throw new RegraNegocioException("Horário já agendado para este médico");
        c.motivo = c.motivo.trim();
    }
}

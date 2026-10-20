package medconnect.service;

import medconnect.exception.RegraNegocioException;
import medconnect.model.Prontuario;
import medconnect.repository.ConsultaRepository;
import medconnect.repository.ProntuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ProntuarioService extends CrudService<Prontuario> {

    private final ConsultaRepository consultas;

    public ProntuarioService(ProntuarioRepository prontuarios, ConsultaRepository consultas) {
        super(prontuarios);
        this.consultas = consultas;
    }

    @Override
    protected void validar(Prontuario p) {
        Long idConsulta = p.consulta.getId();
        if (idConsulta == null) throw new RegraNegocioException("Consulta é obrigatória");
        p.consulta = consultas.findById(idConsulta).orElseThrow(() -> new RegraNegocioException("Consulta não encontrada"));
    }
}

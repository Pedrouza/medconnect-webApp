package medconnect.service;

import java.time.LocalDate;
import medconnect.exception.RegraNegocioException;
import medconnect.model.Paciente;
import medconnect.repository.PacienteRepository;
import medconnect.util.IdadeUtil;
import medconnect.util.Regras;
import org.springframework.stereotype.Service;

@Service
public class PacienteService extends CrudService<Paciente> {

    private final PacienteRepository pacientes;

    public PacienteService(PacienteRepository pacientes) {
        super(pacientes);
        this.pacientes = pacientes;
    }

    @Override
    protected void validar(Paciente p) {
        if (!Regras.cpfValido(p.cpf)) throw new RegraNegocioException("CPF inválido");
        p.cpf = Regras.formatarCpf(p.cpf);
        // Correção: duplicidade verificada pelo próprio CPF (e não por nome)
        pacientes.findByCpf(p.cpf).filter(o -> !o.getId().equals(p.getId()))
                .ifPresent(o -> { throw new RegraNegocioException("CPF já cadastrado"); });
        if (!Regras.telefoneValido(p.telefone)) throw new RegraNegocioException("Telefone inválido");
        LocalDate hoje = LocalDate.now();
        if (p.nascimento.isAfter(hoje)) throw new RegraNegocioException("Data de nascimento não pode ser no futuro");
        if (IdadeUtil.calcularIdade(p.nascimento, hoje) > 120)
            throw new RegraNegocioException("Data de nascimento inválida (idade superior a 120 anos)");
        p.nome = p.nome.trim();
    }
}

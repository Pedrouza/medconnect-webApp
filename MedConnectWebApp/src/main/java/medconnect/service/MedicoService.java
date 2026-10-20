package medconnect.service;

import medconnect.exception.RegraNegocioException;
import medconnect.model.Medico;
import medconnect.repository.MedicoRepository;
import medconnect.util.Regras;
import org.springframework.stereotype.Service;

@Service
public class MedicoService extends CrudService<Medico> {

    private final MedicoRepository medicos;

    public MedicoService(MedicoRepository medicos) {
        super(medicos);
        this.medicos = medicos;
    }

    @Override
    protected void validar(Medico m) {
        if (!Regras.crmValido(m.crm)) throw new RegraNegocioException("CRM deve ter entre 4 e 7 dígitos");
        m.crm = Regras.digitos(m.crm);
        medicos.findByCrm(m.crm).filter(o -> !o.getId().equals(m.getId()))
                .ifPresent(o -> { throw new RegraNegocioException("CRM já cadastrado"); });
        m.nome = m.nome.trim();
    }
}

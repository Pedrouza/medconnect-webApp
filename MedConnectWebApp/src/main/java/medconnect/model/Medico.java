package medconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Medico extends Entidade {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    public String nome;

    @NotBlank(message = "CRM é obrigatório")
    @Column(nullable = false, unique = true, length = 20)
    public String crm;

    @NotBlank(message = "Especialidade é obrigatória")
    @Column(nullable = false, length = 80)
    public String especialidade;

    @NotBlank(message = "Horário de atendimento é obrigatório")
    @Column(nullable = false, length = 100)
    public String horario;
}

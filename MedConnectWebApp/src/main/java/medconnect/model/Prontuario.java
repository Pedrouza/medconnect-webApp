package medconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Prontuario extends Entidade {

    @NotNull(message = "Consulta é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "consulta_id")
    public Consulta consulta;

    @NotBlank(message = "Observações são obrigatórias")
    @Size(max = 5000, message = "Observações devem ter no máximo 5000 caracteres")
    @Column(nullable = false, length = 5000)
    public String observacoes;

    @Size(max = 2000, message = "Receita deve ter no máximo 2000 caracteres")
    @Column(length = 2000)
    public String receita;
}

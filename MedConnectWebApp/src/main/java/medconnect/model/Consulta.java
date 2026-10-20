package medconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Consulta extends Entidade {

    @NotNull(message = "Paciente é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id")
    public Paciente paciente;

    @NotNull(message = "Médico é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id")
    public Medico medico;

    @NotNull(message = "Data e hora são obrigatórias")
    @Column(nullable = false)
    public LocalDateTime dataHora;

    @NotBlank(message = "Motivo da consulta é obrigatório")
    @Size(max = 500, message = "Motivo deve ter no máximo 500 caracteres")
    @Column(nullable = false, length = 500)
    public String motivo;
}

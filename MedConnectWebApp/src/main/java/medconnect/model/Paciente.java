package medconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import medconnect.util.IdadeUtil;

@Entity
public class Paciente extends Entidade {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    public String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 14)
    public String cpf;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(nullable = false)
    public LocalDate nascimento;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 20)
    public String telefone;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(nullable = false, length = 120)
    public String email;

    @Column(length = 200)
    public String endereco;

    /** Calculada (não persistida): usa IdadeUtil. */
    public Integer getIdade() {
        return nascimento == null ? null : IdadeUtil.calcularIdade(nascimento, LocalDate.now());
    }
}

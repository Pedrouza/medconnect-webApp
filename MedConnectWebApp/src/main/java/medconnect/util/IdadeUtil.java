package medconnect.util;

import java.time.LocalDate;
import java.time.Period;

/** Cálculo de idade (reaproveitado da etapa de testes). */
public final class IdadeUtil {

    public static final int IDADE_MAIORIDADE = 18;

    private IdadeUtil() {}

    public static int calcularIdade(LocalDate nascimento, LocalDate referencia) {
        if (nascimento == null || referencia == null) {
            throw new IllegalArgumentException("Datas não podem ser nulas");
        }
        if (nascimento.isAfter(referencia)) {
            throw new IllegalArgumentException("Data de nascimento posterior à data de referência");
        }
        return Period.between(nascimento, referencia).getYears();
    }

    public static boolean isMaiorDeIdade(LocalDate nascimento, LocalDate referencia) {
        return calcularIdade(nascimento, referencia) >= IDADE_MAIORIDADE;
    }
}

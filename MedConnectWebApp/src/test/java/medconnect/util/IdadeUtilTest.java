package medconnect.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IdadeUtil - cálculo de idade")
class IdadeUtilTest {

    private static final LocalDate REF = LocalDate.of(2026, 10, 19);

    @Test
    @DisplayName("Aniversário já ocorrido no ano: idade completa")
    void aniversarioJaOcorrido() {
        assertEquals(30, IdadeUtil.calcularIdade(LocalDate.of(1996, 3, 10), REF));
    }

    @Test
    @DisplayName("Aniversário ainda não ocorrido no ano: idade menos um")
    void aniversarioAindaNaoOcorrido() {
        assertEquals(29, IdadeUtil.calcularIdade(LocalDate.of(1996, 12, 25), REF));
    }

    @Test
    @DisplayName("No dia exato do aniversário a idade já incrementa")
    void diaDoAniversario() {
        assertEquals(30, IdadeUtil.calcularIdade(LocalDate.of(1996, 10, 19), REF));
    }

    @Test
    @DisplayName("Um dia antes do aniversário ainda não incrementa")
    void vesperaDoAniversario() {
        assertEquals(29, IdadeUtil.calcularIdade(LocalDate.of(1996, 10, 20), REF));
    }

    @Test
    @DisplayName("Nascido em 29/02: em 28/02 ainda não completou o ano, em 01/03 já completou")
    void nascidoEmAnoBissexto() {
        LocalDate nasc = LocalDate.of(2024, 2, 29);
        assertEquals(1, IdadeUtil.calcularIdade(nasc, LocalDate.of(2026, 2, 28)));
        assertEquals(2, IdadeUtil.calcularIdade(nasc, LocalDate.of(2026, 3, 1)));
    }

    @Test
    @DisplayName("Recém-nascido no mesmo dia tem 0 anos")
    void mesmoDia() {
        assertEquals(0, IdadeUtil.calcularIdade(REF, REF));
    }

    @Test
    @DisplayName("Nascimento posterior à referência lança exceção")
    void nascimentoNoFuturo() {
        assertThrows(IllegalArgumentException.class,
                () -> IdadeUtil.calcularIdade(REF.plusDays(1), REF));
    }

    @Test
    @DisplayName("Datas nulas lançam exceção")
    void datasNulas() {
        assertThrows(IllegalArgumentException.class, () -> IdadeUtil.calcularIdade(null, REF));
        assertThrows(IllegalArgumentException.class, () -> IdadeUtil.calcularIdade(REF, null));
    }

    @Test
    @DisplayName("Maioridade: um dia antes dos 18 é menor; aos 18 exatos é maior")
    void maioridade() {
        assertFalse(IdadeUtil.isMaiorDeIdade(REF.minusYears(18).plusDays(1), REF));
        assertTrue(IdadeUtil.isMaiorDeIdade(REF.minusYears(18), REF));
    }
}

package medconnect.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Regras - CPF, telefone e CRM")
class RegrasTest {

    @ParameterizedTest
    @ValueSource(strings = {"529.982.247-25", "52998224725", "390.533.447-05"})
    @DisplayName("CPFs válidos (com e sem máscara)")
    void cpfValido(String cpf) { assertTrue(Regras.cpfValido(cpf)); }

    @ParameterizedTest
    @ValueSource(strings = {"111.111.111-11", "000.000.000-00", "529.982.247-26", "123.456.789", "", "abc"})
    @DisplayName("CPFs inválidos")
    void cpfInvalido(String cpf) { assertFalse(Regras.cpfValido(cpf)); }

    @Test
    void cpfNulo() { assertFalse(Regras.cpfValido(null)); }

    @Test
    @DisplayName("Formatação do CPF")
    void formatar() { assertEquals("529.982.247-25", Regras.formatarCpf("52998224725")); }

    @ParameterizedTest
    @ValueSource(strings = {"(13) 3333-4444", "13 99999-8888", "1333334444"})
    void telefoneValido(String t) { assertTrue(Regras.telefoneValido(t)); }

    @ParameterizedTest
    @ValueSource(strings = {"99999-888", "(13) 99999-88889", ""})
    void telefoneInvalido(String t) { assertFalse(Regras.telefoneValido(t)); }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "1234567", "CRM/SP 123456"})
    void crmValido(String c) { assertTrue(Regras.crmValido(c)); }

    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678", "ABC"})
    void crmInvalido(String c) { assertFalse(Regras.crmValido(c)); }
}

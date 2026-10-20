package medconnect.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AgendaRegras - conflito de horário")
class AgendaRegrasTest {

    private static final LocalDateTime T = LocalDateTime.of(2027, 1, 10, 9, 0);

    @Test
    @DisplayName("Lista vazia: sem conflito")
    void vazia() { assertFalse(AgendaRegras.existeConflitoHorario(List.of(), T)); }

    @Test
    @DisplayName("Mesmo horário: conflito")
    void mesmo() { assertTrue(AgendaRegras.existeConflitoHorario(List.of(T), T)); }

    @Test
    @DisplayName("Horário diferente (1 minuto depois): sem conflito")
    void diferente() { assertFalse(AgendaRegras.existeConflitoHorario(List.of(T), T.plusMinutes(1))); }

    @Test
    @DisplayName("Conflito em qualquer posição da lista")
    void naLista() { assertTrue(AgendaRegras.existeConflitoHorario(List.of(T.minusHours(1), T.plusHours(1), T), T)); }

    @Test
    @DisplayName("Entradas nulas não geram conflito")
    void nulos() {
        assertFalse(AgendaRegras.existeConflitoHorario(null, T));
        assertFalse(AgendaRegras.existeConflitoHorario(List.of(T), null));
    }
}

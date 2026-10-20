package medconnect.util;

import java.time.LocalDateTime;
import java.util.List;

/** Regra de agenda (extraída na etapa de testes), agora sobre LocalDateTime. */
public final class AgendaRegras {

    private AgendaRegras() {}

    /** True se o novo horário coincide com algum horário já ocupado pelo médico. */
    public static boolean existeConflitoHorario(List<LocalDateTime> ocupados, LocalDateTime novo) {
        if (ocupados == null || novo == null) return false;
        return ocupados.stream().anyMatch(novo::equals);
    }
}

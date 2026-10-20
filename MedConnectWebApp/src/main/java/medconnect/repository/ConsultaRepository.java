package medconnect.repository;

import java.time.LocalDateTime;
import java.util.List;
import medconnect.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedicoId(Long medicoId);
    long countByDataHoraAfter(LocalDateTime data);
    List<Consulta> findTop5ByDataHoraAfterOrderByDataHoraAsc(LocalDateTime data);
}

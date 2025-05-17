package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Disponibilidad;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {
    
    List<Disponibilidad> findByTutorId(Integer tutorId);
    
    @Query("SELECT d FROM Disponibilidad d WHERE d.tutor.id = :tutorId AND d.horaInicial >= :fechaActual")
    List<Disponibilidad> findAvailableByTutorIdAndFutureDate(Integer tutorId, LocalDateTime fechaActual);
    
    @Query("SELECT d FROM Disponibilidad d WHERE d.id = :disponibilidadId AND NOT EXISTS " +
            "(SELECT s FROM Sesion s WHERE " +
            "((s.horaInicial < d.horaFinal AND s.horaFinal > d.horaInicial)))")
    Disponibilidad findAvailableById(Integer disponibilidadId);
    
    // Consulta optimizada para verificar si un horario específico está disponible
    @Query("SELECT COUNT(s) = 0 FROM Sesion s WHERE " +
           "s.tutor.id = :tutorId AND " +
           "((s.horaInicial < :horaFinal AND s.horaFinal > :horaInicial))")
    boolean isHorarioDisponible(Integer tutorId, LocalDateTime horaInicial, LocalDateTime horaFinal);
}

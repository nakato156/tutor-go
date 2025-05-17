package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.enums.TipoEstadoSesiones;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    List<Sesion> findByEstudianteIdOrderByFechaDescHoraInicialDesc(Integer estudianteId);

    List<Sesion> findByEstudianteIdAndTipoEstadoOrderByFechaDescHoraInicialDesc(Integer estudianteId, TipoEstadoSesiones estado);
    
    List<Sesion> findByTutorIdAndFecha(Integer tutorId, LocalDateTime fecha);

    List<Sesion> findByTutorIdOrderByFechaDescHoraInicialDesc(Integer tutorId);

    // Método para verificar si existe una sesión en el mismo rango de fechas para un tutor
    boolean existsByTutorIdAndHoraInicialAndHoraFinal(Integer tutorId, LocalDateTime horaInicial, LocalDateTime horaFinal);
    
    // Nuevo método para verificar si ya existe una sesión para un estudiante específico en un horario específico
    boolean existsByEstudianteIdAndTutorIdAndHoraInicialAndHoraFinal(
            Integer estudianteId, 
            Integer tutorId, 
            LocalDateTime horaInicial, 
            LocalDateTime horaFinal
    );
    
    // Consulta para verificar colisiones de horarios
    @Query("SELECT COUNT(s) FROM Sesion s WHERE s.tutor.id = :tutorId " +
           "AND ((s.horaInicial < :horaFinal AND s.horaFinal > :horaInicial))")
    long countSesionesSuperpuestas(Integer tutorId, LocalDateTime horaInicial, LocalDateTime horaFinal);
    
    // Consulta para verificar la existencia de una sesión con exactamente los mismos horarios
    @Query("SELECT COUNT(s) > 0 FROM Sesion s WHERE s.tutor.id = :tutorId " +
           "AND s.horaInicial = :horaInicial AND s.horaFinal = :horaFinal")
    boolean existsExactHorario(Integer tutorId, LocalDateTime horaInicial, LocalDateTime horaFinal);

}

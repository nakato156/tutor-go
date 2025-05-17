package tutorgo.com.tutorgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.enums.TipoEstadoSesiones;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Integer> {

    @Query("SELECT s FROM Sesion s " +
            "LEFT JOIN FETCH s.enlace e " +
            "WHERE s.estudiante.id = :estudianteId " +
            "AND s.tipoEstado = :estado " +
            "AND s.fecha >= CURRENT_DATE " +
            "ORDER BY s.fecha ASC, s.horaInicial ASC")
    List<Sesion> findProximasSesionesConfirmadas(
            @Param("estudianteId") Integer estudianteId,
            @Param("estado") TipoEstadoSesiones estado);

    @Query("SELECT s FROM Sesion s " +
            "LEFT JOIN FETCH s.enlace e " +
            "WHERE s.estudiante.id = :estudianteId " +
            "AND s.fecha >= CURRENT_DATE " +
            "ORDER BY s.fecha ASC, s.horaInicial ASC")
    List<Sesion> findSesionesFuturasByEstudianteId(@Param("estudianteId") Integer estudianteId);

    // ✅ Nuevo método con LocalDateTime
    @Query("SELECT s FROM Sesion s " +
            "LEFT JOIN FETCH s.enlace e " +
            "WHERE s.estudiante.id = :estudianteId " +
            "AND s.fecha >= :fechaDesde " +
            "ORDER BY s.fecha ASC, s.horaInicial ASC")
    List<Sesion> findSesionesDesdeFecha(
            @Param("estudianteId") Integer estudianteId,
            @Param("fechaDesde") LocalDateTime fechaDesde);


    @Query("SELECT s FROM Sesion s " +
            "WHERE s.tipoEstado = 'CONFIRMADO' " +
            "AND s.horaInicial BETWEEN :desde AND :hasta")
    List<Sesion> findSesionesDentroDeRango(@Param("desde") LocalDateTime desde,
                                           @Param("hasta") LocalDateTime hasta);

}

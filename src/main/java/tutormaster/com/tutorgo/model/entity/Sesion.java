package tutormaster.com.tutorgo.model.entity;

import tutormaster.com.tutorgo.model.enums.TipoEstadoSesiones;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sesiones")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "hora_inicial", nullable = false)
    private Timestamp horaInicial;

    @Column(name = "hora_final", nullable = false)
    private Timestamp horaFinal;

    //    ENUMS
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado")
    private TipoEstadoSesiones tipoEstado;

    //    RELACIONES
//    UN TUTOR PUEDE TENER MUCHAS SESIONES
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_tutor"))
    private Tutor tutor;
    //    UN ESTUDIANTE PUEDE TENER MUCHAS SESIONES
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_estudiante"))
    private Estudiante estudiante;

    //    UN ENLACE POR SESION
    @OneToOne(mappedBy = "sesion", cascade = CascadeType.ALL)
    private EnlaceSesion enlace;
}

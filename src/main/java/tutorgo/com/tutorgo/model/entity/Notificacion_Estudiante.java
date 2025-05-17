package tutorgo.com.tutorgo.model.entity;

import tutorgo.com.tutorgo.model.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notificacion_estudiantes")
public class Notificacion_Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    //    ROLES
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacion tipo;

    //    RELACION
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_notificacion_estudiantes"))
    private Estudiante estudiante;
}

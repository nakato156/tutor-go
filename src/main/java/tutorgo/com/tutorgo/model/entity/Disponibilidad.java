package tutorgo.com.tutorgo.model.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "disponibilidades")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha",nullable = false)
    private LocalDateTime fecha;

    @Column(name = "hora_inicial", nullable = false)
    private Timestamp horaInicial;

    @Column(name = "hora_final", nullable = false)
    private Timestamp horaFinal;

    //    RELACION
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_disponibilidad_tutor"))
    private Tutor tutor;
}

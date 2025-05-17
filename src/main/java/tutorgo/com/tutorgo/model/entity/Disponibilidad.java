package tutorgo.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "disponibilidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicial", nullable = false)
    private LocalDateTime horaInicial;

    @Column(name = "hora_final", nullable = false)
    private LocalDateTime horaFinal;

    @ManyToOne
    @JoinColumn(
            name = "tutor_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_disponibilidad_tutor")
    )
    private Tutor tutor;

    @Transient
    private boolean disponible = true;

    public boolean isDisponible() {
        return disponible;
    }
}

package tutorgo.com.tutorgo.model.entity;

import tutorgo.com.tutorgo.model.enums.MetodoPago;
import tutorgo.com.tutorgo.model.enums.TipoEstadoPago;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "comision_plataforma", nullable = false)
    private Double comisionPlataforma;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    //    ENUMS
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado", nullable = false)
    private TipoEstadoPago tipoEstado;

    //    RELACIONES
//    Un estudiante pudo haber hecho muchos pagos
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_pago_estudiantes"))
    private Estudiante estudiante;
    //    Un tutor pudo haber recibido muchos pagos
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_pago_tutores"))
    private Tutor tutor;
}


package tutorgo.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tutorgo.com.tutorgo.model.enums.TipoEstadoSesiones;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sesion_id_seq_gen")
    @SequenceGenerator(name = "sesion_id_seq_gen", sequenceName = "sesion_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "hora_inicial", nullable = false)
    private LocalDateTime horaInicial;

    @Column(name = "hora_final", nullable = false)
    private LocalDateTime horaFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado", nullable = false, length = 20)
    private TipoEstadoSesiones tipoEstado;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_tutor"))
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_estudiante"))
    private Estudiante estudiante;

    @OneToOne(mappedBy = "sesion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EnlaceSesion enlace;
    
    @PrePersist
    protected void onCreate() {
        if (this.tipoEstado == null) {
            this.tipoEstado = TipoEstadoSesiones.PENDIENTE;
        }
        
        if (this.fecha == null && this.horaInicial != null) {
            this.fecha = this.horaInicial; // Asignar fecha basada en hora inicial si está disponible
        } else if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        
        // Asegurarse que las horas no sean nulas
        if (this.horaInicial == null) {
            this.horaInicial = this.fecha;
        }
        
        if (this.horaFinal == null) {
            this.horaFinal = this.horaInicial.plusHours(1);
        }
    }
    
    // Método helper para establecer el enlace manteniendo la coherencia bidireccional
    public void setEnlaceSesion(EnlaceSesion enlaceSesion) {
        this.enlace = enlaceSesion;
        if (enlaceSesion != null && enlaceSesion.getSesion() != this) {
            enlaceSesion.setSesion(this);
        }
    }
}

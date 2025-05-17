package tutorgo.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "centro_estudio", nullable = false, length = 150)
    private String centroEstudio;

    // RELACIÓN UNO A UNO CON USUARIO
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_estudiante_usuarios"))
    private Usuario usuario;

    // RELACIÓN UNO A MUCHOS CON NOTIFICACIONES DEL ESTUDIANTE
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Notificacion_Estudiante> notificaciones;

    // RELACIÓN UNO A MUCHOS CON PAGOS
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Pago> pagosRealizados;

    // RELACIÓN UNO A MUCHOS CON SESIONES (SESIONES RECIBIDAS)
    @OneToMany(mappedBy = "estudiante")
    private List<Sesion> sesionesRecibidas;
}

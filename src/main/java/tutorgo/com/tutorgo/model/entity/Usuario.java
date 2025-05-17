package tutorgo.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "pasword_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "hora_creacion", nullable = false)
    private Timestamp horaCreacion;

    @Column(name = "hora_actualizacion")
    private Timestamp horaActualizacion;

    // RELACIONES
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_usuario_rol"))
    private Rol rol;

    @OneToOne(mappedBy = "usuario")
    private Tutor tutor;

    @OneToOne(mappedBy = "usuario")
    private Estudiante estudiante;
}

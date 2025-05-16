package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "enlaces_sesiones")
public class EnlaceSesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "enlace", nullable = false, length = 150)
    private String enlace;

    //    RELACIONES
    @OneToOne
    @JoinColumn(name = "sesion_id", nullable = false, unique = true, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_enlace_sesion"))
    private Sesion sesion;
}


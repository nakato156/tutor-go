package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "tutores")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tarifa_hora")
    private Integer tarifaHora;

    @Column(name = "rubro", nullable = false)
    private String rubro;

    @Column(name = "bio", columnDefinition = "TEXT", nullable = false)
    private String bio;

    @Column(name = "estrellas_promedio")
    private Float estrellasPromedio;

    //    RELACIONES
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_tutor_usuarios"))
    private Usuario usuario;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Disponibilidad> disponibilidades;

    // UN TUTOR PUEDE TENER MUCHAS SESIONES
    @JsonIgnore
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Sesion> sesionesRealizadas;
}

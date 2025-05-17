package tutorgo.com.tutorgo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "tutores")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tarifa_hora", nullable = false)
    private Integer tarifaHora;

    @Column(name = "rubro", nullable = false, length = 100)
    private String rubro;

    @Column(name = "bio")
    private String bio;

    @Column(name = "estrellas_promedio")
    private Double estrellasPromedio;

    // Relación con Usuario
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Disponibilidad> disponibilidades;

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Sesion> sesionesRealizadas;
}

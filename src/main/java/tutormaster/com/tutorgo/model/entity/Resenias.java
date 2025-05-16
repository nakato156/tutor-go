package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resenas")
public class Resenias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Column(name = "comentario", nullable = false, columnDefinition = "TEXT")
    private String comentario;
    //RELACIONES
    @OneToOne
    @JoinColumn(name = "sesion_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name="FK_resenias_sesion"))
    private Sesion sesion;
}

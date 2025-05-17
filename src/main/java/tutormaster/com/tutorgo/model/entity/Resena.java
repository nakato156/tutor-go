package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resenas")
public class Resena {
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
            foreignKey = @ForeignKey(name = "FK_resenias_sesion"))
    private Sesion sesion;

    // --- GETTERS Y SETTERS ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

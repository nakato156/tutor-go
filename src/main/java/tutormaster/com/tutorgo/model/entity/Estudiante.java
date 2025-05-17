package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "centro_estudio", nullable = false, length = 150)
    private String centroEstudio;

    //    RELACION
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_estudiante_usuarios"))
    private Usuario usuario;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Notificacion_Estudiante> notificaciones;

    @OneToMany(mappedBy = "estudiante" , cascade = CascadeType.ALL)
    private List<Pago> pagosRealizados;

    //    UN ESTUDIANTE PUEDE TENER MUCHAS SESIONES
    @OneToMany(mappedBy = "estudiante")
    private List<Sesion> sesionesRecibidas;

    // --- GETTERS Y SETTERS ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCentroEstudio() {
        return centroEstudio;
    }

    public void setCentroEstudio(String centroEstudio) {
        this.centroEstudio = centroEstudio;
    }

    public List<Pago> getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(List<Pago> pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }
}
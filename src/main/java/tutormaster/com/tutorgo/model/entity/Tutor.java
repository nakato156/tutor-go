package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "tutores")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tarifa_hora")
    private Integer tarifaHora;

    @Column(name = "rubro", nullable = false, length = 150)
    private String rubro;

    @Column(name = "bio", columnDefinition = "TEXT", nullable = false)
    private String bio;

    @Column(name = "estrellas_promedio", nullable = false)
    private Float estrellasPromedio = 0.0f;

    //    RELACIONES
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false,unique = true, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_tutor_usuarios"))
    private Usuario usuario;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Disponibilidad> disponibilidades;

    // UN TUTOR PUEDE TENER MUCHAS SESIONES
    @JsonIgnore
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Sesion> sesionesRealizadas;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagosRecibidos;

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

    public Integer getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(Integer tarifaHora) {
        this.tarifaHora = tarifaHora;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Float getEstrellasPromedio() {
        return estrellasPromedio;
    }

    public void setEstrellasPromedio(Float estrellasPromedio) {
        this.estrellasPromedio = estrellasPromedio;
    }

    public List<Pago> getPagosRecibidos() {
        return pagosRecibidos;
    }

    public void setPagosRecibidos(List<Pago> pagosRecibidos) {
        this.pagosRecibidos = pagosRecibidos;
    }
}

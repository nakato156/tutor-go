package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enlaces_sesiones", uniqueConstraints = {
        @UniqueConstraint(name = "UK_enlace_sesion_sesion_enlace", columnNames = {"sesion_id", "enlace"}), // Combinación de sesion_id y URL debe ser única
        @UniqueConstraint(name = "UK_enlace_sesion_sesion_nombre", columnNames = {"sesion_id", "nombre"})  // Combinación de sesion_id y nombre debe ser única
})
public class EnlaceSesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "enlace", nullable = false, length = 150)
    private String enlace;

    //    RELACIONES
    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_enlace_sesion"))
    private Sesion sesion;

    // --- GETTERS Y SETTERS ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    // Constructores (opcional, pero buena práctica)
    public EnlaceSesion() {
    }

    public EnlaceSesion(String nombre, String enlace, Sesion sesion) {
        this.nombre = nombre;
        this.enlace = enlace;
        this.sesion = sesion;
    }
}


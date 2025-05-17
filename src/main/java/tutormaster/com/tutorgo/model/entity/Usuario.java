package tutormaster.com.tutorgo.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 150)
    private String passwordHash;

    @Column(name = "foto_url", length = 150)
    private String fotoUrl;

    //    RELACIONES
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_usuario_rol"))
    private Rol rol;

    // Relación con Tutor (perfil específico del tutor)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Tutor tutor;

    // Relación con Estudiante (perfil específico del estudiante)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Estudiante estudiante;

    // Sesiones donde este usuario es el tutor
    @OneToMany(mappedBy = "tutorUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sesion> sesionesComoTutor;

    // Sesiones donde este usuario es el estudiante
    @OneToMany(mappedBy = "estudianteUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sesion> sesionesComoEstudiante;

    // --- GETTERS Y SETTERS MANUALES ---
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Rol getRol() { // <--- MÉTODO NECESARIO
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public List<Sesion> getSesionesComoTutor() {
        return sesionesComoTutor;
    }

    public void setSesionesComoTutor(List<Sesion> sesionesComoTutor) {
        this.sesionesComoTutor = sesionesComoTutor;
    }

    public List<Sesion> getSesionesComoEstudiante() {
        return sesionesComoEstudiante;
    }

    public void setSesionesComoEstudiante(List<Sesion> sesionesComoEstudiante) {
        this.sesionesComoEstudiante = sesionesComoEstudiante;
    }
}

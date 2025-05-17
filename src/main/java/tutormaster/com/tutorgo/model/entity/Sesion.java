package tutormaster.com.tutorgo.model.entity;

import tutormaster.com.tutorgo.model.enums.EstadoSesion;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "sesiones")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "hora_inicial", nullable = false)
    private Timestamp horaInicial;

    @Column(name = "hora_final", nullable = false)
    private Timestamp horaFinal;

    //    ENUMS
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado", nullable = false)
    private EstadoSesion tipoEstado;

    //    RELACIONES
//    UN TUTOR PUEDE TENER MUCHAS SESIONES
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_tutor"))
    private Tutor tutor;

    //    UN ESTUDIANTE PUEDE TENER MUCHAS SESIONES
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_sesion_estudiante"))
    private Estudiante estudiante;


    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnlaceSesion> enlaces;

    @OneToOne(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Resena resena;

    // --- GETTERS Y SETTERS ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.estudiante= estudiante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Timestamp getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Timestamp horaInicial) {
        this.horaInicial = horaInicial;
    }

    public Timestamp getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Timestamp horaFinal) {
        this.horaFinal = horaFinal;
    }

    public EstadoSesion getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(EstadoSesion tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public List<EnlaceSesion> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<EnlaceSesion> enlaces) {
        this.enlaces = enlaces;
    }

    public Resena getResena() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }
}

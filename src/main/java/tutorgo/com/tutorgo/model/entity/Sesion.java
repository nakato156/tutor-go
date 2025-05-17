package tutorgo.com.tutorgo.model.entity;

import tutorgo.com.tutorgo.model.enums.EstadoSesion;
import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "sesiones")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // tutor_id en Sesiones es el PK de la tabla 'tutores'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sesion_tutor")) // FK a tutores.id
    private Tutor tutor; // Ahora es de tipo Tutor

    // estudiante_id en Sesiones es el PK de la tabla 'estudiantes'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sesion_estudiante")) // FK a estudiantes.id
    private Estudiante estudiante; // Ahora es de tipo Estudiante

    @Column(nullable = false)
    private Date fecha;

    @Column(name = "hora_inicial", nullable = false)
    private Timestamp horaInicial;

    @Column(name = "hora_final", nullable = false)
    private Timestamp horaFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado", nullable = false, length = 50)
    private EstadoSesion tipoEstado;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EnlaceSesion> enlaces;

    @OneToOne(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Resena resena;

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Tutor getTutor() { return tutor; } // Cambiado
    public void setTutor(Tutor tutor) { this.tutor = tutor; } // Cambiado

    public Estudiante getEstudiante() { return estudiante; } // Cambiado
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; } // Cambiado

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Timestamp getHoraInicial() { return horaInicial; }
    public void setHoraInicial(Timestamp horaInicial) { this.horaInicial = horaInicial; }

    public Timestamp getHoraFinal() { return horaFinal; }
    public void setHoraFinal(Timestamp horaFinal) { this.horaFinal = horaFinal; }

    public EstadoSesion getTipoEstado() { return tipoEstado; }
    public void setTipoEstado(EstadoSesion tipoEstado) { this.tipoEstado = tipoEstado; }

    public List<EnlaceSesion> getEnlaces() { return enlaces; }
    public void setEnlaces(List<EnlaceSesion> enlaces) { this.enlaces = enlaces; }

    public Resena getResena() { return resena; }
    public void setResena(Resena resena) { this.resena = resena; }
}
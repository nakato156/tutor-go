package tutormaster.com.tutorgo.dto.response;


public class ResenaResponseDto {
    private Integer id;
    private Integer sesionId;
    private Integer calificacion;
    private String comentario;

    public ResenaResponseDto() {
    }

    public ResenaResponseDto(Integer id, Integer sesionId, Integer calificacion, String comentario) {
        this.id = id;
        this.sesionId = sesionId;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSesionId() {
        return sesionId;
    }

    public void setSesionId(Integer sesionId) {
        this.sesionId = sesionId;
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

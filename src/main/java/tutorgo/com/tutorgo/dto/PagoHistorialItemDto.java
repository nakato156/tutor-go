package tutorgo.com.tutorgo.dto; // Asegúrate que el paquete sea correcto

import tutorgo.com.tutorgo.model.enums.EstadoPago; // Asegúrate que el paquete del enum sea correcto
// import lombok.AllArgsConstructor; // Eliminado o comentado si no se usa Lombok
// import lombok.Data;             // Eliminado o comentado
// import lombok.NoArgsConstructor; // Eliminado o comentado

import java.math.BigDecimal;
// No hay campo fechaTransaccion aquí, según nuestro Escenario B

public class PagoHistorialItemDto {
    private Integer pagoId;
    // private LocalDateTime fechaTransaccion; // ELIMINADO según Escenario B
    private BigDecimal monto;
    private String descripcion;
    private EstadoPago estado;
    private String tipo;
    private String contraparteNombre;

    // Constructor sin argumentos (necesario para algunas librerías/frameworks)
    public PagoHistorialItemDto() {
    }

    // --- CONSTRUCTOR CON TODOS LOS ARGUMENTOS (el que necesitas) ---
    public PagoHistorialItemDto(Integer pagoId, BigDecimal monto, String descripcion,
                                EstadoPago estado, String tipo, String contraparteNombre) {
        this.pagoId = pagoId;
        this.monto = monto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.tipo = tipo;
        this.contraparteNombre = contraparteNombre;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContraparteNombre() {
        return contraparteNombre;
    }

    public void setContraparteNombre(String contraparteNombre) {
        this.contraparteNombre = contraparteNombre;
    }
}
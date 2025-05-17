package tutorgo.com.tutorgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.dto.request.PagoRequest;
import tutorgo.com.tutorgo.dto.response.HistorialPagosResponse;
import tutorgo.com.tutorgo.dto.response.MessageResponse;
import tutorgo.com.tutorgo.dto.response.PagoResponse;
import tutorgo.com.tutorgo.service.PagoService;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    private final PagoService pagoService;
    
    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
        System.out.println("PagoController inicializado correctamente");
    }
    
    /**
     * Endpoint para obtener el historial de pagos de un usuario
     */
    @GetMapping("/historial/{usuarioId}")
    public ResponseEntity<?> getHistorialPagos(@PathVariable Integer usuarioId) {
        System.out.println("Consultando historial de pagos para usuario ID: " + usuarioId);
        
        try {
            HistorialPagosResponse respuesta = pagoService.getHistorialPagos(usuarioId);
            
            if (!respuesta.getExito()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(respuesta.getMensaje()));
            }
            
            if (respuesta.getTransacciones().isEmpty()) {
                return ResponseEntity.ok()
                        .body(new MessageResponse(respuesta.getMensaje()));
            }
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            System.out.println("Error al consultar historial de pagos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("No se pudo cargar tu historial de pagos. Intenta nuevamente más tarde"));
        }
    }
    
    /**
     * Endpoint alternativo en formato API V1
     */
    @GetMapping("/api/v1/usuarios/{usuarioId}/pagos")
    public ResponseEntity<?> getHistorialPagosApiV1(@PathVariable Integer usuarioId) {
        return getHistorialPagos(usuarioId);
    }

    /**
     * Endpoint para procesar el pago de una tutoría
     */
    /**
     * Endpoint para procesar el pago de una tutoría
     */
    @PostMapping("/tutoria")
    public ResponseEntity<?> procesarPagoTutoria(@RequestBody PagoRequest pagoRequest) {
        System.out.println("Procesando pago para sesión ID: " + pagoRequest.getSesionId());

        try {
            PagoResponse respuesta = pagoService.procesarPagoTutoria(pagoRequest);

            if (!respuesta.getExito()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse(respuesta.getMensaje()));
            }

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            System.out.println("Error al procesar pago: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Ocurrió un error al procesar el pago"));
        }
    }

    /**
     * Endpoint para cancelar un proceso de pago en curso
     */
    @DeleteMapping("/cancelar/{sesionId}")
    public ResponseEntity<?> cancelarPago(@PathVariable Integer sesionId) {
        System.out.println("Cancelando proceso de pago para sesión ID: " + sesionId);

        try {
            PagoResponse respuesta = pagoService.cancelarPago(sesionId);

            if (!respuesta.getExito()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse(respuesta.getMensaje()));
            }

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            System.out.println("Error al cancelar pago: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Ocurrió un error al cancelar el pago"));
        }
    }

}
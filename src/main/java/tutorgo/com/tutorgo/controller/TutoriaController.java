package tutorgo.com.tutorgo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.dto.request.ReservaTutoriaRequest;
import tutorgo.com.tutorgo.dto.response.DisponibilidadResponse;
import tutorgo.com.tutorgo.dto.response.MessageResponse;
import tutorgo.com.tutorgo.dto.response.SesionResponse;
import tutorgo.com.tutorgo.service.TutoriaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tutorias")
@CrossOrigin(origins = "*")
public class TutoriaController {
    
    private final TutoriaService tutoriaService;
    
    @Autowired
    public TutoriaController(TutoriaService tutoriaService) {
        this.tutoriaService = tutoriaService;
        System.out.println("TutoriaController inicializado correctamente");
    }
    
    // Endpoint simple para verificar que el controlador funciona
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        System.out.println("Endpoint de prueba llamado");
        return ResponseEntity.ok(Map.of("mensaje", "El controlador está funcionando correctamente"));
    }
    
    @GetMapping("/disponibilidades/tutor/{tutorId}")
    public ResponseEntity<List<DisponibilidadResponse>> getDisponibilidadesByTutor(@PathVariable Integer tutorId) {
        System.out.println("Buscando disponibilidades para tutor ID: " + tutorId);
        List<DisponibilidadResponse> disponibilidades = tutoriaService.getDisponibilidadesByTutor(tutorId);
        return ResponseEntity.ok(disponibilidades);
    }
    
    @PostMapping("/reservar")
    public ResponseEntity<?> reservarTutoria(@Valid @RequestBody ReservaTutoriaRequest request) {
        System.out.println("Recibida solicitud de reserva: " + request);
        try {
            // Validación adicional
            if (request.getDisponibilidadId() == null || request.getEstudianteId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MessageResponse("La disponibilidad y el estudiante son campos obligatorios")
                );
            }
            
            SesionResponse sesion = tutoriaService.reservarTutoria(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new MessageResponse("Tu solicitud ha sido enviada. El tutor la confirmará pronto.")
            );
        } catch (IllegalArgumentException e) {
                // Manejar específicamente el caso de disponibilidad ocupada o inexistente,
                // respetando los criterios de aceptación
                System.out.println("Error de validación: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MessageResponse(e.getMessage())
                );
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // Loguear el error completo para depuración
                System.out.println("Error de integridad de datos: " + e.getMessage());
                e.printStackTrace();
                
                // Mensaje amigable según los criterios de aceptación
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new MessageResponse("El tutor no tiene disponibilidad en ese horario. Por favor, elige otra opción.")
                );
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new MessageResponse("Ha ocurrido un error al procesar tu solicitud. Por favor, inténtalo más tarde.")
            );
        }
    }
    
    /**
     * Este endpoint se mantiene por compatibilidad con clientes existentes.
     * Se recomienda usar /api/v1/tutorias/reservar para nuevas integraciones.
     */
    @PostMapping("/reservas")
    public ResponseEntity<?> reservarTutoriaAlternativo(@RequestBody ReservaTutoriaRequest request) {
        System.out.println("Recibida solicitud de reserva alternativa: " + request);
        try {
            SesionResponse sesion = tutoriaService.reservarTutoria(request);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Reserva creada exitosamente",
                            "sesionId", sesion.getId()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return new ResponseEntity<>(
                    Map.of("message", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace(); // Para depuración
            return new ResponseEntity<>(
                    Map.of("message", "Ha ocurrido un error al procesar tu solicitud: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<SesionResponse>> getSesionesByEstudiante(@PathVariable Integer estudianteId) {
        System.out.println("Buscando sesiones para estudiante ID: " + estudianteId);
        List<SesionResponse> sesiones = tutoriaService.getSesionesByEstudiante(estudianteId);
        return ResponseEntity.ok(sesiones);
    }
    
    @GetMapping("/estudiante/{estudianteId}/pendientes")
    public ResponseEntity<List<SesionResponse>> getSesionesPendientesByEstudiante(@PathVariable Integer estudianteId) {
        System.out.println("Buscando sesiones pendientes para estudiante ID: " + estudianteId);
        List<SesionResponse> sesiones = tutoriaService.getSesionesPendientesByEstudiante(estudianteId);
        return ResponseEntity.ok(sesiones);
    }
    
    /**
     * Endpoint para API v1 que maneja reservas de tutorías
     */
    @PostMapping("/api/v1/tutorias/reservar")
    public ResponseEntity<?> reservarTutoriaApiV1(@Valid @RequestBody ReservaTutoriaRequest request) {
        System.out.println("Recibida solicitud de reserva API V1: " + request);
        try {
            // Validación adicional
            if (request.getDisponibilidadId() == null || request.getEstudianteId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MessageResponse("La disponibilidad y el estudiante son campos obligatorios")
                );
            }
            
            SesionResponse sesion = tutoriaService.reservarTutoria(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new MessageResponse("Tu solicitud ha sido enviada. El tutor la confirmará pronto.")
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MessageResponse(e.getMessage())
            );
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            System.out.println("Error de integridad de datos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new MessageResponse("El tutor no tiene disponibilidad en ese horario. Por favor, elige otra opción.")
            );
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MessageResponse("No se pudo completar la reserva. Por favor, intenta nuevamente más tarde.")
            );
        }
    }
}

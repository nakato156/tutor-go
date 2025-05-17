package tutorgo.com.tutorgo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.service.AccountService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{estudianteId}/enlace-sesion-activa")
    public ResponseEntity<?> obtenerEnlaceSesionActiva(@PathVariable Integer estudianteId) {
        try {
            String enlace = accountService.obtenerEnlaceSesionActiva(estudianteId);
            //Escenario 1: Ingreso exitoso, retornamos enlace
            return ResponseEntity.ok(enlace);
        } catch (IllegalStateException e) {
            // Aquí puede ser que el enlace no esté disponible
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al acceder a la tutoría. Intenta nuevamente");
        }
    }

    // Nuevo endpoint: sesiones desde 2023
    @GetMapping("/{estudianteId}/sesiones-desde-2023")
    public ResponseEntity<?> obtenerSesionesDesde2023(@PathVariable Integer estudianteId) {
        try {
            return ResponseEntity.ok(accountService.obtenerSesionesDesde2023(estudianteId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudieron obtener las sesiones desde 2023. Error: "  + e.getMessage());
        }
    }

    @GetMapping("/{estudianteId}/notificaciones")
    public ResponseEntity<?> obtenerNotificaciones(@PathVariable Integer estudianteId) {
        try {
            return ResponseEntity.ok(accountService.obtenerNotificaciones(estudianteId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener notificaciones: " + e.getMessage());
        }
    }

}

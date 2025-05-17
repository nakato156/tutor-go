package tutorgo.com.tutorgo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.service.AccountService;
import tutorgo.com.tutorgo.service.SesionService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final SesionService sesionService;  // ⬅️ Agregado

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    @GetMapping("/sesiones/{sesionId}/ingresar")
    public ResponseEntity<?> ingresarASesion(@PathVariable Integer sesionId,
                                             @RequestParam Integer estudianteId) {
        try {
            String enlace = sesionService.obtenerEnlaceSesion(sesionId, estudianteId);
            return ResponseEntity.ok(enlace);
        } catch (RuntimeException e) {
            e.printStackTrace();  // <-- Agrega esto para ver la traza completa
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  // <-- Y esto también
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al acceder a la tutoría. Intenta nuevamente");
        }
    }


    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }


}

package tutorgo.com.tutorgo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.service.AccountService;
import tutorgo.com.tutorgo.service.UsuarioService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UsuarioService usuarioService;

    @Autowired
    public AccountController(AccountService accountService, UsuarioService usuarioService) {
        this.accountService = accountService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Object request) {
        // Adaptar según la lógica real de registro
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro exitoso");
    }

    @DeleteMapping("/perfil")
    public ResponseEntity<String> eliminarCuenta(@RequestParam Integer usuarioId, HttpSession session) {
        try {
            usuarioService.eliminarPerfilUsuario(usuarioId);
            session.invalidate(); // Cierra la sesión actual
            return ResponseEntity.ok("Tu cuenta ha sido eliminada exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();  // Para ver el error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo eliminar tu cuenta." );
        }
    }
}

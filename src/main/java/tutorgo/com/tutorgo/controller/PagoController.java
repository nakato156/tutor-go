package tutorgo.com.tutorgo.controller;

import tutorgo.com.tutorgo.dto.PagoHistorialItemDto;
import tutorgo.com.tutorgo.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/me/historial")
    public ResponseEntity<?> getMiHistorialDePagos(
            @RequestHeader("X-Usuario-Id") Integer usuarioIdAutenticado) { // Simulación
        List<PagoHistorialItemDto> historial = pagoService.getHistorialPagos(usuarioIdAutenticado);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "Aún no tienes transacciones registradas."));
        }
        return ResponseEntity.ok(historial);
    }
}
package tutorgo.com.tutorgo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.model.entity.Disponibilidad;
import tutorgo.com.tutorgo.service.DisponibilidadService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @PostMapping
    public ResponseEntity<String> registrarDisponibilidad(@RequestBody Disponibilidad disponibilidad) {
        try {
            disponibilidadService.guardarDisponibilidad(disponibilidad);
            return ResponseEntity.ok("Disponibilidad agregada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verifica los datos ingresados, no se pudo subir el nuevo turno.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarDisponibilidad(@PathVariable Integer id, @RequestBody Disponibilidad disponibilidad) {
        try {
            disponibilidadService.actualizarDisponibilidad(id, disponibilidad);
            return ResponseEntity.ok("Disponibilidad actualizada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verifica los datos ingresados, no se pudo actualizar el turno.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDisponibilidad(@PathVariable Integer id) {
        disponibilidadService.eliminarDisponibilidad(id);
        return ResponseEntity.ok("Disponibilidad eliminada correctamente.");
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Disponibilidad>> listarDisponibilidadesPorTutor(@PathVariable Integer tutorId) {
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidadesPorTutor(tutorId));
    }
}

package tutorgo.com.tutorgo.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.model.entity.Disponibilidad;
import tutorgo.com.tutorgo.repository.DisponibilidadRepository;
import tutorgo.com.tutorgo.service.DisponibilidadService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;

    @Override
    public void guardarDisponibilidad(Disponibilidad disponibilidad) {
        validarDisponibilidad(disponibilidad);

        // Forzar año 2023 en fecha
        LocalDate fechaOriginal = disponibilidad.getFecha();
        disponibilidad.setFecha(LocalDate.of(2023, fechaOriginal.getMonthValue(), fechaOriginal.getDayOfMonth()));

        // Ajustar horaInicial y horaFinal a año 2023
        disponibilidad.setHoraInicial(convertirATimestampConFecha2023(disponibilidad.getHoraInicial()));
        disponibilidad.setHoraFinal(convertirATimestampConFecha2023(disponibilidad.getHoraFinal()));

        disponibilidadRepository.save(disponibilidad);
    }

    @Override
    public void actualizarDisponibilidad(Integer id, Disponibilidad disponibilidad) {
        validarDisponibilidad(disponibilidad);

        Disponibilidad existente = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con ID: " + id));

        // Forzar año 2023 en fecha
        LocalDate fechaOriginal = disponibilidad.getFecha();
        existente.setFecha(LocalDate.of(2023, fechaOriginal.getMonthValue(), fechaOriginal.getDayOfMonth()));

        existente.setHoraInicial(convertirATimestampConFecha2023(disponibilidad.getHoraInicial()));
        existente.setHoraFinal(convertirATimestampConFecha2023(disponibilidad.getHoraFinal()));
        existente.setTutor(disponibilidad.getTutor());

        disponibilidadRepository.save(existente);
    }

    @Override
    public void eliminarDisponibilidad(Integer id) {
        disponibilidadRepository.deleteById(id);
    }

    @Override
    public List<Disponibilidad> obtenerDisponibilidadesPorTutor(Integer tutorId) {
        return disponibilidadRepository.findByTutorId(tutorId);
    }

    private void validarDisponibilidad(Disponibilidad disponibilidad) {
        if (disponibilidad.getFecha() == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (disponibilidad.getHoraInicial() == null || disponibilidad.getHoraFinal() == null) {
            throw new IllegalArgumentException("Los horarios inicial y final no pueden ser nulos.");
        }
        if (disponibilidad.getHoraFinal().before(disponibilidad.getHoraInicial())) {
            throw new IllegalArgumentException("La hora final debe ser posterior a la hora inicial.");
        }
        if (disponibilidad.getTutor() == null || disponibilidad.getTutor().getId() == null) {
            throw new IllegalArgumentException("El tutor debe estar especificado.");
        }
    }

    private Timestamp convertirATimestampConFecha2023(Timestamp timestamp) {
        LocalDateTime original = timestamp.toLocalDateTime();
        return Timestamp.valueOf(LocalDateTime.of(
                2023,
                original.getMonthValue(),
                original.getDayOfMonth(),
                original.getHour(),
                original.getMinute(),
                original.getSecond(),
                original.getNano()
        ));
    }
}

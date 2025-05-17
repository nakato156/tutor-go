package tutorgo.com.tutorgo.service;

import tutorgo.com.tutorgo.model.entity.Disponibilidad;

import java.util.List;

public interface DisponibilidadService {
    void guardarDisponibilidad(Disponibilidad disponibilidad);
    void actualizarDisponibilidad(Integer id, Disponibilidad disponibilidad);
    void eliminarDisponibilidad(Integer id);
    List<Disponibilidad> obtenerDisponibilidadesPorTutor(Integer tutorId);
}

package tutorgo.com.tutorgo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorgo.com.tutorgo.dto.request.PagoRequest;
import tutorgo.com.tutorgo.dto.response.HistorialPagosResponse;
import tutorgo.com.tutorgo.dto.response.PagoResponse;
import tutorgo.com.tutorgo.dto.response.TransaccionResponse;
import tutorgo.com.tutorgo.model.entity.Estudiante;
import tutorgo.com.tutorgo.model.entity.Pago;
import tutorgo.com.tutorgo.model.entity.Sesion;
import tutorgo.com.tutorgo.model.entity.Tutor;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.model.enums.TipoEstadoPago;
import tutorgo.com.tutorgo.model.enums.TipoEstadoSesiones;
import tutorgo.com.tutorgo.repository.EstudianteRepository;
import tutorgo.com.tutorgo.repository.PagoRepository;
import tutorgo.com.tutorgo.repository.SesionRepository;
import tutorgo.com.tutorgo.repository.TutorRepository;
import tutorgo.com.tutorgo.repository.UserRepository;

@Service
public class PagoService {
    
    private final PagoRepository pagoRepository;
    private final UserRepository userRepository;
    private final EstudianteRepository estudianteRepository;
    private final TutorRepository tutorRepository;
    private final SesionRepository sesionRepository;
    
    @Autowired
    public PagoService(
            PagoRepository pagoRepository,
            UserRepository userRepository,
            EstudianteRepository estudianteRepository,
            TutorRepository tutorRepository,
            SesionRepository sesionRepository) {
        this.pagoRepository = pagoRepository;
        this.userRepository = userRepository;
        this.estudianteRepository = estudianteRepository;
        this.tutorRepository = tutorRepository;
        this.sesionRepository = sesionRepository;
    }
    
    public HistorialPagosResponse getHistorialPagos(Integer usuarioId) {
        try {
            // Verificar que el usuario existe
            Usuario usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            // Determinar si es estudiante o tutor
            Integer estudianteId = null;
            Integer tutorId = null;
            
            Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioId(usuarioId);
            if (estudianteOpt.isPresent()) {
                estudianteId = estudianteOpt.get().getId();
            }
            
            Optional<Tutor> tutorOpt = tutorRepository.findByUsuarioId(usuarioId);
            if (tutorOpt.isPresent()) {
                tutorId = tutorOpt.get().getId();
            }
            
            // Verificar si hay transacciones
            boolean hayTransacciones = false;
            if (estudianteId != null) {
                hayTransacciones = pagoRepository.existsTransaccionesByUsuarioId(estudianteId);
            } else if (tutorId != null) {
                hayTransacciones = pagoRepository.existsTransaccionesByUsuarioId(tutorId);
            }
            
            if (!hayTransacciones) {
                return HistorialPagosResponse.builder()
                        .usuarioId(usuarioId)
                        .transacciones(new ArrayList<>())
                        .exito(true)
                        .mensaje("Aún no tienes transacciones registradas")
                        .build();
            }
            
            // Obtener pagos
            List<Pago> pagos = new ArrayList<>();
            if (estudianteId != null) {
                pagos.addAll(pagoRepository.findPagosByEstudianteId(estudianteId));
            }
            if (tutorId != null) {
                pagos.addAll(pagoRepository.findPagosByTutorId(tutorId));
            }
            
            // Crear una variable final para usar en la lambda
            final boolean esEstudiante = estudianteId != null;
            
            // Mapear a DTOs
            List<TransaccionResponse> transacciones = pagos.stream()
                    .map(pago -> mapToTransaccionResponse(pago, esEstudiante))
                    .collect(Collectors.toList());
            
            return HistorialPagosResponse.builder()
                    .usuarioId(usuarioId)
                    .transacciones(transacciones)
                    .exito(true)
                    .mensaje("Historial de pagos obtenido correctamente")
                    .build();
            
        } catch (IllegalArgumentException e) {
            return HistorialPagosResponse.builder()
                    .usuarioId(usuarioId)
                    .exito(false)
                    .mensaje(e.getMessage())
                    .build();
        } catch (DataAccessException e) {
            return HistorialPagosResponse.builder()
                    .usuarioId(usuarioId)
                    .exito(false)
                    .mensaje("No se pudo cargar tu historial de pagos. Intenta nuevamente más tarde")
                    .build();
        } catch (Exception e) {
            return HistorialPagosResponse.builder()
                    .usuarioId(usuarioId)
                    .exito(false)
                    .mensaje("No se pudo cargar tu historial de pagos. Intenta nuevamente más tarde")
                    .build();
        }
    }
    
    private TransaccionResponse mapToTransaccionResponse(Pago pago, boolean esEstudiante) {
        String tipo = esEstudiante ? "PAGO" : "INGRESO";
        String conceptoReferencia = "Tutoría #" + pago.getId();
        
        // Generar una descripción sin usar la relación sesión
        String descripcion = "Pago por tutoría con " + 
                (esEstudiante ? 
                    pago.getTutor().getUsuario().getNombre() : 
                    pago.getEstudiante().getUsuario().getNombre());
        
        return TransaccionResponse.builder()
                .id(pago.getId())
                .fecha(pago.getFecha())
                .monto(new BigDecimal(pago.getMonto()))
                .descripcion(descripcion)
                .estado(pago.getTipoEstado().toString())
                .tipo(tipo)
                .conceptoReferencia(conceptoReferencia)
                .build();
    }
    
    /**
     * Procesa el pago de una tutoría y actualiza el estado de la sesión
     * 
     * @param pagoRequest Datos del pago a procesar
     * @return Respuesta con resultado del pago
     */
    @Transactional
    public PagoResponse procesarPagoTutoria(PagoRequest pagoRequest) {
        try {
            // 1. Validar que la sesión existe y está en estado pendiente
            Sesion sesion = sesionRepository.findById(pagoRequest.getSesionId())
                    .orElseThrow(() -> new IllegalArgumentException("La sesión no existe"));
            
            if (sesion.getTipoEstado() != TipoEstadoSesiones.PENDIENTE) {
                return PagoResponse.builder()
                        .sesionId(pagoRequest.getSesionId())
                        .exito(false)
                        .mensaje("La sesión no está en estado pendiente y no puede ser pagada")
                        .estadoSesion(sesion.getTipoEstado().toString())
                        .build();
            }
            
            // 2. Validar que el estudiante existe y corresponde a la sesión
            Estudiante estudiante = estudianteRepository.findById(pagoRequest.getEstudianteId())
                    .orElseThrow(() -> new IllegalArgumentException("El estudiante no existe"));
            
            if (!estudiante.getId().equals(sesion.getEstudiante().getId())) {
                return PagoResponse.builder()
                        .sesionId(pagoRequest.getSesionId())
                        .exito(false)
                        .mensaje("El estudiante no corresponde a esta sesión")
                        .estadoSesion(sesion.getTipoEstado().toString())
                        .build();
            }
            
            // 3. Validar los datos de pago (simulación de verificación de tarjeta)
            if (!validarDatosTarjeta(pagoRequest)) {
                return PagoResponse.builder()
                        .sesionId(pagoRequest.getSesionId())
                        .exito(false)
                        .mensaje("Datos incorrectos, el pago fue rechazado")
                        .estadoSesion(TipoEstadoSesiones.PENDIENTE.toString())
                        .build();
            }
            
            // 4. Crear y guardar el pago
            Pago nuevoPago = new Pago();
            nuevoPago.setEstudiante(estudiante);
            nuevoPago.setTutor(sesion.getTutor());
            nuevoPago.setMonto(pagoRequest.getMonto());
            nuevoPago.setFecha(LocalDateTime.now());
            nuevoPago.setTipoEstado(TipoEstadoPago.COMPLETADO);
            double comisionPorcentaje = 0.10; // 10%
            double comision = pagoRequest.getMonto() * comisionPorcentaje;
            nuevoPago.setComisionPlataforma(comision);
            nuevoPago.setMetodoPago(pagoRequest.getMetodoPago());

            Pago pagoGuardado = pagoRepository.save(nuevoPago);


            // 5. Actualizar estado de la sesión a CONFIRMADO
            sesion.setTipoEstado(TipoEstadoSesiones.CONFIRMADO);
            sesionRepository.save(sesion);
            
            // 6. Construir respuesta exitosa
            return PagoResponse.builder()
                    .pagoId(pagoGuardado.getId())
                    .sesionId(sesion.getId())
                    .exito(true)
                    .mensaje("Pago exitoso. Te esperamos en la tutoría")
                    .estadoSesion(TipoEstadoSesiones.CONFIRMADO.toString())
                    .build();
            
        } catch (IllegalArgumentException e) {
            return PagoResponse.builder()
                    .sesionId(pagoRequest.getSesionId())
                    .exito(false)
                    .mensaje(e.getMessage())
                    .estadoSesion(TipoEstadoSesiones.PENDIENTE.toString())
                    .build();
        } catch (Exception e) {
            return PagoResponse.builder()
                    .sesionId(pagoRequest.getSesionId())
                    .exito(false)
                    .mensaje("Ocurrió un error al procesar el pago. Intente nuevamente más tarde.")
                    .estadoSesion(TipoEstadoSesiones.PENDIENTE.toString())
                    .build();
        }
    }
    
    /**
     * Simula la validación de datos de tarjeta
     * En un entorno real, esto se conectaría a un procesador de pagos
     */
    private boolean validarDatosTarjeta(PagoRequest pagoRequest) {
        // Validar formato de tarjeta (16 dígitos)
        if (pagoRequest.getNumeroTarjeta() == null || 
            !pagoRequest.getNumeroTarjeta().matches("\\d{16}")) {
            return false;
        }
        
        // Validar formato de fecha de expiración (MM/YY)
        if (pagoRequest.getFechaExpiracion() == null || 
            !pagoRequest.getFechaExpiracion().matches("\\d{2}/\\d{2}")) {
            return false;
        }
        
        // Validar código de seguridad (3-4 dígitos)
        if (pagoRequest.getCodigoSeguridad() == null || 
            !pagoRequest.getCodigoSeguridad().matches("\\d{3,4}")) {
            return false;
        }
        
        // Simulación: rechazar pagos con tarjetas que terminan en "0000" 
        // (para simular el escenario de error)
        return !pagoRequest.getNumeroTarjeta().endsWith("0000");
    }
    
    /**
     * Método para cancelar un pago en proceso
     */
    public PagoResponse cancelarPago(Integer sesionId) {
        try {
            // Buscar la sesión
            Sesion sesion = sesionRepository.findById(sesionId)
                    .orElseThrow(() -> new IllegalArgumentException("La sesión no existe"));
            
            // Verificar que está en estado pendiente
            if (sesion.getTipoEstado() != TipoEstadoSesiones.PENDIENTE) {
                return PagoResponse.builder()
                        .sesionId(sesionId)
                        .exito(false)
                        .mensaje("No se puede cancelar el pago de una sesión que no está pendiente")
                        .estadoSesion(sesion.getTipoEstado().toString())
                        .build();
            }
            
            // No hay nada que hacer, solo confirmamos que se canceló el proceso
            return PagoResponse.builder()
                    .sesionId(sesionId)
                    .exito(true)
                    .mensaje("Proceso de pago cancelado correctamente")
                    .estadoSesion(TipoEstadoSesiones.PENDIENTE.toString())
                    .build();
            
        } catch (Exception e) {
            return PagoResponse.builder()
                    .sesionId(sesionId)
                    .exito(false)
                    .mensaje("Error al cancelar el pago: " + e.getMessage())
                    .estadoSesion(TipoEstadoSesiones.PENDIENTE.toString())
                    .build();
        }
    }
}

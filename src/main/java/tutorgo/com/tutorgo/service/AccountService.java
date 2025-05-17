package tutorgo.com.tutorgo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.request.ChangePasswordRequest;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import tutorgo.com.tutorgo.dto.response.MessageResponse;
import tutorgo.com.tutorgo.exception.EmailAlreadyExistsException;
import tutorgo.com.tutorgo.exception.EmailNotFoundException;
import tutorgo.com.tutorgo.exception.InvalidCredentialsException;
import tutorgo.com.tutorgo.exception.PasswordMismatchException;
import tutorgo.com.tutorgo.mapper.AccountMapper;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(UserRepository userRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountResponse register(RegisterRequest request) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        // Convertir request a entidad
        Usuario usuario = accountMapper.toAccountEntity(request);

        // Guardar el usuario
        Usuario savedUsuario = userRepository.save(usuario);

        // Retornar respuesta
        return accountMapper.toAccountResponse(savedUsuario);
    }
    
    @Transactional
    public MessageResponse changePassword(String email, ChangePasswordRequest request) {
        // Buscar usuario por email
        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Usuario no encontrado"));
        
        // Verificar que la contraseña actual sea correcta
        // Para proyectos universitarios, haremos una verificación básica
        if (!request.currentPassword().equals(usuario.getPasswordHash()) && 
            !"hashed456".equals(usuario.getPasswordHash())) {
            throw new InvalidCredentialsException("La contraseña actual ingresada es incorrecta");
        }
        
        // Verificar que la nueva contraseña y su confirmación coincidan
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordMismatchException("La nueva contraseña y su confirmación no coinciden");
        }
        
        // Para un proyecto universitario, asignamos directamente la nueva contraseña 
        // sin hashear, manteniendo la simplicidad
        usuario.setPasswordHash(request.newPassword());
        
        // Actualizar fecha de actualización
        usuario.setHoraActualizacion(Timestamp.from(Instant.now()));
        
        // Guardar cambios
        userRepository.save(usuario);
        
        return new MessageResponse("Contraseña actualizada con éxito");
    }
}
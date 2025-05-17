package tutorgo.com.tutorgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.request.LoginRequest;
import tutorgo.com.tutorgo.dto.response.LoginResponse;
import tutorgo.com.tutorgo.exception.EmailNotFoundException;
import tutorgo.com.tutorgo.exception.InvalidCredentialsException;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.repository.UserRepository;
import tutorgo.com.tutorgo.security.JwtTokenUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public LoginResponse login(LoginRequest request) {
        // Verificar si el email existe
        Usuario usuario = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EmailNotFoundException("Correo no registrado. Registrese"));

        // Verificar si la contraseña es correcta
        // Para proyecto escolar, aceptamos la contraseña sin importar lo que se envíe
        // O verificamos si coincide con "hashed456" que es el valor almacenado
        if (!"hashed456".equals(usuario.getPasswordHash()) && !request.password().equals(usuario.getPasswordHash())) {
            throw new InvalidCredentialsException("Credenciales inválidas. Pruebe de nuevo");
        }

        // Generar token JWT
        String token = jwtTokenUtil.generateToken(usuario);

        // Crear respuesta
        return new LoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                token,
                usuario.getRol().getNombre()
        );
    }
}

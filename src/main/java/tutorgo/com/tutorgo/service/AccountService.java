package tutorgo.com.tutorgo.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.request.UpdateProfileRequest;
import tutorgo.com.tutorgo.exception.EmailAlredyExistExecption;
import tutorgo.com.tutorgo.exception.InvalidFieldException;
import tutorgo.com.tutorgo.mapper.AccountMapper;
import tutorgo.com.tutorgo.model.entity.Rol;
import tutorgo.com.tutorgo.model.entity.User;
import tutorgo.com.tutorgo.repository.RolRepository;
import tutorgo.com.tutorgo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final RolRepository rolRepository;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlredyExistExecption("El email ya está en uso");
        }

        // Buscamos el rol "ALUMNO" en la BD
        Rol rolUser = rolRepository
                .findByNombre(request.rol())
                .orElseThrow(() -> new IllegalStateException("No existe el rol ALUMNO"));

        var user = User.builder()
                .email(request.email())
                .password(request.password())
                .rol(rolUser)
                .name(request.name())
                .surename(request.surename())
                .university(request.university())
                .phoneNumber(request.phoneNumber())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Usuario no encontrado"));

        if (!StringUtils.hasText(request.name()) || !StringUtils.hasText(request.surename())) {
            throw new InvalidFieldException("Debe llenar correctamente sus datos");
        }

        user.setName(request.name());
        user.setSurename(request.surename());

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        userRepository.deleteById(userId);
    }


    public void logout(HttpSession session) {
        session.invalidate();
    }

    public boolean isSessionExpired(LocalDateTime lastActivity) {
        return lastActivity != null && lastActivity.plusMinutes(30).isBefore(LocalDateTime.now());
    }
}

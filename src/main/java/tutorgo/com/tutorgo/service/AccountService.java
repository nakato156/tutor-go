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
import tutorgo.com.tutorgo.model.User;
import tutorgo.com.tutorgo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlredyExistExecption("El email ya está en uso");
        }

        var user = User.builder()
                .email(request.email())
                .password(request.password())
                .role(request.role())
                .name(request.name())
                .surename(request.surename())
                .university(request.university())
                .phoneNumber(request.phoneNumber())
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

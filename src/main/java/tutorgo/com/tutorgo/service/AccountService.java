package tutorgo.com.tutorgo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import tutorgo.com.tutorgo.exception.EmailAlreadyExistsException;
import tutorgo.com.tutorgo.mapper.AccountMapper;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.repository.UserRepository;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(UserRepository userRepository, AccountMapper accountMapper) {
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
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
}
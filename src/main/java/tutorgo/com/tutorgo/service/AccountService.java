package tutorgo.com.tutorgo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.exception.EmailAlredyExistExecption;
import tutorgo.com.tutorgo.mapper.AccountMapper;
import tutorgo.com.tutorgo.model.User;
import tutorgo.com.tutorgo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    // private final AccountFinderService accountFinderService;

    @Transactional
    public void register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlredyExistExecption("El email ya está en uso");
        }
        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(request.role())
                .build();
        userRepository.save(user);
    }

}

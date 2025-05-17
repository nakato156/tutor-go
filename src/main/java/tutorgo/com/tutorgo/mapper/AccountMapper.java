package tutorgo.com.tutorgo.mapper;

import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import tutorgo.com.tutorgo.model.User;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toAccountResponse(User user) {
        if (user == null) {
            return null;
        }
        // Mapeo manual de campos
        return new AccountResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getCreatedAt(),
                user.getPhoneNumber()
        );
    }

    public User toAccountEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setPhoneNumber(request.phoneNumber());
        return user;
    }
}

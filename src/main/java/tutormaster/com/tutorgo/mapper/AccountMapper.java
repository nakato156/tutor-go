package tutorgo.com.tutorgo.mapper;

import tutorgo.com.tutorgo.dto.request.CreateAccountRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toAccountResponse(Account account) {
        if (account == null) {
            return null;
        }
        // Mapeo manual de campos
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getEmail(),
                account.getRole() != null ? account.getRole().name() : null,
                account.getCreatedAt(),
                account.getPhoneNumber()
        );
    }

    public Account toAccountEntity(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }

        Account account = new Account();
        account.setPhoneNumber(request.phoneNumber());
        return account;
    }
}

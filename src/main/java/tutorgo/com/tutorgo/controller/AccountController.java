package tutorgo.com.tutorgo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.dto.request.ChangePasswordRequest;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import tutorgo.com.tutorgo.dto.response.MessageResponse;
import tutorgo.com.tutorgo.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        AccountResponse response = accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @RequestParam String email,
            @Valid @RequestBody ChangePasswordRequest request) {
        MessageResponse response = accountService.changePassword(email, request);
        return ResponseEntity.ok(response);
    }
}


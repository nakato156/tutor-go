package tutorgo.com.tutorgo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.service.AccountService;
import tutorgo.com.tutorgo.dto.request.UpdateProfileRequest;


@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final  AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<String> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        accountService.updateProfile(userId, request);
        return ResponseEntity.ok("Perfil actualizado con éxito");
    }

}

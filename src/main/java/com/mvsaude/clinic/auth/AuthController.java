// com.mvsaude.clinic.auth.AuthController
package com.mvsaude.clinic.auth;

import com.mvsaude.clinic.security.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwt;

    public AuthController(JwtService jwt) { this.jwt = jwt; }

    // Somente para testes: gera um token sem validar usuário/senha
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO dto) {
        String role = (dto.role() == null || dto.role().isBlank()) ? "ROLE_USER" : dto.role();
        String token = jwt.generate(dto.username(), role);
        return Map.of("access_token", token, "token_type", "Bearer");
    }

    public record LoginDTO(String username, String password, String role) {}
}

// com.mvsaude.clinic.auth.AuthController
package com.mvsaude.clinic.auth;

import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Usuario;
import com.mvsaude.clinic.repository.UsuarioRepository;
import com.mvsaude.clinic.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwt;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Login real: valida usuário/senha e gera JWT com a role do banco.
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO dto) {

        Usuario usuario = usuarioRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new BusinessException("Usuário ou senha inválidos");
        }

        String token = jwt.generate(usuario.getUsername(), usuario.getRole().name());

        return Map.of("access_token", token, "token_type", "Bearer", "role", usuario.getRole().name());
    }

    public record LoginDTO(String username, String password) {}
}

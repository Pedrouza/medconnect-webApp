package medconnect.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    public record Login(String email, String senha) {}

    @Value("${medconnect.admin.email}")
    private String emailAdmin;

    @Value("${medconnect.admin.senha}")
    private String senhaAdmin;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login dados, HttpSession sessao) {
        if (dados.email() == null || dados.senha() == null
                || !emailAdmin.equalsIgnoreCase(dados.email().trim()) || !senhaAdmin.equals(dados.senha())) {
            return ResponseEntity.status(401).body(Map.of("erro", "E-mail ou senha incorretos"));
        }
        sessao.setAttribute("usuario", emailAdmin);
        return ResponseEntity.ok(Map.of("usuario", emailAdmin));
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession sessao) {
        return Map.of("usuario", String.valueOf(sessao.getAttribute("usuario")));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession sessao) {
        sessao.invalidate();
        return Map.of("status", "ok");
    }
}

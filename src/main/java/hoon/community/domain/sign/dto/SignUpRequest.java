package hoon.community.domain.sign.dto;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String loginId;
    private String password;
    private String username;
    private String email;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder encoder) {
        return new Member(req.loginId, encoder.encode(req.password), req.username, req.email, role);
    }
}

package hoon.community.domain.sign.dto;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "사용자 이름을 입력하세요")
    @Size(min = 2, message = "이름이 너무 짧습니다")
    @Pattern(regexp = "^[A-Za-z]+$", message = "유저이름은 영어만 입력가능합니다")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "사용자 이름을 입력하세요")
    @Size(min = 2, message = "이름이 너무 짧습니다")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "유저이름은 영어만 입력가능합니다")

    private String username;

    @Email(message = "이메일 형식을 맞춰주세요")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;


    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder encoder) {
        return new Member(req.loginId, encoder.encode(req.password), req.username, req.email, role);
    }
}

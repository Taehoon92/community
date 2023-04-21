package hoon.community.domain.sign.dto;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.role.entity.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "Sign up Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @ApiModelProperty(value = "Password", notes = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.", required = true, example = "password1!")
    @NotBlank(message = "Enter a password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.")
    private String password;

    @ApiModelProperty(value = "Username", notes = "Username must be English characters or numbers.", required = true, example = "username")
    @NotBlank(message = "Enter a username")
    @Size(min = 2, message = "Username is too short.")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must be English characters or numbers.")
    private String username;

    @ApiModelProperty(value = "Email", notes = "Enter a email.", required = true, example = "example@email.com")
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Enter a email")
    private String email;


    public static Member toEntity(SignUpRequest req, List<RoleType> roleTypes, PasswordEncoder encoder) {
        return new Member(encoder.encode(req.password), req.username, req.email, roleTypes);
    }

    public static SignInRequest toSignInRequest(SignUpRequest request) {
        return new SignInRequest(request.getEmail(), request.getPassword());
    }
}

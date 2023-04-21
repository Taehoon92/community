package hoon.community.domain.member.dto;

import hoon.community.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@ApiModel(value = "비밀번호 변경 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordModifyDto {

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "Old password", notes = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.", required = true, example = "password1!")
    @NotBlank(message = "Enter old password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.")
    private String oldPassword;

    @ApiModelProperty(value = "New password", notes = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.", required = true, example = "password1!")
    @NotBlank(message = "Enter new password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, must contain letters in mixed case and must contain numbers and special symbols.")
    private String newPassword;


}

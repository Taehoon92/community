package hoon.community.domain.sign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @ApiModelProperty(value = "이메일", notes = "사용자의 이메일을 입력해주세요.", required = true, example = "example@gmail.com")
    @Email(message = "이메일 형식을 맞춰주세요")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @ApiModelProperty(value = "비밀번호", notes = "사용자의 비밀번호를 입력해주세요.", required = true, example = "password1!")
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}

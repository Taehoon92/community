package hoon.community.domain.sign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "Sign in Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @ApiModelProperty(value = "Email", notes = "Enter user's email", required = true, example = "example@email.com")
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Enter a email")
    private String email;

    @ApiModelProperty(value = "Password", notes = "Enter user's password", required = true, example = "password1!")
    @NotBlank(message = "Enter a password")
    private String password;
}

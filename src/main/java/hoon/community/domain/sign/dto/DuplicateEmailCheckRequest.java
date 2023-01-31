package hoon.community.domain.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateEmailCheckRequest {

    @Email(message = "이메일 형식을 맞춰주세요")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;
}


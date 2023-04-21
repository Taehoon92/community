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

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Enter a email")
    private String email;
}


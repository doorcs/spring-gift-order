package gift.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Size(min = 5, message = "이메일 형식을 확인해주세요.") // a@a.a
    String email,

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Length(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[!@#$%^&*()]).+$",
        message = "비밀번호는 특수문자 '!@#$%^&*()'를 하나 이상 포함해야 합니다."
    )
    String password
) {
}

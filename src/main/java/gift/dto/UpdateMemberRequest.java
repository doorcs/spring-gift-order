package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberRequest(

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Size(min = 5, message = "이메일 형식을 확인해주세요.") // a@a.a
    String email
) {
}

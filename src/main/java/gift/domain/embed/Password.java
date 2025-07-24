package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    protected Password() {}

    public Password(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("비밀번호가 입력되지 않았습니다.");
        }
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

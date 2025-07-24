package gift.domain.embed;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    protected Email() {}

    public Email(String email) {
        if (!StringUtils.hasText(email) || !email.matches(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") // RFC 5322 이메일 검증 정규식!
        ) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Email email1 = (Email)o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}

package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class Role {

    @Column(name = "role", nullable = false)
    private String role;

    protected Role() {}

    public Role(String role) {
        if (!StringUtils.hasText(role)) {
            throw new IllegalArgumentException("역할이 입력되지 않았습니다.");
        }

        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

package gift.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class OptionName {

    @Column(name = "option_name", nullable = false)
    private String optionName;

    protected OptionName() {}

    public OptionName(String name) {
        if (!StringUtils.hasText(name)
            || name.length() > 50
            || !name.matches("^[0-9a-zA-Z가-힣()\\s\\[\\]+\\-&/_]*$")) {
            throw new IllegalArgumentException("옵션 이름 형식이 올바르지 않습니다.");
        }

        this.optionName = name;
    }

    public String getOptionName() {
        return optionName;
    }
}

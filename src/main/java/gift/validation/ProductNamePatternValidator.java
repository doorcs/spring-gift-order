package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNamePatternValidator implements ConstraintValidator<ProductNamePattern, String> {

    @Override
    public boolean isValid(String pattern, ConstraintValidatorContext context) {
        return pattern.matches("^[0-9a-zA-Z가-힣()\\s\\[\\]+\\-&/_]*$");
    }
}

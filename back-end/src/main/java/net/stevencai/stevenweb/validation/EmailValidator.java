package net.stevencai.stevenweb.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN="^[_a-zA-Z0-9-+]+(.[_A-Aa-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    private String message;
    private Pattern pattern;
    private Matcher matcher;

    public void initialize(ValidEmail constraint) {
        message = constraint.message();
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

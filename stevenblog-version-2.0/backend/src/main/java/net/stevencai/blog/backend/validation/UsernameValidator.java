package net.stevencai.blog.backend.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private static final String USER_PATTERN="^[a-zA-Z0-9]+[a-zA-Z0-9_]*$";
    private String illegalCharsMessage;
    public void initialize(ValidUsername constraint) {
        illegalCharsMessage = constraint.illegalCharsMessage();
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(username == null || username.isEmpty()){
            return false;
        }
        Pattern pattern  = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(username);
        if(!matcher.matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(illegalCharsMessage)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

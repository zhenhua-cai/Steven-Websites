package net.stevencai.stevenweb.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername,String> {
    private static final String USER_PATTERN="^[a-zA-Z0-9]+$";
    private String illegalCharsMessage;
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        illegalCharsMessage = constraintAnnotation.illegalCharsMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null||value.isEmpty()){
            return true;
        }
        Pattern pattern  = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(value);
        if(!matcher.matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(illegalCharsMessage)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

package net.stevencai.blog.backend.validation;

import org.passay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private int min;
    private int max;
    private List<Rule> rules = new ArrayList<>();

    @Value("${password.allowed.characters}")
    private String allowedChars;
    public void initialize(ValidPassword constraint) {
        min = constraint.min();
        max = constraint.max();
        rules.addAll(Arrays.asList(
                new LengthRule(min, max),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special,1),
                new WhitespaceRule()
        ));
        if(allowedChars != null){
            rules.add(new AllowedCharacterRule(allowedChars.toCharArray()));
        }
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));
        if(result.isValid()){
            return true;
        }
        context.disableDefaultConstraintViolation();
        List<String> msgs = validator.getMessages(result);
        context.buildConstraintViolationWithTemplate(msgs.toString()).addConstraintViolation();
        return false;
    }
}

package net.stevencai.stevenweb.validation;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PropertySource("classpath:password-validation.properties")
@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private int min;
    private int max;
    private List<Rule> rules = new ArrayList<>();

    private String allowedChars;
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    public void initialize(ValidPassword constraint) {
        min = constraint.min();
        max = constraint.max();
        rules.addAll(Arrays.asList(
                new LengthRule(min, max),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special,1),
                new WhitespaceRule(),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5,false)
        ));
        allowedChars = env.getProperty("password.allowed.characters");
        if(allowedChars != null){
            rules.add(new AllowedCharacterRule(allowedChars.toCharArray()));
        }
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }
        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(value));
        if(result.isValid()){
            return true;
        }
        context.disableDefaultConstraintViolation();
        List<String> msgs = validator.getMessages(result);
        for(String msg : msgs){
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
        return false;
    }
}

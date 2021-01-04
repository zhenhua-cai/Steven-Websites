package net.stevencai.blog.backend.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    public void initialize(FieldMatch constraint) {
        firstFieldName= constraint.first();
        secondFieldName = constraint.second();
        message = constraint.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid;
        BeanWrapper beanWrapper =new BeanWrapperImpl(value);
        Object firstStr = beanWrapper .getPropertyValue(firstFieldName);
        Object secondStr =beanWrapper.getPropertyValue(secondFieldName);
        valid = firstStr == null && secondStr == null
                || firstStr != null && firstStr.equals(secondStr);

        if(!valid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

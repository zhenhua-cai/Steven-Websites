package net.stevencai.stevenweb.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default("Invalid Username");
    String illegalCharsMessage() default("Username can only contains digits or letters");
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}

package net.stevencai.blog.backend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {
    String message() default "Invalid Password";
    int min() default 1;
    int max() default 32;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

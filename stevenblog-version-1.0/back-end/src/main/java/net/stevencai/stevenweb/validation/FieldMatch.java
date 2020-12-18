package net.stevencai.stevenweb.validation;

import net.bytebuddy.dynamic.TargetType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String first();
    String second();

    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @interface List{
        FieldMatch[] value();
    }
}

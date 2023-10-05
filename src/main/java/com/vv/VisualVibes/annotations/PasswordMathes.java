package com.vv.VisualVibes.annotations;

import com.vv.VisualVibes.validations.EmailValidator;
import com.vv.VisualVibes.validations.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMathes {
    String message() default  "Password do not patches";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}

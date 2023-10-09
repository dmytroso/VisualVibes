package com.vv.VisualVibes.annotations;

import com.vv.VisualVibes.validations.EmailValidator;
import com.vv.VisualVibes.validations.PasswordMatchesValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMathes {
    //TODO: fix "Password do not patches"
    String message() default  "Password do not patches";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}

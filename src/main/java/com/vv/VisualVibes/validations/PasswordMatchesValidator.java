package com.vv.VisualVibes.validations;

import com.vv.VisualVibes.annotations.PasswordMathes;
import com.vv.VisualVibes.payload.request.SignUpRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMathes, Object> {

    @Override
    public void initialize(PasswordMathes constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        SignUpRequest signUpRequest = (SignUpRequest) value;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }

}

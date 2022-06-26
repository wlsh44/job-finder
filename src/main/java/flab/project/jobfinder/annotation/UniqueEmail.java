package flab.project.jobfinder.annotation;

import flab.project.jobfinder.service.user.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "이미 존재하는 유저입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

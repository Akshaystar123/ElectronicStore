package com.lcwd.electronic.store.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    //Error message
    String message() default "Invalid Image Name !!";

    //Represents group of constraints
    Class<?>[] groups() default {};

    //Information about annotations
    Class<? extends Payload>[] payload() default {};
}

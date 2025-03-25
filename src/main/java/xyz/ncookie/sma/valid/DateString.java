package xyz.ncookie.sma.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateStringValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateString {

    String message() default "올바르지 않은 날짜 형식입니다. (형식: yyyy-MM-dd)";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

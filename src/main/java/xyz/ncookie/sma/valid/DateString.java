package xyz.ncookie.sma.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 검색 조건으로 날짜 데이터가 들어왔을 때 형식이 올바른지 검사하는 어노테이션
 * 지정한 Validator 에서 날짜 데이터가 유효한지 판단한다.
 */
@Documented
@Constraint(validatedBy = DateStringValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateString {

    String message() default "올바르지 않은 날짜 형식입니다. (형식: yyyy-MM-dd)";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

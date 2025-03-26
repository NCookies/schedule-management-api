package xyz.ncookie.sma.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateStringValidator implements ConstraintValidator<DateString, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // modified_date 데이터는 required = false 속성의 파라미터이기 때문에 공백 문자열이 들어오면 통과시킨다.
        if (s == null || s.isBlank()) {
            return true;
        }

        try {
            LocalDate.parse(s, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

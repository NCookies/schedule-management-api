package xyz.ncookie.sma.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직에서 발생할 수 있는 예외들의 부모 클래스
 */
@Getter
public class BusinessException extends RuntimeException {

    // 커스텀 예외 클래스들이 자체적으로 Http 상태 코드를 가지도록 함
    private final HttpStatus httpStatus;

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}

package xyz.ncookie.sma.exception;

import org.springframework.http.HttpStatus;
import xyz.ncookie.sma.dto.ResponseCode;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super(ResponseCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(ResponseCode responseCode) {
        super(responseCode);
    }

    public InvalidPasswordException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}

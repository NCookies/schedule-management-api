package xyz.ncookie.sma.exception;

import org.springframework.http.HttpStatus;

public class NoSuchIdException extends BusinessException {

    public NoSuchIdException() {
        super("유효하지 않은 ID 입니다.", HttpStatus.NOT_FOUND);
    }

    public NoSuchIdException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}

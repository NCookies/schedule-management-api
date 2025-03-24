package xyz.ncookie.sma.exception;

import org.springframework.http.HttpStatus;
import xyz.ncookie.sma.dto.ResponseCode;

// 유효하지 않은 ID를 전달받을 때의 예외
public class NotFoundException extends BusinessException {

    public NotFoundException() {
        super(ResponseCode.NOT_FOUND);
    }

    public NotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}

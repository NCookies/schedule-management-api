package xyz.ncookie.sma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 40x Error
    NOT_FOUND("유효하지 않은 ID 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_USER_ID("존재하지 않는 회원의 ID 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_SCHEDULE_ID("존재하지 않는 일정 ID 입니다.", HttpStatus.NOT_FOUND),

    INVALID_PASSWORD("유효하지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),

    // 50x Error
    INTERNAL_SERVER_ERROR("서버에 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_WHILE_SAVE("DB에 데이터 저장 중 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}

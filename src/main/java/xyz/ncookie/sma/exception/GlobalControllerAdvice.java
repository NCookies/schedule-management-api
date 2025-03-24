package xyz.ncookie.sma.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.ncookie.sma.common.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    // @ExceptionHandler는 더 구체적인 예외 타입이 우선적으로 매칭되므로, 여기에는 미처 핸들링하지 못한 예외들이 들어온다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleUnhandledException(Exception e) {
        // 에러 메세지 + 스택 트레이스
        log.error("핸들링되지 않은 예외 발생!!! : [{}]{}", e.getClass().getSimpleName(), e.getMessage(), e);

        ApiResponse<String> body = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다.",
                e.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("@Valid 요청 파라미터 유효성 검사 실패: {}", e.getMessage());
        ApiResponse<String> body = ApiResponse.error(HttpStatus.BAD_REQUEST, "요청 파라미터 유효성 검사 실패", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 커스텀 @Valid 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("@Valid 커스텀 요청 파라미터 유효성 검사 실패: {}", e.getMessage());
        ApiResponse<String> body = ApiResponse.error(HttpStatus.BAD_REQUEST, "커스텀 요청 파라미터 유효성 검사 실패", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler({ NotFoundException.class, InvalidPasswordException.class })
    public ResponseEntity<ApiResponse<Void>> handleNoSuchIdException(BusinessException e) {
        log.warn("[{}] 예외 발생: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiResponse<Void> body = ApiResponse.error(e.getHttpStatus(), e.getMessage(), null);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(body);
    }

}

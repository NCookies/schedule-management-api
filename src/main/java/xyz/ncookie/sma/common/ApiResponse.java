package xyz.ncookie.sma.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 공용 응답 클래스
@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    // HttpStatus.OK를 제외한 성공 응답
    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return ApiResponse.success(status, status.name(), data);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.success(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

}

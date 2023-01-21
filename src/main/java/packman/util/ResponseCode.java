package packman.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // common
    NULL_VALUE(HttpStatus.BAD_REQUEST, false, "필요한 값이 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, false, "존재하지 않는 자원"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버 내부 오류입니다"),

    // packing_list
    NO_LIST(HttpStatus.BAD_REQUEST, false, "존재하지 않는 패킹리스트입니다"),

    // together_packing_list
    SUCCESS_INVITE_TOGETHER_PACKING(HttpStatus.OK, true, "함께 패킹리스트 초대 성공");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
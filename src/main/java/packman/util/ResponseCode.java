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

    NO_USER(HttpStatus.UNAUTHORIZED, false, "존재하지 않는 유저입니다"),
    EXCEED_LENGTH(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다"),
    UPDATE_LIST_TITLE_SUCCESS(HttpStatus.OK, true, "패킹리스트 제목 수정 성공"),
    UPDATE_LIST_DEPARTURE_DATE_SUCCESS(HttpStatus.OK, true, "패킹리스트 출발날짜 수정 성공"),
    NO_LIST(HttpStatus.NOT_FOUND, false, "존재하지 않는 패킹리스트입니다");



    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
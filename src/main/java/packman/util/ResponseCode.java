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

    // alonePackingList
    SUCCESS_CREATE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 생성 성공"),
    SUCCESS_UPDATE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 수정 성공"),


    // category
    DUPLICATED_CATEGORY(HttpStatus.BAD_REQUEST, false, "중복된 카테고리 명입니다"),
    NO_CATEGORY(HttpStatus.BAD_REQUEST, false, "존재하지 않는 카테고리입니다"),
    NO_LIST_CATEGORY(HttpStatus.BAD_REQUEST, false, "리스트에 존재하지 않는 카테고리입니다"),
    // packingList

    NO_LIST(HttpStatus.BAD_REQUEST, false, "존재하지 않는 패킹리스트입니다"),
    EXCEED_LEN(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
package packman.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // common
    NULL_VALUE(HttpStatus.BAD_REQUEST, false, "필요한 값이 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, false, "존재하지 않는 자원"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버 내부 오류입니다"),
    EXCEED_LENGTH(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다"),
    EXCEED_LEN(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다"),

    // user
    SUCCESS_GET_USER(HttpStatus.OK, true, "유저 조회 성공"),
    NO_USER(HttpStatus.UNAUTHORIZED, false, "존재하지 않는 유저입니다"),
    SUCCESS_DELETE_USER(HttpStatus.OK, true, "유저 탈퇴 성공"),

    // folder
    SUCCESS_GET_ALONE_FOLDERS(HttpStatus.OK, true, "혼자 패킹리스트 폴더 조회 성공"),
    SUCCESS_GET_TOGETHER_FOLDERS(HttpStatus.OK, true, "함께 패킹리스트 폴더 조회 성공"),
    SUCCESS_GET_ALONE_LISTS_IN_FOLDER(HttpStatus.OK, true, "폴더 속 혼자 패킹리스트 조회 성공"),
    SUCCESS_GET_TOGETHER_LISTS_IN_FOLDER(HttpStatus.OK, true, "폴더 속 함께 패킹리스트 조회 성공"),
    NO_USER_FOLDER(HttpStatus.BAD_REQUEST, false, "유저에 존재하지 않는 폴더입니다"),
    NO_FOLDER(HttpStatus.NOT_FOUND, false, "존재하지 않는 폴더입니다"),
    SUCCESS_CREATE_FOLDER(HttpStatus.OK, true, "폴더 생성 성공"),
    FAIL_CREATE_FOLDER(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다"),
    SUCCESS_GET_FOLDERS(HttpStatus.OK, true, "폴더 조회 성공"),

    // category
    DUPLICATED_CATEGORY(HttpStatus.BAD_REQUEST, false, "중복된 카테고리 명입니다"),

    // alonePackingList
    SUCCESS_CREATE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 생성 성공"),
    SUCCESS_CREATE_ALONE_LIST(HttpStatus.OK, true, "혼자 패킹리스트 생성 성공"),

    // packingList
    UPDATE_LIST_TITLE_SUCCESS(HttpStatus.OK, true, "패킹리스트 제목 수정 성공"),
    UPDATE_LIST_DEPARTURE_DATE_SUCCESS(HttpStatus.OK, true, "패킹리스트 출발날짜 수정 성공"),
    NO_LIST(HttpStatus.NOT_FOUND, false, "존재하지 않는 패킹리스트입니다");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
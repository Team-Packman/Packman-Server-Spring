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

    // token
    SUCCESS_KAKAO_LOGIN(HttpStatus.OK, true, "소셜 로그인 토큰 생성"),
    SUCCESS_GET_NEW_TOKEN(HttpStatus.OK, true, "토큰 재발급 성공"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, false, "만료된 토큰입니다"),
    INVALIDE_TOKEN(HttpStatus.UNAUTHORIZED, false, "유효하지 않은 토큰입니다"),
    NO_TOKEN(HttpStatus.UNAUTHORIZED, false, "토큰이 없습니다"),
    NO_USER_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, false, "유저의 리프레쉬 토큰이 아닙니다"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, false, "만료된 리프레쉬 토큰입니다"),
    VALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, false, "유효한 토큰입니다"),
    NO_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, false, "Bearer Token 형식이 아닙니다"),

    // user
    SUCCESS_GET_USER(HttpStatus.OK, true, "유저 조회 성공"),
    NO_USER(HttpStatus.UNAUTHORIZED, false, "존재하지 않는 유저입니다"),
    SUCCESS_DELETE_USER(HttpStatus.OK, true, "유저 탈퇴 성공"),
    CREATE_USER_SUCCESS(HttpStatus.OK, true, "유저 생성 성공"),
    SUCCESS_UPDATE_USER(HttpStatus.OK, true, "유저 수정 성공"),
    NO_MAKER(HttpStatus.BAD_REQUEST, false, "삭제할 권한이 없는 유저입니다"),
    NO_DELETE_MAKER(HttpStatus.BAD_REQUEST, false, "생성자는 삭제할 수 없습니다"),

    // member
    NO_MEMBER_USER(HttpStatus.BAD_REQUEST, false, "멤버에 존재하지 않는 유저입니다"),
    SUCCESS_GET_MEMBER(HttpStatus.OK, true, "멤버 조회 성공"),
    EMPTY_MEMBER(HttpStatus.BAD_REQUEST, false, "멤버가 비어있습니다"),
    DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, false, "이미 추가된 멤버입니다"),
    SUCCESS_ADD_MEMBER(HttpStatus.OK, true, "그룹원 추가 성공"),
    SUCCESS_DELETE_MEMBER(HttpStatus.OK, true, "멤버 삭제 성공"),
    NO_MEMBER(HttpStatus.NOT_FOUND, false, "존재하지 않는 멤버입니다"),

    // group
    NO_GROUP(HttpStatus.NOT_FOUND, false, "존재하지 않는 그룹입니다"),

    // folder
    SUCCESS_GET_ALONE_FOLDERS(HttpStatus.OK, true, "혼자 패킹리스트 폴더 조회 성공"),
    SUCCESS_GET_TOGETHER_FOLDERS(HttpStatus.OK, true, "함께 패킹리스트 폴더 조회 성공"),
    SUCCESS_GET_ALONE_LISTS_IN_FOLDER(HttpStatus.OK, true, "폴더 속 혼자 패킹리스트 조회 성공"),
    SUCCESS_GET_TOGETHER_LISTS_IN_FOLDER(HttpStatus.OK, true, "폴더 속 함께 패킹리스트 조회 성공"),
    NO_USER_FOLDER(HttpStatus.BAD_REQUEST, false, "유저에 존재하지 않는 폴더입니다"),
    NO_FOLDER(HttpStatus.NOT_FOUND, false, "존재하지 않는 폴더입니다"),
    SUCCESS_CREATE_FOLDER(HttpStatus.OK, true, "폴더 생성 성공"),
    SUCCESS_DELETE_FOLDER(HttpStatus.OK, true, "폴더 삭제 성공"),
    FAIL_CREATE_FOLDER(HttpStatus.BAD_REQUEST, false, "제한된 글자수를 초과하였습니다"),
    SUCCESS_UPDATE_FOLDER(HttpStatus.OK, true, "폴더 수정 성공"),
    SUCCESS_GET_FOLDERS(HttpStatus.OK, true, "폴더 조회 성공"),

    // category
    DUPLICATED_CATEGORY(HttpStatus.BAD_REQUEST, false, "중복된 카테고리 명입니다"),
    NO_CATEGORY(HttpStatus.NOT_FOUND, false, "존재하지 않는 카테고리입니다"),
    NO_LIST_CATEGORY(HttpStatus.BAD_REQUEST, false, "리스트에 존재하지 않는 카테고리입니다"),

    // pack
    SUCCESS_CREATE_ALONE_PACK(HttpStatus.OK, true, "혼자 패킹리스트 짐 생성 성공"),
    SUCCESS_UPDATE_ALONE_PACK(HttpStatus.OK, true, "혼자 패킹리스트 짐 수정 성공"),
    SUCCESS_DELETE_ALONE_PACK(HttpStatus.OK, true, "혼자 패킹리스트 짐 삭제 성공"),
    SUCCESS_CREATE_TOGETHER_PACK(HttpStatus.OK, true, "함께 패킹리스트 짐 생성 성공"),
    SUCCESS_UPDATE_TOGETHER_PACK(HttpStatus.OK, true, "함께 패킹리스트 짐 수정 성공"),
    SUCCESS_DELETE_TOGETHER_PACK(HttpStatus.OK, true, "함께 패킹리스트 짐 삭제 성공"),
    NO_PACK(HttpStatus.NOT_FOUND, false, "존재하지 않는 짐입니다"),
    NO_CATEGORY_PACK(HttpStatus.BAD_REQUEST, false, "리스트에 존재하지 않는 짐입니다"),
    NO_PACKER(HttpStatus.BAD_REQUEST, false, "그룹에 존재하지 않는 패커입니다"),

    // alonePackingList
    SUCCESS_CREATE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 생성 성공"),
    SUCCESS_CREATE_ALONE_LIST(HttpStatus.OK, true, "혼자 패킹리스트 생성 성공"),
    SUCCESS_DELETE_ALONE_LIST(HttpStatus.OK, true, "폴더 속 혼자 패킹리스트 삭제 성공"),
    SUCCESS_UPDATE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 수정 성공"),
    SUCCESS_DELETE_ALONE_CATEGORY(HttpStatus.OK, true, "혼자 패킹리스트 카테고리 삭제 성공"),
    SUCCESS_GET_ALONE_LIST(HttpStatus.OK, true, "혼자 패킹리스트 상세조회 성공"),
    SUCCESS_GET_INVITE_ALONE_LIST(HttpStatus.OK, true, "공유된 혼자 패킹리스트 조회 성공"),

    // togetherPackingList
    SUCCESS_CREATE_TOGETHER_CATEGORY(HttpStatus.OK, true, "함께 패킹리스트 카테고리 생성 성공"),
    SUCCESS_INVITE_TOGETHER_PACKING(HttpStatus.OK, true, "함께 패킹리스트 초대 성공"),
    SUCCESS_UPDATE_TOGETHER_CATEGORY(HttpStatus.OK, true, "함께 패킹리스트 카테고리 수정 성공"),
    SUCCESS_DELETE_TOGETHER_CATEGORY(HttpStatus.OK, true, "함께 패킹리스트 카테고리 삭제 성공"),
    SUCCESS_CREATE_TOGETHER_LIST(HttpStatus.OK, true, "함께 패킹리스트 생성 성공"),
    SUCCESS_GET_TOGETHER_LIST(HttpStatus.OK, true, "함께 패킹리스트 상세조회 성공"),
    SUCCESS_UPDATE_PACKER(HttpStatus.OK, true, "함께 패킹리스트 담당자 배정 성공"),
    SUCCESS_DELETE_TOGETHER_LIST(HttpStatus.OK, true, "폴더 속 함께 패킹리스트 삭제 성공"),

    // packingList
    UPDATE_LIST_TITLE_SUCCESS(HttpStatus.OK, true, "패킹리스트 제목 수정 성공"),
    UPDATE_LIST_DEPARTURE_DATE_SUCCESS(HttpStatus.OK, true, "패킹리스트 출발날짜 수정 성공"),
    NO_LIST(HttpStatus.NOT_FOUND, false, "존재하지 않는 패킹리스트입니다"),
    UPDATE_LIST_MY_TEMPLATE_SUCCESS(HttpStatus.OK, true, "패킹리스트 나만의 템플릿 추가/업데이트 성공"),
    NO_FOLDER_LIST(HttpStatus.NOT_FOUND, false, "폴더에 존재하지 않는 패킹리스트입니다"),
    GET_LIST_TITLE_DEPARTURE_DATE_SUCCESS(HttpStatus.OK, true, "패킹리스트 제목, 날짜 조회 성공"),
    GET_RECENT_CREATED_LIST_SUCCESS(HttpStatus.OK, true, "최근 생성된 리스트 조회 성공"),
    NO_EXIST_USER_LIST(HttpStatus.OK, true, "유저가 생성한 리스트가 없습니다"),
    SUCCESS_GET_INVITE_LIST(HttpStatus.OK, true, "공유된 패킹리스트 조회 성공"),
    INVALID_LIST_TYPE(HttpStatus.BAD_REQUEST, false, "유효하지 않은 리스트 타입입니다"),

    // template
    NO_TEMPLATE(HttpStatus.NOT_FOUND, false, "존재하지 않는 템플릿입니다"),
    SUCCESS_GET_ALONE_TEMPLATE_LIST(HttpStatus.OK, true, "혼자 패킹 템플릿 리스트 조회 성공"),
    SUCCESS_GET_TOGETHER_TEMPLATE_LIST(HttpStatus.OK, true, "함께 패킹 템플릿 리스트 조회 성공"),
    SUCCESS_GET_DETAILED_TEMPLATE(HttpStatus.OK, true, "템플릿 상세조회 성공");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
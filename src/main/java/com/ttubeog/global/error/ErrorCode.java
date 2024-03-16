package com.ttubeog.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
    INVALID_PARAMETER(400, null, "잘못된 요청 데이터 입니다."),
    INVALID_REPRESENTATION(400, null, "잘못된 표현 입니다."),
    INVALID_FILE_PATH(400, null, "잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(400, null, "해당 값이 존재하지 않습니다."),
    INVALID_CHECK(400, null, "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(400, null, "잘못된 인증입니다.");
    */

    /**
     * Etc
     */
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "E-001", "잘못된 요청입니다."),

    /**
     * 아바타 관련 오류
     */
    AVATAR_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "A-001", "주어진 식별자로 아바타를 찾을 수 없습니다."),

    /**
     * 유저 관련 오류
     */
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U-001", "주어진 식별자로 유저를 찾을 수 없습니다."),

    /**
     * 매니저 관련 오류
     */
    APPLICATION_IS_PENDING(HttpStatus.BAD_REQUEST, "M-001", "해당 유저는 대표 신청 후 승인 대기중 상태입니다."),
    MANAGER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "M-002", "주어진 유저로 매니저를 찾을 수 없습니다."),
    NOT_MANAGER_OF_STORE(HttpStatus.FORBIDDEN, "M-003", "해당 매장을 관리할 수 있는 권한이 없습니다."),

    /**
     * 상점 관련 오류
     */
    STORE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "주어진 식별자로 스토어를 찾을 수 없습니다."),

    /**
     * 북마크 관련 오류
     */
    BOOKMARK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "B-001", "북마크가 이미 존재합니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "B-002", "취소할 북마크를 찾을 수 없습니다."),

    /**
     * 리뷰 관련 오류
     */
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "R-001", "주어진 식별자로 리뷰를 찾을 수 없습니다."),
    NOT_REVIEW_WRITER(HttpStatus.BAD_REQUEST, "R-002", "해당 리뷰의 작성자가 아닙니다."),

    /**
     * 리뷰 좋아요 관련 오류
     */
    REVIEW_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "RL-001", "좋아요가 이미 존재합니다."),
    REVIEW_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "RL-002", "취소할 좋아요를 찾을 수 없습니다."),

    /**
     * 인증 관련 오류
     */
    UNREGISTERED_PROVIDER(HttpStatus.BAD_REQUEST, "OA-001", "등록되지 않은 프로바이더가 입력되었습니다."),
    EMAIL_IS_REGISTER_WITH_ANOTHER_PROVIDER(HttpStatus.BAD_REQUEST, "OA-002", "같은 이메일이 다른 소셜 로그인 플랫폼으로 가입되어 있습니다."),
    UNDEFINED_PROVIDER(HttpStatus.INTERNAL_SERVER_ERROR, "OA-003", "유저타입에 정의되지 않은 프로바이더가 입력되었습니다."),

    /**
     * 닉네임 관련 오류
     */
    FAIL_TO_FIND_UNIQUE_NICKNAME(HttpStatus.INTERNAL_SERVER_ERROR, "N-001", "유일한 닉네임을 찾는데 실패했습니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "N-002", "이미 사용중인 닉네임 입니다."),

    /**
     * 검색 히스토리 관련 오류
     */
    SEARCH_HISTORY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "H-001", "주어진 식별자로 검색 히스토리를 찾을 수 없습니다."),
    SEARCH_HISTORY_FORBIDDEN(HttpStatus.FORBIDDEN, "H-002", "해당 검색 히스토리에 접근할 수 없습니다."),

    /**
     * 태그 관련 오류
     */
    TAG_NOT_FOUND(HttpStatus.BAD_REQUEST, "T-001", "주어진 식별자로 태그를 찾을 수 없습니다."),
    TAG_UNMATCHED_CATEGORY(HttpStatus.BAD_REQUEST, "T-002", "주어진 태그가 유효한 카테고리에 속하지 않습니다."),
    NOT_REVIEW_CATEGORY(HttpStatus.BAD_REQUEST, "T-003", "입력으로 들어온 태그 카테고리가 'REVIEW'가 아닙니다."),

    /**
     * 파일 업로드 관련 오류
     */
    FAIL_TO_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "F-001", "파일 업로드에 실패했습니다."),
    FAIL_TO_ANALYZE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "F-002", "이미지 파일을 분석하는데 실패했습니다."),
    FAIL_TO_GET_TYPE_OF_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "F-003", "이미지 파일의 타입을 가져오지 못했습니다."),
    NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "F-004", "요청된 파일이 이미지 타입이 아닙니다."),
    IMAGE_FILE_IS_NULL(HttpStatus.BAD_REQUEST, "F-005", "요청된 파일이 null입니다."),

    /**
     * 사진 관련 오류
     */
    PHOTO_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "P-001", "사진은 최대 5개까지 첨부 가능합니다."),

    /**
     * 이벤트 관련 오류
     */
    EVENT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "EV-001", "주어진 식별자로 이벤트를 찾을 수 없습니다."),
    INACTIVE_EVENT_FORBIDDEN(HttpStatus.FORBIDDEN, "EV-002", "종료된 이벤트에 접근할 수 없습니다."),

    /**
     * 제품 관련 오류
     */
    PRODUCT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "P-001", "주어진 이름으로 제품을 찾을 수 없습니다."),
    INVALID_PRICE_UNIT(HttpStatus.BAD_REQUEST, "P-002", "올바르지 않은 가격 단위입니다. 10원 단위로 입력해주세요"),
    INVALID_PRODUCT_NAME_FORMAT(HttpStatus.BAD_REQUEST, "P-003", "제품명은 영문(대소문자), 숫자, 한글로만 구성되어야합니다."),
    INVALID_PRODUCT_NAME_LENGTH(HttpStatus.BAD_REQUEST, "P-004", "제품명의 길이가 유효하지 않습니다."),
    NEGATIVE_PRICE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "P-005", "가격은 0보다 작을 수 없습니다."),

    /**
     *
     */
    STORE_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SP-001", "주어진 식별자로 매장 제품을 찾을 수 없습니다.");


    /*
    private final String code;
    private final String message;
    private final int status;
     */

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


}

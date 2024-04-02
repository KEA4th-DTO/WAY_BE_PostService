package com.dto.way.post.global.response.code.status;

import com.dto.way.post.global.response.code.BaseCode;
import com.dto.way.post.global.response.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 멤버 관련 응답
    MEMBER_FOUND(HttpStatus.OK,"MEMBER2001", "회원을 조회했습니다."),
    MEMBER_TERMS_AGREED(HttpStatus.OK, "MEMBER2002", "회원의 이용약관 동의했습니다."),
    MEMBER_UPDATE(HttpStatus.OK, "MEMBER2003", "회원정보를 업데이트 했습니다. "),
    MEMBER_DELETE(HttpStatus.OK, "MEMBER2004", "회원 탈퇴 성공"),
    MEMBER_JOIN(HttpStatus.OK, "MEMBER2005", "회원가입 성공"),
    MEMBER_LOGIN(HttpStatus.OK, "MEMBER2006", "로그인 성공"),

    //  게시글 관련 응답
    DAILY_CREATED(HttpStatus.OK, "DAILY2001", "Daily 게시글이 생성되었습니다."),
    DAILY_UPDATED(HttpStatus.OK,"DAILY2002","Daily 게시글이 수정되었습니다."),
    DAILY_DELETED(HttpStatus.OK,"DAILY2003","Daily 게시글이 삭제되었습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}

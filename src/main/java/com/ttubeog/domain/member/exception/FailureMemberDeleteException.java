package com.ttubeog.domain.member.exception;


public class FailureMemberDeleteException extends RuntimeException {

    public FailureMemberDeleteException(){
        super("회원 탈퇴를 실패하였습니다.");
    }
}

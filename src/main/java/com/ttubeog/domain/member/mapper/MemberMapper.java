package com.ttubeog.domain.member.mapper;

import com.ttubeog.domain.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void save(MemberDto memberDto);
    MemberDto findById(Long id);
    MemberDto findByRefreshToken(String refreshToken);
    void update(MemberDto memberDto);
    void updateRefreshToken(MemberDto memberDto);
}

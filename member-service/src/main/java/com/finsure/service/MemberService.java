package com.finsure.service;

import java.util.List;

import com.finsure.dto.MemberRequestDTO;
import com.finsure.dto.MemberResponseDTO;

public interface MemberService {
    MemberResponseDTO createMember(MemberRequestDTO dto);
    List<MemberResponseDTO> getAllMembers();
    MemberResponseDTO getMemberById(Long id);
    MemberResponseDTO updateMember(Long id, MemberRequestDTO dto);
}

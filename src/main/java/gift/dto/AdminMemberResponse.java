package gift.dto;

import gift.domain.Member;

public record AdminMemberResponse(Long id, String email) {

    public static AdminMemberResponse from(Member member) {
        return new AdminMemberResponse(member.getId(), member.getEmail());
    }
}

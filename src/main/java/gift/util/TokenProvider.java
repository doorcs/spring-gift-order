package gift.util;

import gift.domain.Member;

public interface TokenProvider {

    String createToken(Member member);
    Long getMemberId(String token);
    String getRole(String token);
}

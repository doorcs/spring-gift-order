package gift.dto;

public record KakaoOauthResponse(Long id) {
}

/* id 외에도 connected_at, kakao_account 등의 필드가 있고, kakao_account에서 email을 가져올 수도 있지만
 * "이메일은 사용자 요청에 따라 변경될 수 있으므로, 이를 ID 또는 동일 사용자 여부 판단 기준으로 사용하는 것은 권장하지 않습니다."
 * 라는 API 문서의 가이드를 참고하여 응답의 id값만 추출해 사용하도록 구현했습니다.
 */

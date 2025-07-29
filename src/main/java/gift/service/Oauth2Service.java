package gift.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import gift.domain.KakaoAuth;
import gift.domain.Member;
import gift.domain.embed.Email;
import gift.dto.KakaoOauthResponse;
import gift.dto.KakaoTokenResponse;
import gift.dto.LoginResponse;
import gift.exception.LoginException;
import gift.repository.KakaoAuthRepository;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;

@Service
public class Oauth2Service {

    private final KakaoAuthRepository kakaoAuthRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RestClient restClient;

    public Oauth2Service(
        KakaoAuthRepository kakaoAuthRepository,
        MemberRepository memberRepository,
        TokenProvider tokenProvider,
        RestClient restClient
    ) {
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.restClient = restClient;
    }

    public LoginResponse oauthLogin(
        String clientId,
        String tokenUri,
        String redirectUri,
        String infoUri,
        String code
    ) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        KakaoTokenResponse token = restClient.post()
            .uri(tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(KakaoTokenResponse.class);

        KakaoOauthResponse response = restClient.post()
            .uri(infoUri)
            .header("Authorization", "Bearer " + token.accessToken())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .body(KakaoOauthResponse.class);

        if (response == null || response.id() == null) {
            throw new LoginException("카카오 로그인에 실패했습니다.");
        }
        Email kakaoOauthEmail = new Email(response.id() + "@oauth.kakao");

        if (memberRepository.existsByEmail(kakaoOauthEmail)) {
            // 이미 가입된 회원일 경우(생성된 oauth 메일이 DB에 존재) 바로 로그인 처리
            return new LoginResponse(tokenProvider.createToken(
                memberRepository.findByEmail(kakaoOauthEmail)
                    .orElseThrow(() -> new LoginException("카카오 로그인에 실패했습니다."))
            ));
        }

        Member createdMember = memberRepository.save(
            new Member(response.id() + "@oauth.kakao", "oauth-kakao")
        ); // 비밀번호를 해싱 없이 평문으로 저장하기 때문에 기존 로그인 로직을 통한 공격 불가
        kakaoAuthRepository.save(KakaoAuth.of(createdMember.getId(), token));

        return new LoginResponse(tokenProvider.createToken(createdMember)); // 가입 처리 후 로그인 처리
    }
}

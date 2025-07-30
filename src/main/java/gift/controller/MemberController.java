package gift.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.domain.properties.KakaoOauthProperties;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.service.MemberService;
import gift.service.Oauth2Service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final Oauth2Service oauth2Service;
    private final KakaoOauthProperties kakaoOauthProperties;

    public MemberController(
        MemberService memberService,
        Oauth2Service oauth2Service,
        KakaoOauthProperties kakaoOauthProperties
    ) {
        this.memberService = memberService;
        this.oauth2Service = oauth2Service;
        this.kakaoOauthProperties = kakaoOauthProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> signup(
        @Valid @RequestBody RegisterRequest request
    ){
        LoginResponse response = memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> signin(
        @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = memberService.signin(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/oauth2/kakao") // http://localhost:8080/api/members/oauth2/kakao
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(
            kakaoOauthProperties.clientUri()
                + kakaoOauthProperties.clientId()
                + "&redirect_uri="
                + kakaoOauthProperties.redirectUri()
                + "&response_type=code"
        );
    }

    @GetMapping("/oauth2/kakao/callback")
    public ResponseEntity<LoginResponse> kakaoLoginCallback(@RequestParam String code) {
        LoginResponse resp = oauth2Service.oauthLogin(code);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}

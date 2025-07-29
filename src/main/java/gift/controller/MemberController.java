package gift.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public MemberController(MemberService memberService, Oauth2Service oauth2Service) {
        this.memberService = memberService;
        this.oauth2Service = oauth2Service;
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
    public void kakaoLogin(
        @Value("${auth.oauth2.kakao.client-id}") String clientId,
        @Value("${auth.oauth2.kakao.client-uri}") String clientUri,
        @Value("${auth.oauth2.kakao.redirect-uri}") String redirectUri,
        HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(
            clientUri + clientId + "&redirect_uri=" + redirectUri + "&response_type=code"
        );
    }

    @GetMapping("/oauth2/kakao/callback")
    public ResponseEntity<LoginResponse> kakaoLoginCallback(
        @Value("${auth.oauth2.kakao.client-id}") String clientId,
        @Value("${auth.oauth2.kakao.token-uri}") String tokenUri,
        @Value("${auth.oauth2.kakao.redirect-uri}") String redirectUri,
        @Value("${auth.oauth2.kakao.info-uri}") String infoUri,
        @RequestParam String code
    ){
        LoginResponse resp = oauth2Service.oauthLogin(
            clientId, tokenUri, redirectUri, infoUri, code
        );
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}

package gift.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.KakaoOauth2Response;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.interceptor.MemberAuthInterceptor;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.service.Oauth2Service;
import gift.util.TokenProvider;

@WebMvcTest(MemberController.class)
@TestPropertySource(properties = {"spring.config.location = classpath:test-keys.yml"})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private Oauth2Service oauth2Service;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private MemberRepository memberRepository;

    @MockitoBean
    private MemberAuthInterceptor memberAuthInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        given(memberAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @Test
    void signupTest() throws Exception {
        // given
        String token = "validJwt";
        RegisterRequest request = new RegisterRequest("test@test.com", "1234123!");
        LoginResponse response = new LoginResponse(token);

        given(memberService.signup(any())).willReturn(response);
        String content = objectMapper.writeValueAsString(request);

        // when
        MockHttpServletResponse actual = mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        LoginResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            LoginResponse.class
        );

        assertThat(result.token()).isEqualTo(token);
    }

    @Test
    void signinTest() throws Exception {
        // given
        String token = "validJwt";
        LoginRequest request = new LoginRequest("test@test.com", "1234123!");
        LoginResponse response = new LoginResponse(token);

        given(memberService.signin(any())).willReturn(response);
        String content = objectMapper.writeValueAsString(request);

        // when
        MockHttpServletResponse actual = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.value());

        LoginResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            LoginResponse.class
        );

        assertThat(result.token()).isEqualTo(token);
    }

    @Test
    void kakaoLoginTest() throws Exception {
        // given
        String clientId = "test-client-id";
        String clientUri = "test-client-uri?client_id=";
        String redirectUri = "test-redirect-uri";

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/members/oauth2/kakao"))
            .andReturn().getResponse(); // @Value 어노테이션 설정값들은 test-keys.yml을 통해 매핑됨!

        // then
        assertAll(
            () -> assertThat(actual.getStatus()).isEqualTo(HttpStatus.FOUND.value()),
            () -> assertThat(actual.getRedirectedUrl()).contains(
                "client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code"
            )
        );
    }

    @Test
    void kakaoLoginCallbackTest() throws Exception {
        // given
        String clientId = "test-client-id";
        String redirectUri = "test-redirect-uri";
        String tokenUri = "test-token-uri";
        String code = "validCode";
        String accessToken = "validToken";
        String refreshToken = "validRefreshToken";
        int expiresIn = 21599;
        int refreshTokenExpiresIn = 5183999;

        KakaoOauth2Response expected = new KakaoOauth2Response(
            "bearer",
            accessToken,
            expiresIn,
            refreshToken,
            refreshTokenExpiresIn
        );
        given(oauth2Service.oauthLogin(eq(clientId), eq(tokenUri), eq(redirectUri), eq(code)))
            .willReturn(expected);

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/members/oauth2/kakao/callback")
            .param("code", code)
        ).andReturn().getResponse();

        // then
        KakaoOauth2Response result = objectMapper.readValue(
            actual.getContentAsString(),
            KakaoOauth2Response.class
        );

        assertAll(
            () -> assertThat(result.accessToken()).isEqualTo(accessToken),
            () -> assertThat(result.expiresIn()).isEqualTo(expiresIn),
            () -> assertThat(result.refreshToken()).isEqualTo(refreshToken),
            () -> assertThat(result.refreshTokenExpiresIn()).isEqualTo(refreshTokenExpiresIn)
        );
    }
}

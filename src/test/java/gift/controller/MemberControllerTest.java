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

import gift.domain.properties.KakaoOauthProperties;
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

    @MockitoBean
    private KakaoOauthProperties kakaoOauthProperties;

    @BeforeEach
    void setUp() throws Exception {
        given(memberAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(kakaoOauthProperties.clientId()).willReturn("test-client-id");
        given(kakaoOauthProperties.clientUri()).willReturn("test-client-uri?client_id=");
        given(kakaoOauthProperties.redirectUri()).willReturn("test-redirect-uri");
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
        String code = "validCode";
        String accessToken = "validToken";

        LoginResponse expected = new LoginResponse(
            accessToken
        );
        given(oauth2Service.oauthLogin(eq(code)))
            .willReturn(expected);

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/members/oauth2/kakao/callback")
            .param("code", code)
        ).andReturn().getResponse();

        // then
        LoginResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            LoginResponse.class
        );

       assertThat(result.token()).isEqualTo(accessToken);
    }
}

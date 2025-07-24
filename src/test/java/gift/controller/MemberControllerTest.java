package gift.controller;

import static org.assertj.core.api.Assertions.*;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.interceptor.MemberAuthInterceptor;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.util.TokenProvider;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

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
}

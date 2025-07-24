package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import gift.domain.Member;
import gift.domain.embed.Email;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.exception.LoginException;
import gift.exception.RegisterException;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;

public class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        passwordEncoder,
        tokenProvider
    );

    @Test
    void signupTest() {
        // given
        RegisterRequest request = new RegisterRequest("test@test.com", "1234123!");
        Member savedMember = new Member(1L, "test@test.com", "dbPassword", "ROLE_USER");
        String token = "validJwt";
        given(passwordEncoder.encode("1234123!")).willReturn("dbPassword");
        given(memberRepository.existsByEmail(new Email("test@test.com"))).willReturn(false);
        given(memberRepository.save(any())).willReturn(savedMember);
        given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));
        given(tokenProvider.createToken(savedMember)).willReturn(token);

        // when
        LoginResponse response = memberService.signup(request);

        // then
        assertThat(response.token()).isEqualTo("Bearer " + token);
    }

    @Test
    void signupFailTest() {
        // given
        RegisterRequest request = new RegisterRequest("test@test.com", "1234123!");
        given(memberRepository.existsByEmail(new Email("test@test.com"))).willReturn(true);

        // when, then
        assertThatThrownBy(() -> memberService.signup(request))
            .isInstanceOf(RegisterException.class);
    }

    @Test
    void signinTest() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "1234123!");
        Member member = new Member(
            "test@test.com", "dbPassword"
        );
        String token = "validJwt";
        given(passwordEncoder.matches("1234123!", "dbPassword")).willReturn(true);
        given(memberRepository.findByEmail(new Email("test@test.com"))).willReturn(Optional.of(member));
        given(tokenProvider.createToken(member)).willReturn(token);

        // when
        LoginResponse response = memberService.signin(request);

        // then
        assertThat(response.token()).isEqualTo("Bearer " + token);
    }

    @Test
    void signinFailTest() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
        Member member = new Member(
            "test@test.com", "dbPassword"
        );
        given(memberRepository.findByEmail(new Email("test@test.com"))).willReturn(Optional.of(member));
        given(passwordEncoder.matches("wrongPassword", "dbPassword")).willReturn(false);

        // when, then
        assertThatThrownBy(() -> memberService.signin(request))
            .isInstanceOf(LoginException.class);
    }
}

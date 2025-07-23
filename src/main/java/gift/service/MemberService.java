package gift.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.embed.Email;
import gift.domain.Member;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.exception.LoginException;
import gift.exception.RegisterException;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final String PREFIX = "Bearer ";

    public MemberService(
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        TokenProvider tokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public LoginResponse signup(RegisterRequest request) {
        if (memberRepository.existsByEmail(new Email(request.email()))) {
            throw new RegisterException("이미 가입된 이메일입니다.");
        }

        Member member = memberRepository.save(new Member(
            request.email(),
            passwordEncoder.encode(request.password()))
        );

        return new LoginResponse(PREFIX + tokenProvider.createToken(member));
    }

    @Transactional(readOnly = true)
    public LoginResponse signin(LoginRequest request) {
        Member member = memberRepository.findByEmail(new Email(request.email()))
            .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(PREFIX + tokenProvider.createToken(member));
    }

    // TODO: 추후 브라우저 로컬스토리지나 쿠키에 토큰을 저장한다면, 토큰을 invalidate하는 signout 메서드 구현하기!
}

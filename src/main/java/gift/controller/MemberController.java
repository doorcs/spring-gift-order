package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.service.MemberService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
}

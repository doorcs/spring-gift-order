package gift.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import gift.exception.ApprovalRequiredException;
import gift.exception.AuthorizationRequiredException;
import gift.exception.KakaoApiException;
import gift.exception.LoginException;
import gift.exception.ProductCreateException;
import gift.exception.ProductDeleteException;
import gift.exception.ProductNotFoundException;
import gift.exception.ProductUpdateException;
import gift.exception.RegisterException;
import gift.exception.WishlistAddException;
import gift.exception.WishlistDeleteException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationRequiredException.class)
    public ProblemDetail handleAuthorizationRequiredException(AuthorizationRequiredException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException() { // 예외 메시지를 사용하는 대신, 일반화된 메시지 사용
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "토큰 검증에 실패했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList(); // 오류가 발생하는 항목들을 리스트에 담아 응답

        ProblemDetail error =
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다.");
        error.setProperty("errors", messages); // `detail`엔 String만 담을 수 있기 때문에, 별도의 필드 사용

        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ProblemDetail handleSQLIntegrityConstraintViolationException(
        SQLIntegrityConstraintViolationException e
    ) {
        if (e.getMessage().contains("insert into option"))
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "옵션명은 중복될 수 없습니다.");

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    public ProblemDetail handleSignupException(RegisterException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ProblemDetail handleSigninException(LoginException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ApprovalRequiredException.class)
    public ProblemDetail handleApprovalRequiredException(ApprovalRequiredException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ProductCreateException.class)
    public ProblemDetail handleProductCreateException(ProductCreateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ProductUpdateException.class)
    public ProblemDetail handleProductUpdateException(ProductUpdateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ProductDeleteException.class)
    public ProblemDetail handleProductDeleteException(ProductDeleteException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(WishlistAddException.class)
    public ProblemDetail handleWishlistAddException(WishlistAddException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(WishlistDeleteException.class)
    public ProblemDetail handleWishlistDeleteException(WishlistDeleteException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(KakaoApiException.class)
    public ProblemDetail handleKakaoApiException(KakaoApiException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handleRestClientException(RestClientException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}

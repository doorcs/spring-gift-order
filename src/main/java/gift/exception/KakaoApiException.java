package gift.exception;

public class KakaoApiException extends RuntimeException {

    public KakaoApiException(String code, String msg) {
        super("요청 데이터에 오류가 있습니다. 코드: " + code + ", 메시지: " + msg);
    }
}

# 해보자!

## 토큰 관련 Best Practice 공부하기
- [x] JWT 토큰이 de facto standard가 된 이유? 다른 토큰은 안 쓰나?
- [x] 토큰과 다른 인증방식의 장단점, 다른 걸 쓰는게 적절한 경우는 없는지 알아보기
- [x] JWT 사용시 주의점 알아보기
- [x] Java/Spring에서 JWT를 쓸 때의 Best Practice 알아보기
  - [x] 암호화는 그냥 SHA256 쓰면 되나?
  - [x] 페이로드는 암호화되는게 아니다!! 절대 민감한 정보를 넣으면 안 된다. 그럼 뭘 넣어야하나? 토큰에 PK를 노출시켜도 되나?
  - [x] 토큰 유효기간은 어떻게 설정?
  - [ ] OAuth가 뭔데? 리프레시 토큰은 또 뭔데?
- - -
1. 세션은 서버에서 세션 정보를 기억하고 있어야 한다. 즉, stateful하다
   - 서버를 horizontal scaling(scale out)할 때, 세션 정보를 모든 서버가 공유해야함. session storage 필요
   - -> 성능 이슈 뿐만 아니라, 구조가 복잡해짐(error-prone!)
2. JWT를 사용하면 stateless한 서버 아키텍쳐를 구성할 수 있다.
   - 분산 서버 환경이나 MSA 구조에 어울린다고 함
3. 왜 JWT? 다른 토큰도 있는데?
   - JSON 기반이라 웹 프론트엔드나 모바일 클라이언트와의 연동이 쉬움
   - `Base64(Header).Base64(Payload).Base64(Signature)`
   - stateless 구조의 이점은 다른 토큰들과 동일하지만, 이미 JWT가 광범위한 생태계를 구축해뒀을 뿐. 즉, 일종의 선점효과
4. 주의사항
   - **절대로** 페이로드에 민감한 정보를 담지 말아야 한다. 페이로드는 암호화가 아니라 그냥 Base64 인코딩된다
   - 헤더의 알고리즘을 none으로 두지 말아야 한다!!
   - 모놀리틱 서버라면 HS256같은 대칭키 알고리즘을 사용해도 되지만, MSA 구조라면 RS256같은 비대칭키 알고리즘을 사용해야 한다고 함
   - 엑세스 토큰의 유효기간은 가급적 짧게 설정하고, 리프레시 토큰을 통해 엑세스 토큰을 재발급받도록 하는 구조가 좋다고 함. OAuth랑 같이 공부해보기
- [ ] TODO: 토큰을 통해 Auto Increment인 ID를 유추할 수 없도록 UUID 적용해보기

## 테스트 코드 작성하기
- [x] JUnit 프레임워크 사용법 훑어보기 (당장 필요한 내용 위주로)
- [ ] 테스트 코드 작성시 유의사항 알아보기
- [ ] 테스트 코드 작성
  - [ ] 통합 테스트
  - [x] 단위 테스트

## 커스텀 어노테이션으로 상품명 검증 수행하기
- [x] 어노테이션 인터페이스 사용법, 커스텀 어노테이션 만드는 방법 찾아보기
- [x] `@Valid` 어노테이션의 검증 프로세스 알아보기
- [x] 커스텀 어노테이션을 `@Valid`의 검증 프로세스에 적용하는 방법 알아보기
- [ ] 커스텀 어노테이션 활용시 파일명, 코드 구조의 Best Practice 알아보기
- [x] 적용
- - -
1. `public @interface "커스텀 어노테이션명"` 인터페이스를 작성
    - 필드에 대한 검증을 수행할것이므로 `@Target(ElementType.FIELD)` 어노테이션 추가
    - 검증은 런타임에 수행됨. 따라서 런타임까지 어노테이션이 유지되어야 하므로 `@Retention(RetentionPolicy.RUNTIME)` 어노테이션 추가
    - 검증 로직을 수행할 validator 클래스를 지정해주기 위해 `@Constraint(validatedBy = {"validator 클래스명".class})` 어노테이션 추가 
2. `public interface ConstraintValidator<A extends Annotation, T>` 인터페이스를 구현하는 validator 클래스를 작성
   - `default void initialize(A constraintAnnotation)`는 어노테이션의 속성값 처리를 위한 메서드로, 기본값은 no-op이며 구현하지 않아도 됨
   - `boolean isValid(T value, ConstraintValidatorContext context)` 메서드에 원하는 검증 로직을 작성. true면 통과, false면 검증 실패
3. 검증을 원하는 필드 위에 커스텀 어노테이션을 붙여주면 됨. 커스텀 어노테이션을 통한 검증도 실패시 `MethodArgumentNotValidException`을 던져줌!!
4. 파일명이나 패키지 구조를 어떻게 가져가는게 Best Practice인지는 아직 모르겠다... 일단 `jakarta.validation` 처럼 `validation` 패키지에 모아둠

## RFC 문서 읽어보기
- [ ] HTTP 프로토콜 관련 문서들 원문으로 읽어보기
  - [ ] RFC 2616 - HTTP/1.1 (구버전)
  - [ ] RFC 7230 - HTTP/1.1 : Message Syntax and Routing
  - [ ] RFC 7231 - HTTP/1.1 : Semantics and Content
  - [ ] RFC 7232 - HTTP/1.1 : Conditional Requests
  - [ ] RFC 7233 - HTTP/1.1 : Range Requests 
  - [ ] RFC 7234 - HTTP/1.1 : Caching 
  - [ ] RFC 7235 - HTTP/1.1 : Authentication

## 인증, 인가, RBAC
- [x] 인증(Authentication)과 인가(Authorization)의 차이점 알아보기
- [x] RBAC(Role-Based Access Control) 알아보기
  - [ ] RBAC의 개념과 장단점
  - [x] RBAC를 구현하는 방법 (스프링 시큐리티 없이!)
  - [ ] 스프링 시큐리티로 RBAC를 구현하는 방법

## DTO, 예외
- 현재 클래스의 역할 단위로 패키지가 나뉘어져 있는데, 더 많아지다 보면 특정 도메인 단위로 패키지를 나눌 수 있을 것
- 예외(오류) 응답을 규격화. 예외를 포멧화시키고 상속, 인터페이스 구현 등으로 담고 있는 내용과 처리 로직도 공통화시킨다면 예외 클래스의 수를 줄일 수 있을 것
- 예외 시 사용하는 값을 enumeration이나 별도의 상수로 처리하는 경우도 많다고 함. 이것도 한번 찾아보자

- 한가지 클래스만으로 모든 것을 구현하는것보단 DTO와 Exception이 많아지는 편이 낫다!
- 비즈니스 로직이 흩어지는것 보단 불변의 DTO나 Exception이 많은 것이 낫다!

## 리플렉션, `.class`
- [ ] `.class`가 뭔데?
- [ ] `Class` 클래스는 뭔데?
- [ ] 어떨 때 쓰는거지? 어떤 장점이 있지? 주의사항? 웹 애플리케이션 개발에서도 쓸 일이 있나?

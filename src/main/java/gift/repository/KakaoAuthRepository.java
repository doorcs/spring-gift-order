package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.domain.KakaoAuth;

public interface KakaoAuthRepository extends JpaRepository<KakaoAuth,Long> {
}

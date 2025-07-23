package gift.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.domain.embed.Email;
import gift.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long id);

    boolean existsByEmail(Email email);
}

package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.domain.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
}

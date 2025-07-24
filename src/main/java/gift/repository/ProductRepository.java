package gift.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

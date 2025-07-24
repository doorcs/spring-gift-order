package gift.domain.embed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import gift.domain.Wish;

@Embeddable
public class Wishlist {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public Wishlist() {}

    public List<Wish> getWishlist() {
        return Collections.unmodifiableList(new ArrayList<>(wishes));
    }

    public void add(Wish wish) {
        if (wishes.stream()
            .anyMatch(elem -> elem.getProduct().getId().equals(wish.getProduct().getId()))) {
            throw new IllegalArgumentException("동일한 상품이 이미 위시리스트에 존재합니다.");
        }

        this.wishes.add(wish);
    }

    public void remove(Wish wish) {
        this.wishes.removeIf(elem -> elem.getProduct().getId().equals(wish.getProduct().getId()));
    }
}

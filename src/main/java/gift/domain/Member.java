package gift.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import gift.domain.embed.Email;
import gift.domain.embed.Password;
import gift.domain.embed.Role;
import gift.domain.embed.Wishlist;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Role role;

    @Embedded
    private Wishlist wishlist;

    protected Member() {}

    public Member(String email, String password) {
        this(null, email, password, "ROLE_USER");
    }

    public Member(Long id, String email, String password, String role) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = new Role(role);
        this.wishlist = new Wishlist();
    }

    public List<Wish> getWishlist() {
        return wishlist.getWishlist();
    }

    public void addToWishlist(Product product) {
        this.wishlist.add(
            new Wish(this, product)
        );
    }

    public void removeFromWishlist(Product product) {
        this.wishlist.remove(
            new Wish(this, product)
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public String getRole() {
        return this.role.getRole();
    }
}

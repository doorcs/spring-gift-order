package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import gift.domain.embed.Quantity;

@Entity
@Table(name = "wish", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "product_id"})
})
public class Wish {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected Wish() {}

    public Wish(Member member, Product product) {
        this(null, member, product, 1L);
    }

    public Wish(Long id, Member member, Product product, Long quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}

package gift.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import gift.domain.embed.ImageUrl;
import gift.domain.embed.Optionlist;
import gift.domain.embed.ProductName;
import gift.domain.embed.Price;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName productName;

    @Embedded
    private Price price;

    @Embedded
    private ImageUrl imageUrl;

    @Embedded
    private Optionlist options;

    protected Product() {}

    public Product(String name, Long price, String imageUrl, List<Option> options) {
        this(null, name, price, imageUrl, options);
    }

    public Product(Long id, String name, Long price, String imageUrl, List<Option> options) {
        this.id = id;
        this.productName = new ProductName(name);
        this.price = new Price(price);
        this.imageUrl = new ImageUrl(imageUrl);
        this.options = new Optionlist(options);
    }

    public List<Option> getOptions() {
        return options.getOptions();
    }

    public void add(Option option) {
        options.add(option);
    }

    public void addAll(List<Option> options) {
        this.options.addAll(options);
    }

    public void remove(Option option) {
        options.remove(option);
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName.getProductName();
    }

    public Long getPrice() {
        return price.getPrice();
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }
}

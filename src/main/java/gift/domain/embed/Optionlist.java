package gift.domain.embed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import gift.domain.Option;

@Embeddable
public class Optionlist {

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Optionlist() {}

    public Optionlist(List<Option> options) {
        this.options = options;
    }

    public List<Option> getOptions() {
        return Collections.unmodifiableList(new ArrayList<>(options));
    }

    public void addAll(List<Option> options) {
        this.options.addAll(options);
    }

    public void add(Option option) {
        if (options.stream()
            .anyMatch(elem -> elem.getId().equals(option.getId()))) {
            throw new IllegalArgumentException("동일한 옵션이 이미 존재합니다.");
        }

        options.add(option);
    }

    public void remove(Option option) {
        options.removeIf(elem -> elem.getId().equals(option.getId()));
    }
}

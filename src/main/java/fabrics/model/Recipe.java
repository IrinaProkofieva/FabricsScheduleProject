package fabrics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Map;

@JsonIdentityInfo(scope = Recipe.class, generator=ObjectIdGenerators.PropertyGenerator.class, property="name")
public class Recipe{

    // Название
    String name;
    // Требуемые продукты
    List<Product> components;
    // Результирующий продукт
    Product product;

    @JsonCreator
    public Recipe(
            @JsonProperty("name") String name,
            @JsonProperty("components") List<Product> components,
            @JsonProperty("product") Product product
    ) {
        this.name = name;
        this.components = components;
        this.product = product;
    }

    public boolean isAvailable(Map<String, Integer> availableProducts) {
        return components.stream().allMatch(c -> availableProducts.getOrDefault(c.id(), 0) >= c.num());
    }

    public Product getProduct() {
        return product;
    }

    public List<Product> getComponents() {
        return components;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComponents(List<Product> components) {
        this.components = components;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

package fabrics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Ability(
        // Название рецепта
        @JsonProperty("name")
        Recipe recipe,
        // Длительность изготовления
        Integer duration
) {
}

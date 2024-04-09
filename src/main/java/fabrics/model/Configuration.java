package fabrics.model;

import java.util.List;

public record Configuration(
        List<Product> products,
        List<Recipe> recipes,
        List<Project> projects,
        List<Building> buildings
) {
}

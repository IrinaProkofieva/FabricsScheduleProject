package fabrics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

// Тип фабрики
@JsonIdentityInfo(scope=Project.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="name")
public class Project {
    // Название типа фабрики
    String name;
    // Возможности
    List<Ability> abilities;

    @JsonCreator
    public Project(
            @JsonProperty("name")
            String name,
            @JsonProperty("abilities")
            List<Ability> abilities
    ) {
        this.name = name;
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }
}
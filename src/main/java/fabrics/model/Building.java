package fabrics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = Building.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="name")
public class Building {

    // Название фабрики
    String name;
    // Тип фабрики
    Project project;

    @JsonCreator
    public Building(
            @JsonProperty("name")
            String name,
            @JsonProperty("project")
            Project project
    ) {
        this.name = name;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

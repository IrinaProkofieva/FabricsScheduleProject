package fabrics.model;

public record ProductionLogRow(
        Integer time,
        String factory,
        String recipe
) {
    @Override
    public String toString() {
        return "time: %d, factory: %s, recipe: %s".formatted(time, factory, recipe);
    }
}

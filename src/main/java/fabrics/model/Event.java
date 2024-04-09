package fabrics.model;

import java.util.Comparator;

public record Event(
        Integer time,
        EventType type,
        Building building,
        Recipe recipe
) implements Comparable<Event>{

    @Override
    public int compareTo(Event o) {
        // Сравниваем по времени, если два события произошли одновременно,
        // то сравниваем по типу - считаем, что ресурсы сначала освобождаются, затем забираются,
        // в случае совпадения типов порядок не важен
        return Comparator.comparingInt(Event::time)
                .thenComparingInt(e -> e.type.getCode())
                .compare(this, o);
    }
}

package fabrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fabrics.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    // Счетчик продуктов
    public static Map<String, Integer> productCounts;
    // Список доступных рецептов
    public static List<Recipe> AVAILABLE_RECIPE = new ArrayList<>();
    // Список недоступных рецептов
    public static List<Recipe> UNAVAILABLE_RECIPE = new ArrayList<>();
    // Запланированные события
    public static final Queue<Event> EVENTS = new PriorityQueue<>();
    // Свободные фабрики
    public static final List<Building> AVAILABLE_BUILDINGS = new ArrayList<>();
    // Лог производства
    public static final List<ProductionLogRow> PRODUCTION_LOG = new ArrayList<>();
    // Текущее время
    public static Integer currentTime = 0;

    public static void main(String[] args) throws IOException {
        // Читаем входные данные
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Configuration configuration = mapper.readValue(new File("src/main/resources/data.yaml"),
                Configuration.class);

        // Инициализируем данные
        List<Recipe> recipeList = configuration.recipes();
        productCounts = configuration.products().stream().collect(Collectors.toMap(Product::id, Product::num));

        recipeList.forEach(r -> {
            boolean isAvailable = r.isAvailable(productCounts);
            if (isAvailable) {
                AVAILABLE_RECIPE.add(r);
            } else {
                UNAVAILABLE_RECIPE.add(r);
            }
        });

        AVAILABLE_BUILDINGS.addAll(configuration.buildings());

        // Производим расчет
        createEvent(currentTime);

        while (!EVENTS.isEmpty()) {
            Event event = EVENTS.poll();
            currentTime = applyEvent(event);
            createEvent(event.time());
        }

        // Выводим результат
        printAnswer();
    }


    // Ищем, готов ли кто-то взяться за производство
    private static void createEvent(Integer currentTime) {
        Building building = null;
        Ability buildingAbility = null;

        int i = 0;
        while (building == null && i < AVAILABLE_BUILDINGS.size()) {
            var currentBuilding = AVAILABLE_BUILDINGS.get(i);
            buildingAbility = currentBuilding.getProject().getAbilities().stream()
                    .filter(a -> AVAILABLE_RECIPE.contains(a.recipe())).findFirst().orElse(null);
            if (buildingAbility != null) {
                building = currentBuilding;
            }
            i++;
        }

        if (building != null) {
            EVENTS.add(new Event(currentTime, EventType.START_PRODUCTION, building, buildingAbility.recipe()));
            EVENTS.add(new Event(currentTime + buildingAbility.duration(), EventType.FINISH_PRODUCTION,
                    building, buildingAbility.recipe()));
            AVAILABLE_BUILDINGS.remove(building);
        }
    }

    // Обрабатываем событие - изменяем счетчики
    private static Integer applyEvent(Event event) {
        switch (event.type()) {
            case FINISH_PRODUCTION -> {
                var resultProduct = event.recipe().getProduct();
                productCounts.compute(resultProduct.id(),
                        (k, v) -> (v == null) ? resultProduct.num() : v + resultProduct.num());
                AVAILABLE_BUILDINGS.add(event.building());

                var potentiallyAvailableRecipes = UNAVAILABLE_RECIPE.stream()
                        .filter(r -> r.getComponents().stream()
                                .anyMatch(p -> p.id().equals(resultProduct.id())
                                        && p.num() <= productCounts.get(resultProduct.id())))
                        .toList();
                potentiallyAvailableRecipes.forEach(r -> {
                            if (r.isAvailable(productCounts)) {
                                AVAILABLE_RECIPE.add(r);
                                UNAVAILABLE_RECIPE.remove(r);
                            }
                        });
            }
            case START_PRODUCTION -> {
                PRODUCTION_LOG.add(new ProductionLogRow(event.time(), event.building().getName(),
                        event.recipe().getName()));
                var components = event.recipe().getComponents();
                var componentNames = components.stream().map(Product::id).toList();
                for (Product product: components) {
                    productCounts.compute(product.id(),
                            (k, v) -> (v == null) ? product.num() : v - product.num());
                }

                var potentiallyUnavailableRecipes = AVAILABLE_RECIPE.stream()
                        .filter(r -> r.getComponents().stream().anyMatch(p -> componentNames.contains(p.id())
                                && p.num() > productCounts.get(p.id())))
                        .toList();
                potentiallyUnavailableRecipes.forEach(r -> {
                    if (!r.isAvailable(productCounts)) {
                        AVAILABLE_RECIPE.remove(r);
                        UNAVAILABLE_RECIPE.add(r);
                    }
                });
            }
            default -> {}
        }
        return event.time();
    }

    private static void printAnswer() {
        System.out.println("productionStartLog:");
        for (ProductionLogRow row: PRODUCTION_LOG) {
            System.out.println(row);
        }
        System.out.printf("totalTime: %d%n", currentTime);
        System.out.println("products:");
        System.out.println(productCounts);
    }
}

package com.paradigmas.fp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MAP / FILTER / REDUCE con la Stream API de Java
 * =================================================
 * Stream.map()     → transforma cada elemento
 * Stream.filter()  → selecciona elementos según predicado
 * Stream.reduce()  → acumula / colapsa a un solo valor
 * Stream.collect() → materializa el stream en una colección
 *
 * Java 26: var, records, pattern matching, toList()
 */
public class MapFilterReduce {

    record Producto(String nombre, double precio, String categoria) {}

    public static void main(String[] args) {

        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // ── MAP ──────────────────────────────────────────────────────
        System.out.println("=== map ===");
        var cuadrados = numeros.stream()
                               .map(x -> x * x)
                               .toList();
        System.out.println("Cuadrados: " + cuadrados);

        var cadenas = numeros.stream()
                             .map(x -> "num_" + x)
                             .toList();
        System.out.println("Cadenas:   " + cadenas);

        // ── FILTER ───────────────────────────────────────────────────
        System.out.println("\n=== filter ===");
        var pares = numeros.stream()
                           .filter(x -> x % 2 == 0)
                           .toList();
        System.out.println("Pares:    " + pares);

        var mayoresDe5 = numeros.stream()
                                .filter(x -> x > 5)
                                .toList();
        System.out.println("Mayores de 5: " + mayoresDe5);

        // ── REDUCE ───────────────────────────────────────────────────
        System.out.println("\n=== reduce ===");
        int suma = numeros.stream()
                          .reduce(0, Integer::sum);
        System.out.println("Suma:     " + suma);

        int producto = numeros.stream()
                              .reduce(1, (acc, x) -> acc * x);
        System.out.println("Producto: " + producto);

        Optional<Integer> maximo = numeros.stream()
                                          .reduce(Integer::max);
        System.out.println("Máximo:   " + maximo.orElse(-1));

        // ── PIPELINE encadenado ──────────────────────────────────────
        System.out.println("\n=== Pipeline encadenado ===");
        // Suma de cuadrados de números impares
        int sumaCuadImpares = numeros.stream()
                                     .filter(x -> x % 2 != 0)
                                     .map(x -> x * x)
                                     .reduce(0, Integer::sum);
        System.out.println("Suma de cuadrados de impares: " + sumaCuadImpares);

        // ── EJEMPLO PRÁCTICO: productos ──────────────────────────────
        System.out.println("\n=== Productos ===");
        var productos = List.of(
            new Producto("Laptop",  1200, "tecnologia"),
            new Producto("Libro",   25,   "educacion"),
            new Producto("Monitor", 350,  "tecnologia"),
            new Producto("Silla",   180,  "muebles"),
            new Producto("Teclado", 80,   "tecnologia")
        );

        // Nombres en mayúsculas de tech con precio < 500
        var techBarata = productos.stream()
                                  .filter(p -> p.categoria().equals("tecnologia"))
                                  .filter(p -> p.precio() < 500)
                                  .map(p -> p.nombre().toUpperCase())
                                  .toList();
        System.out.println("Tech barata (< $500): " + techBarata);

        // Total invertido en tecnología
        double totalTech = productos.stream()
                                    .filter(p -> p.categoria().equals("tecnologia"))
                                    .mapToDouble(Producto::precio)
                                    .sum();
        System.out.println("Total tecnología: $" + totalTech);

        // Agrupar por categoría
        Map<String, List<Producto>> porCategoria = productos.stream()
                                                            .collect(Collectors.groupingBy(Producto::categoria));
        System.out.println("\nProductos por categoría:");
        porCategoria.forEach((cat, prods) ->
            System.out.println("  " + cat + ": " + prods.stream().map(Producto::nombre).toList())
        );

        // Estadísticas de precios
        var stats = productos.stream()
                             .mapToDouble(Producto::precio)
                             .summaryStatistics();
        System.out.printf("%nEstadísticas precios → min: $%.0f, max: $%.0f, avg: $%.1f%n",
                          stats.getMin(), stats.getMax(), stats.getAverage());
    }
}

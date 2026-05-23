package com.paradigmas.fp.composicion;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * COMPOSICIÓN DE FUNCIONES
 * =========================
 * Java provee Function.compose() y Function.andThen():
 *
 *   f.andThen(g)   → g(f(x))  [izquierda a derecha, como pipe]
 *   f.compose(g)   → f(g(x))  [derecha a izquierda, matemático]
 *
 * Podemos construir utilidades pipe() y compose() genéricas
 * que encadenen N funciones.
 *
 * Java 26: var, generics, lambdas, method references
 */
public class Composicion {

    // ── Utilidades genéricas ─────────────────────────────────────────
    /**
     * pipe(f, g, h)(x) == h(g(f(x)))  — izquierda a derecha.
     * Usamos lambda explícita para evitar ambigüedad de inferencia
     * con Function::andThen en reduce().
     */
    @SafeVarargs
    static <T> Function<T, T> pipe(UnaryOperator<T>... funcs) {
        return x -> {
            T result = x;
            for (var f : funcs) result = f.apply(result);
            return result;
        };
    }

    /**
     * compose(f, g, h)(x) == f(g(h(x)))  — derecha a izquierda.
     */
    @SafeVarargs
    static <T> Function<T, T> compose(UnaryOperator<T>... funcs) {
        return x -> {
            T result = x;
            for (int i = funcs.length - 1; i >= 0; i--) result = funcs[i].apply(result);
            return result;
        };
    }

    // ── Funciones atómicas de texto ──────────────────────────────────
    static UnaryOperator<String> limpiar          = String::strip;
    static UnaryOperator<String> normalizar        = String::toLowerCase;
    static UnaryOperator<String> capitalizarPalab  = s -> java.util.Arrays.stream(s.split(" "))
        .map(w -> w.isEmpty() ? w : Character.toUpperCase(w.charAt(0)) + w.substring(1))
        .collect(java.util.stream.Collectors.joining(" "));
    static UnaryOperator<String> quitarPuntuacion  = s -> s.replaceAll("[^\\w\\s]", "");
    static UnaryOperator<String> agregarExclamacion = s -> s + "!";

    // ── Funciones numéricas ──────────────────────────────────────────
    static UnaryOperator<Integer> doble    = x -> x * 2;
    static UnaryOperator<Integer> sumar1   = x -> x + 1;
    static UnaryOperator<Integer> negativo = x -> -x;

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== andThen vs compose ===");
        // pipe:    doble → sumar1 → negativo    (izq a der)
        // compose: negativo ∘ sumar1 ∘ doble    (der a izq, mismo resultado)
        Function<Integer, Integer> pipeline = ((Function<Integer,Integer>) doble)
                                                .andThen(sumar1)
                                                .andThen(negativo);
        System.out.println("pipe(doble, +1, neg)(5) = " + pipeline.apply(5)); // -11

        Function<Integer, Integer> composed = ((Function<Integer,Integer>) negativo)
                                                .compose(sumar1)
                                                .compose(doble);
        System.out.println("neg∘(+1)∘doble    (5) = " + composed.apply(5));  // -11

        System.out.println("\n=== pipe() genérico ===");
        var formatear = pipe(limpiar, normalizar, quitarPuntuacion, capitalizarPalab);
        List<String> sucios = List.of("  juan   ", "MARÍA JOSÉ!", "  carlos  ");
        sucios.forEach(n -> System.out.printf("'%s'  →  '%s'%n", n, formatear.apply(n)));

        System.out.println("\n=== Pipeline completo con saludo festivo ===");
        Function<String, String> saludoFestivo =
            ((Function<String, String>) formatear::apply)
                .andThen(n -> "Hola, " + n)
                .andThen(agregarExclamacion);

        System.out.println(saludoFestivo.apply("  ana  "));   // Hola, Ana!
        System.out.println(saludoFestivo.apply("PEDRO!!!"));  // Hola, Pedro!

        System.out.println("\n=== Composición sobre lista ===");
        var nums = List.of(1, 2, 3, 4, 5);
        // doble → sumar1 → negativo aplicado a cada elemento
        var resultado = nums.stream()
                            .map(((Function<Integer,Integer>) doble).andThen(sumar1).andThen(negativo))
                            .toList();
        System.out.println("Resultado: " + resultado); // [-3, -5, -7, -9, -11]

        System.out.println("\n=== Composición de validadores ===");
        // Validadores como funciones puras componibles
        Function<String, Boolean> noVacio    = s -> !s.isBlank();
        Function<String, Boolean> longMin3   = s -> s.length() >= 3;
        Function<String, Boolean> soloLetras = s -> s.matches("[a-zA-ZáéíóúÁÉÍÓÚ ]+");

        // Combinador AND genérico: encadena predicados
        Function<String, Boolean> validarNombre =
            s -> noVacio.apply(s) && longMin3.apply(s) && soloLetras.apply(s);

        // Aplicar el validador compuesto sobre varios nombres (strip previo con pipe)
        Function<String, String> prepararNombre = pipe(limpiar, quitarPuntuacion);

        List<String> nombres = List.of("Ana", "", "J", "Carlos123", "  María  ");
        nombres.forEach(n -> {
            var limpio = prepararNombre.apply(n);
            boolean valido = validarNombre.apply(limpio);
            System.out.printf("'%s' → limpio='%s' válido: %b%n", n, limpio, valido);
        });
    }
}

package com.paradigmas.fp.currying;

import java.util.function.*;

/**
 * CURRYING Y APLICACIÓN PARCIAL
 * ==============================
 * Currying: f(a,b) → f(a)(b)
 *   Cada invocación recibe un argumento y retorna
 *   una función que espera el siguiente.
 *
 * Java usa Function<A, Function<B, C>> para representar
 * funciones curried de 2 argumentos.
 *
 * Java 26: var, lambdas, interfaces funcionales, method references
 */
public class Currying {

    // ── CURRYING de 2 argumentos ─────────────────────────────────────
    static Function<Integer, Function<Integer, Integer>> suma =
        a -> b -> a + b;

    static Function<Double, Function<Double, Double>> potencia =
        base -> exp -> Math.pow(base, exp);

    // ── CURRYING de 3 argumentos ─────────────────────────────────────
    static Function<Double, Function<Double, Function<Double, Double>>> volumen =
        largo -> ancho -> alto -> largo * ancho * alto;

    // ── Utilidad: convertir BiFunction en curried ────────────────────
    static <A, B, C> Function<A, Function<B, C>> curry(BiFunction<A, B, C> f) {
        return a -> b -> f.apply(a, b);
    }

    // ── Utilidad: aplicación parcial (fijar primer argumento) ────────
    static <A, B, C> Function<B, C> partial(BiFunction<A, B, C> f, A a) {
        return b -> f.apply(a, b);
    }

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Currying básico ===");
        System.out.println(suma.apply(3).apply(4));    // 7

        var suma5  = suma.apply(5);   // función especializada "suma 5 a algo"
        var suma10 = suma.apply(10);
        System.out.println(suma5.apply(8));   // 13
        System.out.println(suma10.apply(7));  // 17

        System.out.println("\n=== Currying 3 argumentos ===");
        System.out.println(volumen.apply(2.0).apply(3.0).apply(4.0));  // 24.0
        var volCaja5x5 = volumen.apply(5.0).apply(5.0);  // parcialmente aplicado
        System.out.println(volCaja5x5.apply(10.0));        // 250.0

        System.out.println("\n=== Potencia curried ===");
        var cuadrado = potencia.apply(2.0);  // base = 2, espera exponente
        var cubo     = potencia.apply(3.0);
        System.out.println("2^10 = " + cuadrado.apply(10.0));  // 1024.0
        System.out.println("3^4  = " + cubo.apply(4.0));       // 81.0

        System.out.println("\n=== curry() genérico sobre BiFunction ===");
        BiFunction<String, Integer, String> repetir = String::repeat;
        var repetirCurried = curry(repetir);
        var triplicar = repetirCurried.apply("*");
        System.out.println(triplicar.apply(3));  // ***
        System.out.println(triplicar.apply(5));  // *****

        System.out.println("\n=== Aplicación parcial ===");
        BiFunction<String, String, String> log = (nivel, msg) ->
            "[" + nivel + "] " + msg;

        Function<String, String> logInfo  = partial(log, "INFO");
        Function<String, String> logError = partial(log, "ERROR");

        System.out.println(logInfo.apply("Servidor iniciado"));
        System.out.println(logError.apply("No se encontró el archivo"));

        System.out.println("\n=== Pipeline con currying ===");
        // Crear un pipeline de validación de precios
        Function<Double, Function<Double, Boolean>> enRango =
            min -> max -> min <= max;

        var esMayorDe0   = partial((BiFunction<Double, Double, Boolean>)
            (min, x) -> x > min, 0.0);
        var esMenorDe1000 = partial((BiFunction<Double, Double, Boolean>)
            (max, x) -> x < max, 1000.0);

        var precios = java.util.List.of(150.0, -10.0, 999.0, 1500.0, 0.5);
        precios.forEach(p ->
            System.out.printf("$%.1f válido: %b%n", p,
                esMayorDe0.apply(p) && esMenorDe1000.apply(p))
        );
    }
}

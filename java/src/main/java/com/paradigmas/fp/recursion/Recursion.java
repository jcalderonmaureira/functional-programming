package com.paradigmas.fp.recursion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * RECURSIÓN
 * ==========
 * En programación funcional la recursión reemplaza a los bucles.
 * Java no optimiza tail-call automáticamente, pero podemos:
 *   - Usar acumuladores para simular tail-call
 *   - Usar memoización con HashMap / Map.computeIfAbsent
 *   - Usar Stream.iterate() para recursión "lazy"
 *
 * Java 26: records, var, Streams, Map.computeIfAbsent
 */
public class Recursion {

    // ── FACTORIAL ────────────────────────────────────────────────────
    static long factorial(int n) {
        if (n <= 1) return 1L;
        return n * factorial(n - 1);
    }

    /** Tail-recursive con acumulador. */
    static long factorialTail(int n, long acumulador) {
        if (n <= 1) return acumulador;
        return factorialTail(n - 1, (long) n * acumulador);
    }

    static long factorial(int n, long acc) { return factorialTail(n, acc); }

    // ── FIBONACCI con memoización ─────────────────────────────────────
    private static final Map<Integer, Long> memo = new HashMap<>();

    static long fibonacci(int n) {
        if (n <= 1) return n;
        return memo.computeIfAbsent(n, k -> fibonacci(k - 1) + fibonacci(k - 2));
    }

    /** Fibonacci con Stream.iterate() (enfoque funcional sin recursión explícita) */
    static List<Long> fibonacciStream(int cantidad) {
        record Par(long a, long b) {}
        return Stream.iterate(new Par(0L, 1L), p -> new Par(p.b(), p.a() + p.b()))
                     .limit(cantidad)
                     .map(Par::a)
                     .toList();
    }

    // ── SUMA DE LISTA ─────────────────────────────────────────────────
    static int sumaLista(List<Integer> lista) {
        if (lista.isEmpty()) return 0;
        return lista.getFirst() + sumaLista(lista.subList(1, lista.size()));
    }

    // ── APLANAR lista anidada (con Object genérico) ───────────────────
    @SuppressWarnings("unchecked")
    static List<Object> aplanar(List<?> lista) {
        return lista.stream()
                    .flatMap(elem -> elem instanceof List<?>
                        ? aplanar((List<?>) elem).stream()
                        : Stream.of(elem))
                    .toList();
    }

    // ── QUICKSORT funcional ───────────────────────────────────────────
    static List<Integer> quicksort(List<Integer> lista) {
        if (lista.size() <= 1) return lista;
        int pivote = lista.getFirst();
        var menores = lista.stream().skip(1).filter(x -> x <= pivote).toList();
        var mayores = lista.stream().skip(1).filter(x -> x > pivote).toList();
        return Stream.concat(
            Stream.concat(quicksort(menores).stream(), Stream.of(pivote)),
            quicksort(mayores).stream()
        ).toList();
    }

    // ── POTENCIA por exponenciación rápida ────────────────────────────
    static long potencia(long base, int exp) {
        if (exp == 0) return 1;
        if (exp % 2 == 0) {
            long mitad = potencia(base, exp / 2);
            return mitad * mitad;
        }
        return base * potencia(base, exp - 1);
    }

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Factorial ===");
        System.out.println("10! = " + factorial(10));            // 3628800
        System.out.println("10! = " + factorial(10, 1));         // 3628800

        System.out.println("\n=== Fibonacci ===");
        for (int i = 0; i < 10; i++) System.out.print(fibonacci(i) + " ");
        System.out.println();
        System.out.println("Stream: " + fibonacciStream(10));

        System.out.println("\n=== Suma recursiva ===");
        System.out.println(sumaLista(List.of(1, 2, 3, 4, 5)));  // 15

        System.out.println("\n=== Aplanar lista anidada ===");
        var anidada = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)), 7);
        System.out.println(aplanar(anidada));   // [1, 2, 3, 4, 5, 6, 7]

        System.out.println("\n=== Quicksort ===");
        var desordenada = List.of(3, 6, 8, 10, 1, 2, 1);
        System.out.println(quicksort(desordenada)); // [1, 1, 2, 3, 6, 8, 10]

        System.out.println("\n=== Potencia rápida ===");
        System.out.println("2^10 = " + potencia(2, 10));  // 1024
        System.out.println("3^8  = " + potencia(3, 8));   // 6561
    }
}

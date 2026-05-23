package com.paradigmas.fp.funciones_puras;

import java.util.List;

/**
 * FUNCIONES PURAS
 * ===============
 * Una función pura:
 *   1. Dado el mismo input, siempre retorna el mismo output.
 *   2. No produce efectos secundarios (no modifica estado externo).
 *
 * Java 26: records, lambdas, method references, var
 */
public class FuncionesPuras {

    // ── FUNCIONES PURAS ──────────────────────────────────────────────
    /** Pura: sólo depende de sus argumentos, sin efectos secundarios. */
    public static int suma(int a, int b) {
        return a + b;
    }

    public static double areaCirculo(double radio) {
        return Math.PI * radio * radio;
    }

    public static String saludar(String nombre) {
        return "Hola, " + nombre + "!";
    }

    /** Pura sobre una lista: retorna nueva lista sin mutar la original. */
    public static List<Integer> duplicar(List<Integer> lista) {
        return lista.stream()
                    .map(x -> x * 2)
                    .toList();   // toList() retorna lista inmutable (Java 16+)
    }

    // ── FUNCIONES IMPURAS ────────────────────────────────────────────
    private static int totalAcumulado = 0;  // estado global mutable

    /** Impura: modifica estado externo. */
    public static int sumaImpura(int a, int b) {
        totalAcumulado += a + b;    // efecto secundario
        return a + b;
    }

    /** Impura: efecto secundario de I/O. */
    public static String saludarImpura(String nombre) {
        System.out.println("Hola, " + nombre + "!");  // efecto secundario
        return "Hola, " + nombre + "!";
    }

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Funciones Puras ===");
        System.out.println(suma(3, 4));           // siempre 7
        System.out.println(suma(3, 4));           // siempre 7
        System.out.println(areaCirculo(5));
        System.out.println(saludar("Ana"));
        System.out.println(duplicar(List.of(1, 2, 3, 4, 5)));

        System.out.println("\n=== Función Impura ===");
        System.out.println(sumaImpura(3, 4));
        System.out.println("totalAcumulado = " + totalAcumulado);  // cambia!
        System.out.println(sumaImpura(3, 4));
        System.out.println("totalAcumulado = " + totalAcumulado);  // cambia de nuevo!
    }
}

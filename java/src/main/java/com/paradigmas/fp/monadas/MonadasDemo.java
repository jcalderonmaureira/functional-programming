package com.paradigmas.fp.monadas;

import java.util.List;
import java.util.Map;

/**
 * DEMO: Maybe y Result Monads
 * ============================
 * Muestra cómo encadenar operaciones que pueden fallar
 * sin null-checks anidados ni bloques try/catch en cadena.
 *
 * Java 26: pattern matching con switch, records, var
 */
public class MonadasDemo {

    // ── Datos de ejemplo ──────────────────────────────────────────────
    record Direccion(String calle, String ciudad) {}
    record Usuario(String nombre, Direccion direccion) {}

    static final Map<Integer, Usuario> USUARIOS = Map.of(
        1, new Usuario("Ana",  new Direccion("Calle 1", "Bogotá")),
        2, new Usuario("Luis", null)
    );

    // ── Funciones con Maybe ───────────────────────────────────────────
    static Maybe<Usuario> buscarUsuario(int id) {
        return Maybe.of(USUARIOS.get(id));
    }

    static Maybe<Direccion> obtenerDireccion(Usuario u) {
        return Maybe.of(u.direccion());
    }

    static Maybe<String> obtenerCiudad(Direccion d) {
        return Maybe.of(d.ciudad());
    }

    // ── Funciones con Result ──────────────────────────────────────────
    static Result<Double> dividir(double a, double b) {
        if (b == 0) return Result.err("División por cero");
        return Result.ok(a / b);
    }

    static Result<Double> raizCuadrada(double x) {
        if (x < 0) return Result.err("Raíz de número negativo: " + x);
        return Result.ok(Math.sqrt(x));
    }

    static Result<Integer> parsearEntero(String s) {
        return Result.attempt(() -> Integer.parseInt(s));
    }

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Maybe Monad ===");

        // Pipeline sin null-checks manuales
        var ciudadAna  = buscarUsuario(1).flatMap(MonadasDemo::obtenerDireccion)
                                         .flatMap(MonadasDemo::obtenerCiudad);
        var ciudadLuis = buscarUsuario(2).flatMap(MonadasDemo::obtenerDireccion)
                                         .flatMap(MonadasDemo::obtenerCiudad);
        var ciudadX    = buscarUsuario(99).flatMap(MonadasDemo::obtenerDireccion)
                                          .flatMap(MonadasDemo::obtenerCiudad);

        System.out.println(ciudadAna);                              // Just(Bogotá)
        System.out.println(ciudadLuis.getOrElse("Sin ciudad"));     // Sin ciudad
        System.out.println(ciudadX.getOrElse("Usuario no existe")); // Usuario no existe

        // Pattern matching con switch (Java 21+)
        String mensaje = switch (ciudadAna) {
            case Maybe.Just<String> j -> "Ciudad encontrada: " + j.value();
            case Maybe.Nothing<String> n -> "No hay ciudad";
        };
        System.out.println(mensaje);

        System.out.println("\n=== Result Monad ===");
        var res1 = dividir(100, 4).flatMap(MonadasDemo::raizCuadrada);
        var res2 = dividir(100, 0).flatMap(MonadasDemo::raizCuadrada);
        var res3 = dividir(-25, 1).flatMap(MonadasDemo::raizCuadrada);
        System.out.println(res1);  // Ok(5.0)
        System.out.println(res2);  // Err(División por cero)
        System.out.println(res3);  // Err(Raíz de número negativo: -25.0)

        System.out.println("\n=== Encadenamiento con map ===");
        var resultado = Result.ok(16.0)
                              .map(x -> x / 4)
                              .map(x -> x + 1)
                              .map(x -> x * 2);
        System.out.println(resultado); // Ok(10.0)

        System.out.println("\n=== Result.attempt() captura excepciones ===");
        List.of("42", "abc", "100", "3.14").forEach(s -> {
            var r = parsearEntero(s).map(n -> n * 2);
            System.out.printf("parsear('%s') → %s%n", s, r);
        });

        System.out.println("\n=== Pipeline complejo ===");
        // Leer, parsear, dividir y sacar raíz — todo en un pipeline seguro
        List.of("100", "0", "-4", "noNumero").forEach(entrada -> {
            var resultado2 = parsearEntero(entrada)
                .flatMap(n -> dividir(100.0, n))
                .flatMap(MonadasDemo::raizCuadrada);
            System.out.printf("entrada='%s' → %s%n", entrada, resultado2);
        });
    }
}

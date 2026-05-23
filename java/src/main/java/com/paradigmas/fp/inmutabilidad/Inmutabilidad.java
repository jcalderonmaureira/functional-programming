package com.paradigmas.fp.inmutabilidad;

import java.util.List;
import java.util.stream.Collectors;

/**
 * INMUTABILIDAD
 * =============
 * En programación funcional los datos no se modifican:
 * se crea una nueva versión en lugar de mutar la existente.
 *
 * Java 26 ofrece:
 *   - Records  → clases de datos inmutables por diseño
 *   - List.of() / List.copyOf() → listas no modificables
 *   - Colecciones inmutables con Collections.unmodifiableXxx()
 */
public class Inmutabilidad {

    // ── RECORD: inmutable por definición ─────────────────────────────
    /**
     * Record Punto: campos finales, constructor canónico, equals/hashCode/toString
     * generados automáticamente. No tiene setters → inmutable.
     */
    record Punto(double x, double y) {
        /** En lugar de mutar, retorna un NUEVO Punto desplazado. */
        Punto mover(double dx, double dy) {
            return new Punto(x + dx, y + dy);
        }

        /** Distancia al origen (función pura sobre el record). */
        double distanciaAlOrigen() {
            return Math.sqrt(x * x + y * y);
        }
    }

    // ── RECORD con validación ─────────────────────────────────────────
    record Precio(double valor, String moneda) {
        // Compact constructor con validación
        Precio {
            if (valor < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
            moneda = moneda.toUpperCase();
        }

        Precio aplicarDescuento(double porcentaje) {
            return new Precio(valor * (1 - porcentaje / 100), moneda);
        }

        Precio convertir(String nuevaMoneda, double tasa) {
            return new Precio(valor * tasa, nuevaMoneda);
        }
    }

    // ── MUTABLE (estilo imperativo) ← evitar ─────────────────────────
    static void agregarImpuestoMutable(double[] precios, double tasa) {
        for (int i = 0; i < precios.length; i++) {
            precios[i] *= (1 + tasa);   // MUTA el array original!
        }
    }

    // ── INMUTABLE (estilo funcional) ← preferir ──────────────────────
    static List<Double> agregarImpuesto(List<Double> precios, double tasa) {
        return precios.stream()
                      .map(p -> p * (1 + tasa))
                      .toList();   // nueva lista, original intacta
    }

    // ── DEMO ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Record Punto (inmutable) ===");
        var p1 = new Punto(0, 0);
        var p2 = p1.mover(3, 4);
        System.out.println("p1 = " + p1);   // Punto[x=0.0, y=0.0]
        System.out.println("p2 = " + p2);   // Punto[x=3.0, y=4.0]
        System.out.println("distancia p2: " + p2.distanciaAlOrigen());

        System.out.println("\n=== Record Precio con validación ===");
        var precio = new Precio(1000, "usd");
        var conDescuento = precio.aplicarDescuento(20);
        var enEuros = conDescuento.convertir("EUR", 0.92);
        System.out.println("Original:     " + precio);
        System.out.println("Con 20% dto:  " + conDescuento);
        System.out.println("En EUR:       " + enEuros);

        System.out.println("\n=== Lista mutable vs inmutable ===");
        double[] preciosMutable = {100.0, 200.0, 50.0};
        System.out.println("Antes: " + java.util.Arrays.toString(preciosMutable));
        agregarImpuestoMutable(preciosMutable, 0.19);
        System.out.println("Después (mutable): " + java.util.Arrays.toString(preciosMutable)); // cambiada

        List<Double> preciosOrig = List.of(100.0, 200.0, 50.0);
        List<Double> preciosNuevos = agregarImpuesto(preciosOrig, 0.19);
        System.out.println("Original (inmutable): " + preciosOrig);   // sin cambios
        System.out.println("Con impuesto:         " + preciosNuevos);
    }
}

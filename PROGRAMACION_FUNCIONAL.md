# Programación Funcional — De la Teoría a la Práctica en Java

> **Curso:** PTEC102 Paradigmas de Programación  
> **Lenguaje de referencia:** Java (Stream API, lambdas, records, Optional)  
> **Repositorio:** `functional-programming/java/`

---

## Tabla de Contenidos

1. [Introducción al Paradigma Funcional](#1-introducción-al-paradigma-funcional)
2. [Funciones Puras](#2-funciones-puras)
3. [Inmutabilidad](#3-inmutabilidad)
4. [Funciones de Orden Superior](#4-funciones-de-orden-superior)
5. [Map, Filter y Reduce](#5-map-filter-y-reduce)
6. [Currying y Aplicación Parcial](#6-currying-y-aplicación-parcial)
7. [Composición de Funciones](#7-composición-de-funciones)
8. [Recursión](#8-recursión)
9. [Functores y Mónadas](#9-functores-y-mónadas)
10. [Fuentes y Referencias](#10-fuentes-y-referencias)

---

## 1. Introducción al Paradigma Funcional

### ¿Qué es la Programación Funcional?

La **programación funcional** (FP, del inglés *Functional Programming*) es un paradigma de programación que trata a la computación como la evaluación de **funciones matemáticas**. Se basa en los principios del **cálculo lambda** (λ-cálculo), formalizado por Alonzo Church en la década de 1930.

A diferencia del paradigma imperativo —donde el programador describe *cómo* se ejecuta un programa mediante instrucciones que modifican el estado— la programación funcional describe *qué* se debe calcular, evitando el estado mutable y los efectos secundarios.

### Breve Historia

| Año | Hito |
|-----|------|
| 1930 | Alonzo Church formaliza el λ-cálculo |
| 1958 | John McCarthy crea **LISP**, primer lenguaje funcional |
| 1973 | Aparece **ML** (Meta Language) con inferencia de tipos |
| 1987 | Nace **Haskell**, lenguaje puramente funcional |
| 2003 | **Scala** combina FP con OOP sobre la JVM |
| 2008 | **Clojure** trae FP al ecosistema Java |
| 2014 | **Java 8** incorpora lambdas, Stream API e interfaces funcionales |
| 2016 | **Java 9+** añade `List.of()`, colecciones inmutables |
| 2020+ | **Java 14–21** introduce records, sealed classes, pattern matching |

### Paradigma Imperativo vs. Funcional

```java
// IMPERATIVO: describimos CÓMO hacerlo, mutando estado
List<Integer> pares = new ArrayList<>();
for (int n : numeros) {
    if (n % 2 == 0) {
        pares.add(n);           // mutación
    }
}

// FUNCIONAL: describimos QUÉ queremos
List<Integer> pares = numeros.stream()
                             .filter(n -> n % 2 == 0)
                             .toList();                 // sin mutación
```

### Principios Fundamentales

La programación funcional se apoya en los siguientes pilares:

- **Funciones puras** — misma entrada produce siempre la misma salida, sin efectos secundarios.
- **Inmutabilidad** — los datos no cambian; se crean nuevas versiones.
- **Funciones de orden superior** — las funciones son valores que se pueden pasar y retornar.
- **Composición** — combinar funciones simples para construir comportamiento complejo.
- **Transparencia referencial** — una expresión puede ser reemplazada por su valor sin alterar el comportamiento del programa.
- **Evaluación perezosa** (*lazy evaluation*) — los valores se computan sólo cuando se necesitan.

### Java y la Programación Funcional

Java es un lenguaje **multi-paradigma**: nació orientado a objetos pero desde Java 8 incorporó herramientas funcionales de primera clase:

| Característica | Desde |
|----------------|-------|
| Lambdas (`x -> x * 2`) | Java 8 |
| Interfaces funcionales (`Function<T,R>`, `Predicate<T>`) | Java 8 |
| Stream API (`map`, `filter`, `reduce`) | Java 8 |
| `Optional<T>` (mónada básica) | Java 8 |
| `List.of()` / colecciones inmutables | Java 9 |
| Records (clases de datos inmutables) | Java 16 |
| Sealed classes + pattern matching | Java 17–21 |

---

## 2. Funciones Puras

### Teoría

Una **función pura** cumple dos condiciones:

1. **Determinismo**: dado el mismo input, siempre retorna el mismo output.
2. **Sin efectos secundarios**: no modifica estado externo (variables globales, archivos, red, pantalla, etc.).

La ausencia de efectos secundarios se llama **transparencia referencial**: podemos sustituir una llamada a la función por su valor de retorno sin cambiar el comportamiento del programa.

**¿Por qué importan las funciones puras?**

- Son **predecibles**: fáciles de razonar y depurar.
- Son **testeables**: no requieren mocks ni configuración de entorno.
- Son **paralelizables**: sin estado compartido, no hay condiciones de carrera.
- Son **memorizables** (*memoizable*): el resultado se puede cachear.

### Funciones Puras vs. Impuras

| Criterio | Pura | Impura |
|----------|------|--------|
| Mismo input → mismo output | ✅ | ❌ |
| Sin efectos secundarios | ✅ | ❌ |
| Depende de estado externo | ❌ | ✅ |
| Modifica estado externo | ❌ | ✅ |
| Fácil de testear | ✅ | ❌ |

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/funciones_puras/FuncionesPuras.java`

```java
// ── FUNCIONES PURAS ──────────────────────────────────────────────
/** Pura: sólo depende de sus argumentos, sin efectos secundarios. */
public static int suma(int a, int b) {
    return a + b;
}

public static double areaCirculo(double radio) {
    return Math.PI * radio * radio;
}

/** Pura sobre una lista: retorna nueva lista sin mutar la original. */
public static List<Integer> duplicar(List<Integer> lista) {
    return lista.stream()
                .map(x -> x * 2)
                .toList();   // toList() retorna lista inmutable (Java 16+)
}

// ── FUNCIONES IMPURAS ────────────────────────────────────────────
private static int totalAcumulado = 0;  // estado global mutable

/** Impura: modifica estado externo → efecto secundario. */
public static int sumaImpura(int a, int b) {
    totalAcumulado += a + b;    // efecto secundario
    return a + b;
}

/** Impura: efecto secundario de I/O. */
public static String saludarImpura(String nombre) {
    System.out.println("Hola, " + nombre + "!");  // efecto secundario
    return "Hola, " + nombre + "!";
}
```

**Análisis del ejemplo:**

- `suma(3, 4)` siempre retorna `7`, sin importar cuántas veces se llame.
- `sumaImpura(3, 4)` retorna `7` pero modifica `totalAcumulado` — el estado del programa cambia cada vez.
- `duplicar(List.of(1,2,3))` retorna una **nueva** lista `[2,4,6]`; la original no se modifica.

### Efectos Secundarios Comunes (a evitar)

```java
// ❌ Modifica variable externa
static int contador = 0;
static int incrementar(int x) { contador++; return x + 1; }

// ❌ Depende de estado externo no determinista
static double precioConImpuesto(double precio) {
    double tasa = TasaImpuesto.obtenerActual(); // ← estado externo
    return precio * (1 + tasa);
}

// ❌ Lanza excepciones no controladas (rompe el tipo de retorno)
static int dividir(int a, int b) {
    return a / b; // ← puede lanzar ArithmeticException
}

// ✅ Versión pura con Optional
static Optional<Integer> dividir(int a, int b) {
    return b == 0 ? Optional.empty() : Optional.of(a / b);
}
```

### Ejercicios — Funciones Puras

#### Ejercicio 1 (Análisis)

Lee el siguiente código y responde:

```java
public class Banco {
    private double saldo = 1000.0;

    public double retirar(double monto) {
        if (monto > saldo) throw new IllegalArgumentException("Saldo insuficiente");
        saldo -= monto;
        return saldo;
    }

    public static double calcularInteres(double capital, double tasa, int anios) {
        return capital * Math.pow(1 + tasa, anios);
    }
}
```

**Preguntas:**
1. ¿Cuál de los dos métodos es una función pura? Justifica con los dos criterios de pureza.
2. ¿Qué efecto secundario tiene el método `retirar`?
3. ¿Podría `calcularInteres` producir resultados distintos dado el mismo input? Explica por qué.
4. Reescribe `retirar` como función pura que retorne un nuevo objeto `Cuenta` con el saldo actualizado.

#### Ejercicio 2 (Aplicación)

Implementa las siguientes funciones puras en Java. Ninguna debe modificar sus argumentos ni referenciar estado externo:

```java
// a) Retorna true si todos los elementos de la lista son positivos
public static boolean todosPositivos(List<Integer> lista) { /* ... */ }

// b) Retorna el máximo de una lista (sin usar Math.max ni reduce)
public static int maximo(List<Integer> lista) { /* ... */ }

// c) Retorna una nueva lista con los elementos que cumplen el predicado,
//    transformados por la función f
public static <T, R> List<R> filtrarYTransformar(
        List<T> lista, Predicate<T> pred, Function<T, R> f) { /* ... */ }
```

Escribe al menos dos pruebas para cada función que demuestren el determinismo.

#### Ejercicio 3 (Aplicación)

Una función **memoizable** guarda sus resultados para evitar recomputar. Implementa un método genérico `memoize` que convierta cualquier `Function<T, R>` en su versión memoizada usando un `HashMap`:

```java
public static <T, R> Function<T, R> memoize(Function<T, R> fn) {
    Map<T, R> cache = new HashMap<>();
    return input -> cache.computeIfAbsent(input, fn);
}
```

a) ¿Por qué la memoización sólo es válida para funciones puras?  
b) Aplica `memoize` a una función que calcule el n-ésimo número primo y compara tiempos de ejecución con y sin caché para `n = 10_000`.

---

## 3. Inmutabilidad

### Teoría

En programación funcional, los datos son **inmutables**: una vez creado un valor, no puede cambiar. En lugar de modificar un objeto, se crea uno nuevo con los cambios deseados.

**Ventajas de la inmutabilidad:**

- **Seguridad en hilos** (*thread safety*): sin escrituras concurrentes, no hay condiciones de carrera.
- **Historial de estado**: cada versión anterior del dato sigue existiendo.
- **Razonamiento local**: el valor de una variable no cambia sorpresivamente.
- **Igualdad estructural**: dos objetos con los mismos datos son iguales.

### Inmutabilidad en Java

Java ofrece varias herramientas para trabajar con datos inmutables:

| Herramienta | Qué hace |
|-------------|----------|
| `final` | Prohíbe reasignación de la referencia |
| `record` | Clase de datos inmutable: campos `final`, sin setters |
| `List.of()` | Lista no modificable (lanza excepción si se intenta cambiar) |
| `Collections.unmodifiableList()` | Vista no modificable de una lista existente |
| `String` | Inmutable por diseño en Java |

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/inmutabilidad/Inmutabilidad.java`

```java
// ── RECORD: inmutable por definición ─────────────────────────────
record Punto(double x, double y) {
    /** En lugar de mutar, retorna un NUEVO Punto desplazado. */
    Punto mover(double dx, double dy) {
        return new Punto(x + dx, y + dy);
    }
}

// ── RECORD con validación ─────────────────────────────────────────
record Precio(double valor, String moneda) {
    Precio {   // Compact constructor con validación
        if (valor < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        moneda = moneda.toUpperCase();
    }

    Precio aplicarDescuento(double porcentaje) {
        return new Precio(valor * (1 - porcentaje / 100), moneda);
    }
}

// ── Transformación funcional de listas ───────────────────────────
// ❌ Mutable — modifica el array original
static void agregarImpuestoMutable(double[] precios, double tasa) {
    for (int i = 0; i < precios.length; i++) {
        precios[i] *= (1 + tasa);  // MUTA
    }
}

// ✅ Inmutable — retorna nueva lista
static List<Double> agregarImpuesto(List<Double> precios, double tasa) {
    return precios.stream()
                  .map(p -> p * (1 + tasa))
                  .toList();   // nueva lista, original intacta
}
```

**Trazando la ejecución:**

```java
var p1 = new Punto(0, 0);
var p2 = p1.mover(3, 4);
// p1 sigue siendo Punto[x=0.0, y=0.0]
// p2 es un nuevo objeto Punto[x=3.0, y=4.0]

var precio = new Precio(1000, "usd");
var conDescuento = precio.aplicarDescuento(20);
// precio.valor == 1000  (sin cambios)
// conDescuento.valor == 800
```

### El Problema del Estado Compartido

```java
// ❌ Estado compartido mutable — peligroso en concurrencia
class Carrito {
    private List<String> items = new ArrayList<>();
    public void agregar(String item) { items.add(item); }
    public List<String> getItems() { return items; }  // ← referencia mutable expuesta
}

// ✅ Estado compartido inmutable — seguro
record Carrito(List<String> items) {
    Carrito() { this(List.of()); }

    Carrito agregar(String item) {
        var nuevos = new ArrayList<>(items);
        nuevos.add(item);
        return new Carrito(List.copyOf(nuevos));  // nuevo carrito, lista inmutable
    }
}
```

### Ejercicios — Inmutabilidad

#### Ejercicio 4 (Análisis)

Lee el siguiente código:

```java
record Temperatura(double valor, String unidad) {
    Temperatura aFahrenheit() {
        if (unidad.equals("C")) return new Temperatura(valor * 9.0/5.0 + 32, "F");
        return this;
    }

    Temperatura aCelsius() {
        if (unidad.equals("F")) return new Temperatura((valor - 32) * 5.0/9.0, "C");
        return this;
    }
}

public class Main {
    public static void main(String[] args) {
        var t1 = new Temperatura(100, "C");
        var t2 = t1.aFahrenheit();
        var t3 = t2.aCelsius();

        System.out.println(t1);  // (A)
        System.out.println(t2);  // (B)
        System.out.println(t3);  // (C)
        System.out.println(t1 == t2);  // (D)
        System.out.println(t1.equals(t3));  // (E)
    }
}
```

**Preguntas:**
1. ¿Qué imprime cada línea (A) a (E)? Justifica cada una.
2. ¿Por qué `t1 == t2` da `false`?
3. ¿Por qué `t1.equals(t3)` puede dar `true` o `false`? (Pista: considera aritmética de punto flotante.)
4. ¿Es `Temperatura` un record inmutable? ¿Tiene alguna vulnerabilidad de inmutabilidad?

#### Ejercicio 5 (Aplicación)

Modela un sistema de gestión de inventario **completamente inmutable** usando records:

```java
record Producto(String id, String nombre, int stock, double precio) {}
record Inventario(List<Producto> productos) {}
```

Implementa como métodos del record `Inventario`:
- `agregarProducto(Producto p)` → retorna nuevo `Inventario`
- `actualizarStock(String id, int nuevoStock)` → retorna nuevo `Inventario`
- `aplicarDescuentoGeneral(double porcentaje)` → retorna nuevo `Inventario`
- `buscar(String id)` → retorna `Optional<Producto>`

**Restricción:** Ningún método puede modificar `this.productos`. Usa `Stream` y `List.copyOf`.

#### Ejercicio 6 (Aplicación)

Java `String` es inmutable. El siguiente código tiene un error conceptual:

```java
String resultado = "";
for (String palabra : List.of("Hola", " ", "mundo", "!")) {
    resultado += palabra;  // ¿Cuántos objetos String se crean?
}
```

a) ¿Cuántos objetos `String` se crean en total? Traza la ejecución.  
b) ¿Por qué esto puede ser ineficiente con miles de palabras?  
c) Reescríbelo de forma funcional con `Stream.reduce` o `Collectors.joining`.  
d) ¿La versión funcional crea menos objetos? Explica.

---

## 4. Funciones de Orden Superior

### Teoría

Una **función de orden superior** (HOF, *Higher-Order Function*) es una función que:

- **Recibe** una o más funciones como argumentos, **y/o**
- **Retorna** una función como resultado.

Las HOFs son el mecanismo central que permite la composición y reutilización en programación funcional.

### Interfaces Funcionales en Java

Java representa funciones como objetos a través de **interfaces funcionales** (interfaces con un solo método abstracto, anotadas con `@FunctionalInterface`):

| Interfaz | Firma | Descripción |
|----------|-------|-------------|
| `Function<T, R>` | `R apply(T t)` | Transforma T en R |
| `BiFunction<T, U, R>` | `R apply(T t, U u)` | Transforma T y U en R |
| `UnaryOperator<T>` | `T apply(T t)` | Caso especial: T → T |
| `Predicate<T>` | `boolean test(T t)` | Condición sobre T |
| `Consumer<T>` | `void accept(T t)` | Consume T sin retorno |
| `Supplier<T>` | `T get()` | Produce T sin input |
| `Comparator<T>` | `int compare(T o1, T o2)` | Ordena T |

### Lambdas y Referencias a Métodos

```java
// Lambda: implementación anónima de una interfaz funcional
Function<Integer, Integer> doble   = x -> x * 2;
Predicate<String>          noVacio = s -> !s.isBlank();
Consumer<String>           imprimir = System.out::println; // method reference

// Method references — cuatro formas:
// 1. Método estático
Function<String, Integer> parsear = Integer::parseInt;

// 2. Método de instancia (sobre argumento)
Function<String, String> mayus = String::toUpperCase;

// 3. Método de instancia (sobre instancia específica)
String prefijo = "INFO: ";
UnaryOperator<String> conPrefijo = prefijo::concat;

// 4. Constructor
Function<String, StringBuilder> crear = StringBuilder::new;
```

### Ejemplo: HOFs como Estrategia

```java
// Una función de orden superior que recibe un Comparator como estrategia
public static <T> List<T> ordenar(List<T> lista, Comparator<T> criterio) {
    return lista.stream()
                .sorted(criterio)
                .toList();
}

// Uso con diferentes estrategias
var personas = List.of("Carlos", "Ana", "Beatriz", "David");

var alfabet = ordenar(personas, Comparator.naturalOrder());
var porLong = ordenar(personas, Comparator.comparingInt(String::length));
var inverso = ordenar(personas, Comparator.reverseOrder());
```

### HOFs que Retornan Funciones

```java
// Fábrica de validadores — retorna una función
public static Predicate<Double> enRango(double min, double max) {
    return valor -> valor >= min && valor <= max;
}

// Fábrica de transformadores de texto
public static Function<String, String> agregarPrefijo(String prefijo) {
    return texto -> prefijo + texto;
}

// Uso
Predicate<Double> precioValido     = enRango(0, 10_000);
Predicate<Double> temperaturaValida = enRango(-273.15, 1_000_000);

Function<String, String> logInfo  = agregarPrefijo("[INFO] ");
Function<String, String> logError = agregarPrefijo("[ERROR] ");
```

### Combinación de Predicados

```java
Predicate<Integer> esPar     = n -> n % 2 == 0;
Predicate<Integer> esMayorDe5 = n -> n > 5;

Predicate<Integer> parYMayorDe5 = esPar.and(esMayorDe5);
Predicate<Integer> parOMayorDe5 = esPar.or(esMayorDe5);
Predicate<Integer> noEsPar      = esPar.negate();

List.of(1, 2, 6, 7, 8, 10).stream()
    .filter(parYMayorDe5)
    .forEach(System.out::println);  // 6, 8, 10
```

### Ejercicios — Funciones de Orden Superior

#### Ejercicio 7 (Análisis)

Lee el siguiente código:

```java
public static <T, R> List<R> transformarSiFumple(
        List<T> lista,
        Predicate<T> condicion,
        Function<T, R> transformar,
        R valorPorDefecto) {

    return lista.stream()
                .map(elem -> condicion.test(elem)
                             ? transformar.apply(elem)
                             : valorPorDefecto)
                .toList();
}

// Llamada:
var resultado = transformarSiFumple(
    List.of(4, -2, 9, 0, 7),
    n -> n > 0,
    n -> n * n,
    -1
);
```

**Preguntas:**
1. ¿Qué contiene `resultado`?
2. ¿Cuántos argumentos de tipo función recibe `transformarSiFumple`?
3. Reescribe la llamada usando method references en lugar de lambdas donde sea posible.
4. ¿Es esta función pura? Justifica.

#### Ejercicio 8 (Aplicación)

Implementa las siguientes HOFs genéricas **sin usar la Stream API**:

```java
// a) Aplica f a cada elemento y retorna nueva lista
public static <T, R> List<R> miMap(List<T> lista, Function<T, R> f) { /* ... */ }

// b) Retorna nueva lista con elementos que cumplen p
public static <T> List<T> miFilter(List<T> lista, Predicate<T> p) { /* ... */ }

// c) Combina todos los elementos con un acumulador
public static <T, R> R miReduce(List<T> lista, R inicial, BiFunction<R, T, R> combinador) { /* ... */ }
```

Luego verifica que `miMap` + `miFilter` + `miReduce` reproduzcan el comportamiento de la Stream API para este pipeline:

```java
// Suma de cuadrados de números impares en [1..10]
int resultado = List.of(1,2,3,4,5,6,7,8,9,10).stream()
    .filter(n -> n % 2 != 0)
    .map(n -> n * n)
    .reduce(0, Integer::sum);
```

#### Ejercicio 9 (Aplicación)

Implementa un mini-framework de **pipeline de validación** usando HOFs:

```java
record ResultadoValidacion(boolean valido, List<String> errores) {}

// Una Regla es una HOF: recibe un valor y retorna un mensaje de error (vacío si válido)
@FunctionalInterface
interface Regla<T> {
    Optional<String> validar(T valor);
}

// Combina múltiples reglas en una sola validación
public static <T> ResultadoValidacion validar(T valor, List<Regla<T>> reglas) { /* ... */ }
```

Implementa reglas para validar un `String` como nombre de usuario:
- No puede estar vacío
- Longitud entre 3 y 20 caracteres
- Solo letras, números y guión bajo
- No puede comenzar con número

---

## 5. Map, Filter y Reduce

### Teoría

**Map**, **Filter** y **Reduce** son las tres operaciones fundamentales de la programación funcional sobre colecciones. Juntas permiten expresar transformaciones de datos de forma declarativa y componible.

| Operación | Descripción | Entrada → Salida |
|-----------|-------------|-----------------|
| **map** | Transforma cada elemento | `List<A>` → `List<B>` |
| **filter** | Selecciona elementos que cumplen una condición | `List<A>` → `List<A>` |
| **reduce** | Combina todos los elementos en un solo valor | `List<A>` → `B` |

### La Stream API de Java

Java implementa estas operaciones a través de `java.util.stream.Stream`. Las operaciones se clasifican en:

- **Intermedias** (retornan un Stream): `map`, `filter`, `flatMap`, `sorted`, `distinct`, `limit`, `skip`
- **Terminales** (consumen el Stream): `reduce`, `collect`, `forEach`, `count`, `findFirst`, `anyMatch`

Un Stream es **lazy**: las operaciones intermedias no se ejecutan hasta que se llama a una terminal.

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/streams/MapFilterReduce.java`

```java
record Producto(String nombre, double precio, String categoria) {}

var productos = List.of(
    new Producto("Laptop",  1200, "tecnologia"),
    new Producto("Libro",   25,   "educacion"),
    new Producto("Monitor", 350,  "tecnologia"),
    new Producto("Silla",   180,  "muebles"),
    new Producto("Teclado", 80,   "tecnologia")
);

// map: transforma
var nombres = productos.stream()
                       .map(Producto::nombre)
                       .toList();
// ["Laptop", "Libro", "Monitor", "Silla", "Teclado"]

// filter: selecciona
var techBarata = productos.stream()
                          .filter(p -> p.categoria().equals("tecnologia"))
                          .filter(p -> p.precio() < 500)
                          .toList();

// reduce: acumula
double totalTech = productos.stream()
                            .filter(p -> p.categoria().equals("tecnologia"))
                            .mapToDouble(Producto::precio)
                            .sum();  // equivale a reduce(0.0, Double::sum)

// Pipeline completo: nombres en mayúsculas de tech < $500
var resultado = productos.stream()
                         .filter(p -> p.categoria().equals("tecnologia"))
                         .filter(p -> p.precio() < 500)
                         .map(p -> p.nombre().toUpperCase())
                         .toList();
// ["MONITOR", "TECLADO"]
```

### flatMap: Aplanar Streams Anidados

```java
// Cada departamento tiene una lista de empleados
record Empleado(String nombre, String rol) {}
record Departamento(String nombre, List<Empleado> empleados) {}

var departamentos = List.of(
    new Departamento("IT", List.of(new Empleado("Ana", "dev"), new Empleado("Luis", "QA"))),
    new Departamento("HR", List.of(new Empleado("Mia", "recruiter")))
);

// flatMap aplana List<List<Empleado>> en un solo Stream<Empleado>
var todosEmpleados = departamentos.stream()
                                  .flatMap(d -> d.empleados().stream())
                                  .toList();
// [Empleado[nombre=Ana,...], Empleado[nombre=Luis,...], Empleado[nombre=Mia,...]]
```

### Collectors: Materializando Resultados

```java
// Agrupar por categoría
Map<String, List<Producto>> porCategoria = productos.stream()
    .collect(Collectors.groupingBy(Producto::categoria));

// Contar por categoría
Map<String, Long> conteo = productos.stream()
    .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()));

// Unir nombres con coma
String listaNames = productos.stream()
    .map(Producto::nombre)
    .collect(Collectors.joining(", "));
```

### Ejercicios — Map, Filter y Reduce

#### Ejercicio 10 (Análisis)

Lee el siguiente pipeline y responde:

```java
var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

var resultado = numeros.stream()
    .filter(n -> {
        System.out.println("filter: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("map: " + n);
        return n * n;
    })
    .findFirst();
```

**Preguntas:**
1. ¿Cuántas veces se ejecuta `filter`? ¿Y `map`? (Pista: recuerda que Streams son lazy.)
2. ¿Qué contiene `resultado`?
3. Si cambias `findFirst()` por `toList()`, ¿cuántas veces se ejecuta `filter` y `map`?
4. Explica el concepto de **evaluación perezosa** con este ejemplo.

#### Ejercicio 11 (Aplicación)

Dada la siguiente lista de transacciones bancarias:

```java
record Transaccion(String id, String tipo, double monto, String moneda, LocalDate fecha) {}

var transacciones = List.of(
    new Transaccion("T1", "credito",  500.0, "CLP", LocalDate.of(2024, 1, 15)),
    new Transaccion("T2", "debito",   200.0, "CLP", LocalDate.of(2024, 1, 20)),
    new Transaccion("T3", "credito", 1500.0, "USD", LocalDate.of(2024, 2, 5)),
    new Transaccion("T4", "debito",   800.0, "CLP", LocalDate.of(2024, 2, 10)),
    new Transaccion("T5", "credito",  300.0, "CLP", LocalDate.of(2024, 3, 1))
);
```

Usando **únicamente Stream API** (sin bucles), calcula:

a) El monto total de créditos en CLP.  
b) La transacción de mayor monto (cualquier moneda).  
c) Las transacciones de enero de 2024, ordenadas por monto descendente.  
d) Un `Map<String, Double>` con la suma total por tipo (`"credito"` → X, `"debito"` → Y).  
e) `true` si existe algún débito mayor a $1000.

#### Ejercicio 12 (Aplicación)

Implementa un **analizador de texto** usando pipeline de streams:

```java
String texto = "La programación funcional es elegante y expresiva. " +
               "Los streams de Java permiten escribir código funcional. " +
               "La programación funcional evita los efectos secundarios.";
```

Usando streams, obtén:
a) Frecuencia de cada palabra (como `Map<String, Long>`), ignorando mayúsculas y puntuación.  
b) Las 5 palabras más frecuentes (excluyendo palabras de menos de 4 caracteres).  
c) El promedio de caracteres por palabra.  
d) `true` si el texto contiene alguna palabra de más de 10 caracteres.

---

## 6. Currying y Aplicación Parcial

### Teoría

**Currying** es la transformación de una función de múltiples argumentos en una secuencia de funciones que toman un argumento cada una:

```
f(a, b, c)  →  f(a)(b)(c)
```

El nombre proviene del matemático Haskell Curry, aunque la técnica fue introducida por Moses Schönfinkel.

**Aplicación parcial** es relacionada pero distinta: consiste en fijar algunos argumentos de una función, obteniendo una nueva función que espera los argumentos restantes.

```
f(a, b, c) con a fijo  →  g(b, c)
```

### ¿Para qué sirven?

- **Especialización**: crear funciones específicas a partir de funciones generales.
- **Reutilización**: la misma función base genera múltiples variantes.
- **Composición**: facilita construir pipelines donde cada paso espera un argumento.

### Representación en Java

Java no tiene sintaxis nativa para currying, pero se implementa con `Function<A, Function<B, C>>`:

```java
// Función normal de 2 argumentos
BiFunction<Integer, Integer, Integer> suma = (a, b) -> a + b;

// Versión curried: Function<A, Function<B, C>>
Function<Integer, Function<Integer, Integer>> sumaCurried = a -> b -> a + b;

// Aplicación total:
sumaCurried.apply(3).apply(4)  // → 7

// Aplicación parcial (fijamos el primero):
Function<Integer, Integer> suma5 = sumaCurried.apply(5);
suma5.apply(8)   // → 13
suma5.apply(12)  // → 17
```

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/currying/Currying.java`

```java
// Currying de 3 argumentos
static Function<Double, Function<Double, Function<Double, Double>>> volumen =
    largo -> ancho -> alto -> largo * ancho * alto;

// Aplicación parcial: "congelamos" largo y ancho
var volCaja5x5 = volumen.apply(5.0).apply(5.0);  // espera solo el alto
System.out.println(volCaja5x5.apply(10.0));  // 250.0
System.out.println(volCaja5x5.apply(3.0));   // 75.0

// Utilidad genérica: convertir BiFunction → curried
static <A, B, C> Function<A, Function<B, C>> curry(BiFunction<A, B, C> f) {
    return a -> b -> f.apply(a, b);
}

// Aplicación parcial fijando el primer argumento
static <A, B, C> Function<B, C> partial(BiFunction<A, B, C> f, A a) {
    return b -> f.apply(a, b);
}

// Logger especializado con aplicación parcial
BiFunction<String, String, String> log = (nivel, msg) -> "[" + nivel + "] " + msg;
Function<String, String> logInfo  = partial(log, "INFO");
Function<String, String> logError = partial(log, "ERROR");

logInfo.apply("Servidor iniciado");   // [INFO] Servidor iniciado
logError.apply("Archivo no encontrado"); // [ERROR] Archivo no encontrado
```

### Currying vs. Aplicación Parcial: Diferencias

| Característica | Currying | Aplicación Parcial |
|----------------|----------|-------------------|
| Argumentos | Uno a la vez siempre | Uno o más a la vez |
| Resultado | Siempre otra función (hasta el último) | Puede ser valor o función |
| Aridad | Reduce a 1 arg cada vez | Reduce en N args |
| Uso típico | Composición funcional | Especialización |

### Ejercicios — Currying y Aplicación Parcial

#### Ejercicio 13 (Análisis)

Traza la ejecución del siguiente código y responde:

```java
Function<Integer, Function<Integer, Function<Integer, Integer>>> entre =
    a -> b -> c -> (c - a) / b;

var cadaPas5 = entre.apply(0).apply(5);
var cadaPas3 = entre.apply(10).apply(3);

System.out.println(cadaPas5.apply(20));   // (A)
System.out.println(cadaPas5.apply(100));  // (B)
System.out.println(cadaPas3.apply(25));   // (C)
System.out.println(entre.apply(0).apply(2).apply(10)); // (D)
```

**Preguntas:**
1. ¿Qué imprime cada línea? Muestra los cálculos.
2. ¿Qué representa matemáticamente `entre`? (Pista: piensa en el número de "pasos" para ir de `a` a `c` con incrementos de `b`.)
3. ¿Cuántas funciones distintas se pueden generar a partir de `entre` fijando solo el primer argumento? ¿Y fijando los dos primeros?

#### Ejercicio 14 (Aplicación)

Implementa un sistema de **formateo de textos** usando currying:

```java
// Crea una función curried que aplica un formato a un texto
Function<String, Function<String, String>> envolverEn = ???;

// Debe permitir:
var enNegritas     = envolverEn.apply("**");
var enCodigo       = envolverEn.apply("`");
var enComillas     = envolverEn.apply("\"");

System.out.println(enNegritas.apply("importante"));  // **importante**
System.out.println(enCodigo.apply("System.out"));    // `System.out`
System.out.println(enComillas.apply("hola mundo"));  // "hola mundo"
```

Luego, crea un curried de 3 argumentos:

```java
// Rodea el texto con prefijo y sufijo distintos
Function<String, Function<String, Function<String, String>>> rodear = ???;

var enHTML  = rodear.apply("<b>").apply("</b>");
var enMarkdown = rodear.apply("**").apply("**");
```

#### Ejercicio 15 (Aplicación)

Implementa un **generador de consultas SQL** usando aplicación parcial:

```java
// Función base
static String consulta(String tabla, String condicion, String orden, int limite) {
    return String.format("SELECT * FROM %s WHERE %s ORDER BY %s LIMIT %d",
                         tabla, condicion, orden, limite);
}
```

a) Crea una versión curried de `consulta`.  
b) Genera funciones especializadas: `consultaUsuarios`, `consultaProductos` (tabla fija).  
c) Genera una función `buscarActivos` (tabla + condición fijas) que sólo recibe orden y límite.  
d) ¿Qué ventaja tiene este enfoque frente a sobrecargar `consulta` con diferentes firmas?

---

## 7. Composición de Funciones

### Teoría

La **composición de funciones** es el proceso de combinar dos o más funciones para producir una nueva función. Si `f: A → B` y `g: B → C`, entonces su composición `g ∘ f: A → C` se define como:

```
(g ∘ f)(x) = g(f(x))
```

En programación funcional, la composición permite construir funciones complejas a partir de funciones simples y reutilizables, siguiendo el principio de **separación de responsabilidades**.

### Dos Estilos de Composición

| Estilo | Notación | Orden | Intuición |
|--------|----------|-------|-----------|
| **Pipe** | `f ▷ g` | f primero, luego g | Como una tubería (Unix: `f | g`) |
| **Compose** | `g ∘ f` | g envuelve f | Notación matemática clásica |

Ambos producen el mismo resultado; difieren en el orden en que se especifican las funciones.

### Composición en Java

```java
Function<Integer, Integer> doble  = x -> x * 2;
Function<Integer, Integer> sumar1 = x -> x + 1;

// andThen: pipe —  doble LUEGO sumar1
Function<Integer, Integer> pipe = doble.andThen(sumar1);
// pipe(5) = sumar1(doble(5)) = sumar1(10) = 11

// compose: matemático — sumar1 DENTRO de doble
Function<Integer, Integer> comp = doble.compose(sumar1);
// comp(5) = doble(sumar1(5)) = doble(6) = 12
```

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/composicion/Composicion.java`

```java
// Funciones atómicas de procesamiento de texto
static UnaryOperator<String> limpiar    = String::strip;
static UnaryOperator<String> normalizar = String::toLowerCase;
static UnaryOperator<String> capitalizar = s ->
    Arrays.stream(s.split(" "))
          .map(w -> w.isEmpty() ? w : Character.toUpperCase(w.charAt(0)) + w.substring(1))
          .collect(Collectors.joining(" "));

// pipe() encadena funciones de izquierda a derecha
@SafeVarargs
static <T> Function<T, T> pipe(UnaryOperator<T>... funcs) {
    return x -> {
        T result = x;
        for (var f : funcs) result = f.apply(result);
        return result;
    };
}

// Pipeline de formateo compuesto desde funciones simples
var formatear = pipe(limpiar, normalizar, capitalizar);

formatear.apply("  juan  ");  // "Juan"
formatear.apply("MARÍA JOSÉ!"); // "María José"

// Composición más compleja
Function<String, String> saludoFormal =
    ((Function<String, String>) formatear::apply)
        .andThen(n -> "Estimado/a " + n)
        .andThen(n -> n + ",");

saludoFormal.apply("  ana  ");  // "Estimada/o Ana,"
```

### Composición de Predicados (Lógica booleana)

```java
Predicate<String> noVacio    = s -> !s.isBlank();
Predicate<String> longMin3   = s -> s.length() >= 3;
Predicate<String> soloLetras = s -> s.matches("[a-zA-ZáéíóúÁÉÍÓÚ ]+");

// Composición AND con method chaining
Predicate<String> esNombreValido = noVacio
    .and(longMin3)
    .and(soloLetras);

// Composición OR
Predicate<String> esBlancoOLargo = s -> s.isBlank();
esBlancoOLargo = esBlancoOLargo.or(s -> s.length() > 100);
```

### Ejercicios — Composición de Funciones

#### Ejercicio 16 (Análisis)

Dado el siguiente código:

```java
Function<Integer, Integer> f = x -> x + 3;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = x -> x - 1;

Function<Integer, Integer> A = f.andThen(g).andThen(h);
Function<Integer, Integer> B = h.compose(g).compose(f);
Function<Integer, Integer> C = f.andThen(g.compose(h));
Function<Integer, Integer> D = g.andThen(f).compose(h);
```

**Preguntas:**
1. Para `x = 4`, calcula `A(4)`, `B(4)`, `C(4)`, `D(4)`. Muestra los pasos.
2. ¿Son `A` y `B` equivalentes? ¿Por qué?
3. ¿Son `A` y `C` equivalentes? Explica.
4. Escribe la expresión matemática de cada función (ej: `A(x) = 2(x+3) - 1`).

#### Ejercicio 17 (Aplicación)

Construye un pipeline de **validación y transformación de datos de usuario** usando composición:

```java
record DatosUsuario(String nombre, String email, String telefono, int edad) {}
```

Implementa como funciones componibles:

```java
// Transformadores (UnaryOperator<String>)
UnaryOperator<String> normalizar = ???;   // minúsculas + strip
UnaryOperator<String> capitalizar = ???;  // Primera letra mayúscula por palabra
UnaryOperator<String> formatearTel = ???; // "+56XXXXXXXXX" → "(+56) XXX-XXXX"

// Validadores como predicados componibles
Predicate<String> emailValido = ???;      // contiene @ y dominio
Predicate<String> telefonoValido = ???;   // empieza con +56 y tiene 11 dígitos
Predicate<Integer> edadValida = ???;      // entre 18 y 120
```

Compón estos elementos en una función `procesarUsuario(DatosUsuario) → Optional<DatosUsuario>` que:
1. Normalice y capitalice el nombre.
2. Valide el email y el teléfono.
3. Valide la edad.
4. Retorne `Optional.empty()` si alguna validación falla.

#### Ejercicio 18 (Aplicación)

Implementa un **motor de reglas de negocio** basado en composición de funciones para un sistema de descuentos:

```java
record Orden(String clienteId, double total, boolean esNuevo, int diasSinCompra) {}

// Cada regla de descuento es un UnaryOperator<Double>
// que recibe el descuento actual y retorna el nuevo descuento acumulado
```

Crea reglas:
- `descuentoNuevo`: 10% si el cliente es nuevo.
- `descuentoFidelidad`: 5% si lleva más de 30 días sin comprar.
- `descuentoVolumen`: 15% si el total supera $500.
- `descuentoMaximo`: el descuento final no puede superar 25%.

Combínalas con `pipe` para calcular el descuento total de una orden.

---

## 8. Recursión

### Teoría

La **recursión** es el mecanismo por el cual una función se llama a sí misma para resolver un problema. En programación funcional, la recursión reemplaza a los bucles: en lugar de mutar un contador, se avanza hacia el caso base en cada llamada.

**Estructura de una función recursiva:**

1. **Caso base**: condición de parada (evita la recursión infinita).
2. **Caso recursivo**: la función se llama con una versión simplificada del problema.

### Tail-Call Optimization (TCO)

Un problema de la recursión es el **desbordamiento de pila** (*stack overflow*) para entradas grandes: cada llamada recursiva agrega un *stack frame*. La **recursión de cola** (*tail recursion*) coloca la llamada recursiva como **última operación** de la función, permitiendo al compilador reutilizar el mismo frame.

```java
// Recursión normal: hay pendientes (multiplicación) después de la llamada recursiva
static long factorial(int n) {
    if (n <= 1) return 1L;
    return n * factorial(n - 1);  // ← multiplicación PENDIENTE tras la llamada
}

// Recursión de cola: la llamada recursiva ES la última operación
static long factorialTail(int n, long acc) {
    if (n <= 1) return acc;
    return factorialTail(n - 1, n * acc);  // ← nada pendiente
}
```

> ⚠️ Java **no optimiza** tail-calls automáticamente. Para listas grandes, usar `Stream.iterate()` o `trampoline` pattern.

### Ejemplo en Java

> Archivo: `java/src/main/java/com/paradigmas/fp/recursion/Recursion.java`

```java
// Fibonacci con memoización (evita recalcular subproblemas)
private static final Map<Integer, Long> memo = new HashMap<>();

static long fibonacci(int n) {
    if (n <= 1) return n;
    return memo.computeIfAbsent(n, k -> fibonacci(k - 1) + fibonacci(k - 2));
}

// Fibonacci funcional con Stream.iterate (sin recursión explícita)
static List<Long> fibonacciStream(int cantidad) {
    record Par(long a, long b) {}
    return Stream.iterate(new Par(0L, 1L), p -> new Par(p.b(), p.a() + p.b()))
                 .limit(cantidad)
                 .map(Par::a)
                 .toList();
}

// Quicksort funcional — puro y sin mutación
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
```

### Comparación: Bucle vs. Recursión

```java
// Imperativo con bucle: suma de 1 a n
static long sumaImperativa(int n) {
    long total = 0;
    for (int i = 1; i <= n; i++) total += i;  // mutación de total e i
    return total;
}

// Funcional con recursión: suma de 1 a n
static long sumaRecursiva(int n) {
    if (n <= 0) return 0;
    return n + sumaRecursiva(n - 1);
}

// Funcional con Stream (más idiomático en Java)
static long sumaStream(int n) {
    return IntStream.rangeClosed(1, n).asLongStream().sum();
}
```

### Ejercicios — Recursión

#### Ejercicio 19 (Análisis)

Analiza el siguiente código:

```java
static int misterio(List<Integer> lista) {
    if (lista.isEmpty()) return 0;
    if (lista.size() == 1) return lista.getFirst();
    int mitad = lista.size() / 2;
    var izq = lista.subList(0, mitad);
    var der = lista.subList(mitad, lista.size());
    return Math.max(misterio(izq), misterio(der));
}
```

**Preguntas:**
1. ¿Qué calcula `misterio`? Traza la ejecución para `[3, 7, 1, 9, 4]`.
2. ¿Cuántas llamadas recursivas se realizan para una lista de `n` elementos? (Expresa en O-notation.)
3. ¿Es este algoritmo una recursión de cola? Justifica.
4. Reescribe `misterio` de forma iterativa y de forma funcional con `Stream`.

#### Ejercicio 20 (Aplicación)

Implementa las siguientes funciones **exclusivamente de forma recursiva** (sin bucles):

```java
// a) Reversa de una lista
static <T> List<T> revertir(List<T> lista) { /* ... */ }

// b) Verifica si una lista es palíndromo
static <T> boolean esPalindromo(List<T> lista) { /* ... */ }

// c) Aplana una lista de listas (un nivel de profundidad)
static <T> List<T> aplanar(List<List<T>> listas) { /* ... */ }

// d) Número de Collatz: pasos hasta llegar a 1
//    Si n es par: n/2. Si n es impar: 3n+1
static int collatz(int n) { /* ... */ }
```

Para `collatz`, ¿cuál es el máximo `n` con el que Java puede ejecutarlo sin `StackOverflowError`? Experimenta y explica.

#### Ejercicio 21 (Aplicación)

Implementa **búsqueda binaria recursiva** en una lista ordenada:

```java
static <T extends Comparable<T>> Optional<Integer> busquedaBinaria(
        List<T> lista, T objetivo) { /* ... */ }
```

a) Traza la ejecución para `lista = [1, 3, 5, 7, 9, 11, 13]` y `objetivo = 7`.  
b) Convierte la función a **tail-recursive** añadiendo parámetros `inicio` y `fin`.  
c) Implementa la misma lógica con `Stream.iterate()`.  
d) ¿Cuál de las tres implementaciones es más eficiente en Java? ¿Por qué?

---

## 9. Functores y Mónadas

### Teoría

**Functor** y **Mónada** son conceptos de la **teoría de categorías** matemáticas aplicados a la programación funcional. Son patrones para encapsular valores y componer operaciones sobre ellos de forma segura.

#### Functor

Un **functor** es un contenedor que puede ser "mapeado" con una función, preservando su estructura. La operación clave es `map`:

```
map(f, Functor<A>)  →  Functor<B>
```

En Java, `Optional<T>` y `Stream<T>` son functores:

```java
Optional<Integer> num = Optional.of(5);
Optional<Integer> doble = num.map(x -> x * 2);  // Optional[10]

Optional<String> s = Optional.<Integer>empty().map(Object::toString);  // Optional.empty
```

#### Mónada

Una **mónada** es un functor que además soporta `flatMap` (también llamado `bind` o `>>=`). `flatMap` evita el anidamiento de contenedores:

```
flatMap(f, Mónada<A>)  →  Mónada<B>    donde f: A → Mónada<B>
```

La diferencia entre `map` y `flatMap`:

```java
// Con map: puede anidar Optional dentro de Optional
Optional<Optional<String>> problematico = Optional.of("hola")
    .map(s -> Optional.of(s.toUpperCase()));  // Optional[Optional[HOLA]]

// Con flatMap: aplana el resultado
Optional<String> correcto = Optional.of("hola")
    .flatMap(s -> Optional.of(s.toUpperCase()));  // Optional[HOLA]
```

### `Optional<T>` — La Mónada de Java

`Optional<T>` encapsula un valor que puede o no estar presente, eliminando los `NullPointerException` a través del tipo:

```java
// Sin Optional — propenso a NullPointerException
Usuario usuario = bd.buscarUsuario(id);      // puede ser null
String ciudad = usuario.getDireccion().getCiudad();  // ¡NPE si alguno es null!

// Con Optional — seguro y componible
Optional<String> ciudad = bd.buscarUsuario(id)
    .flatMap(Usuario::getDireccion)
    .map(Direccion::getCiudad);
```

### Maybe y Result — Mónadas Personalizadas

> Archivos: `java/src/main/java/com/paradigmas/fp/monadas/`

```java
// Maybe<A>: dos casos — Just(valor) o Nothing
Maybe<String> ciudad = buscarUsuario(1)
    .flatMap(MonadasDemo::obtenerDireccion)
    .flatMap(MonadasDemo::obtenerCiudad);

// Pattern matching para manejar ambos casos (Java 21+)
String mensaje = switch (ciudad) {
    case Maybe.Just<String> j -> "Ciudad: " + j.value();
    case Maybe.Nothing<String> n -> "Sin ciudad";
};

// Result<T>: Ok(valor) o Err(mensaje) — para operaciones que pueden fallar
Result<Double> resultado = dividir(100, 4)
    .flatMap(MonadasDemo::raizCuadrada);
// Ok(5.0)

Result<Double> error = dividir(100, 0)
    .flatMap(MonadasDemo::raizCuadrada);
// Err("División por cero")
```

### Las Leyes de las Mónadas

Toda mónada debe cumplir tres leyes:

1. **Identidad izquierda**: `of(a).flatMap(f)` ≡ `f(a)`
2. **Identidad derecha**: `m.flatMap(of)` ≡ `m`
3. **Asociatividad**: `m.flatMap(f).flatMap(g)` ≡ `m.flatMap(x -> f(x).flatMap(g))`

Estas leyes garantizan que la composición de operaciones monádicas sea predecible.

### Ejercicios — Functores y Mónadas

#### Ejercicio 22 (Análisis)

Analiza el siguiente código que usa `Optional`:

```java
Map<String, String> config = Map.of(
    "db.url", "localhost:5432",
    "db.port", "5432",
    "timeout", "abc"
);

Optional<Integer> timeout = Optional.ofNullable(config.get("timeout"))
    .filter(s -> s.matches("\\d+"))
    .map(Integer::parseInt)
    .filter(t -> t > 0 && t < 30000);

Optional<Integer> maxConn = Optional.ofNullable(config.get("max.connections"))
    .map(Integer::parseInt);
```

**Preguntas:**
1. ¿Qué contiene `timeout`? Traza cada paso del pipeline.
2. ¿Qué contiene `maxConn`? ¿Lanza alguna excepción? ¿Por qué?
3. ¿Qué diferencia hay entre `Optional.of()` y `Optional.ofNullable()`? ¿Cuándo usar cada uno?
4. Reescribe `maxConn` para que maneje correctamente el caso donde la clave no existe Y el caso donde el valor no es un número.

#### Ejercicio 23 (Aplicación)

Modela un sistema de **procesamiento de pedidos** usando `Optional` como mónada:

```java
record Pedido(String id, String clienteId, List<String> productos, String cuponId) {}
record Cliente(String id, String email, boolean activo) {}
record Cupon(String codigo, double descuento, boolean valido) {}
```

Implementa usando `flatMap` y `map` (sin `if` ni `null` checks):

```java
// Retorna el descuento aplicable al pedido, si existe y es válido
// Flujo: pedido → buscar cliente (activo) → buscar cupón (válido) → descuento
static Optional<Double> calcularDescuento(Pedido pedido,
        Map<String, Cliente> clientes,
        Map<String, Cupon> cupones) { /* ... */ }
```

#### Ejercicio 24 (Aplicación)

Implementa la mónada `Resultado<T>` (similar a `Result<T>`) con las siguientes características:

```java
sealed interface Resultado<T> permits Resultado.Exito, Resultado.Fallo {
    record Exito<T>(T valor) implements Resultado<T> {}
    record Fallo<T>(String error) implements Resultado<T> {}

    // Operaciones monádicas
    <R> Resultado<R> map(Function<T, R> f);
    <R> Resultado<R> flatMap(Function<T, Resultado<R>> f);
    T getOrElse(T valorPorDefecto);
    Resultado<T> recover(Function<String, T> recuperador);

    // Constructores estáticos
    static <T> Resultado<T> exito(T valor) { /* ... */ }
    static <T> Resultado<T> fallo(String error) { /* ... */ }
    static <T> Resultado<T> intentar(Supplier<T> accion) { /* ... */ }
}
```

Verifica que tu implementación cumpla las tres leyes de las mónadas con pruebas concretas.

---

## 10. Fuentes y Referencias

### Libros Fundamentales

| Título | Autor(es) | Año | Relevancia |
|--------|-----------|-----|------------|
| *Structure and Interpretation of Computer Programs* (SICP) | Abelson, Sussman | 1996 | El clásico de FP; usa Scheme |
| *Functional Programming in Java* | Pierre-Yves Saumont | 2017 | FP aplicada al ecosistema Java |
| *Java: The Good Parts* | Jim Waldo | 2010 | Incluye streams y lambdas |
| *Haskell Programming from First Principles* | Allen, Moronuki | 2016 | FP pura; excelente para la teoría |
| *Category Theory for Programmers* | Bartosz Milewski | 2019 | Fundamentos matemáticos de FP |
| *Programming in Scala* | Odersky, Spoon, Venners | 2021 | FP en la JVM con Scala |

### Artículos y Papers

| Título | Autor | Fuente |
|--------|-------|--------|
| "Why Functional Programming Matters" | John Hughes | 1990 — Paper fundacional |
| "Can Programming Be Liberated from the Von Neumann Style?" | John Backus | 1978 — Turing Award Lecture |
| "A History of Haskell: Being Lazy with Class" | Hudak et al. | 2007 — HOPL III |

### Recursos en Línea

| Recurso | URL | Descripción |
|---------|-----|-------------|
| Java Stream API (Javadoc) | https://docs.oracle.com/en/java/docs/ | Documentación oficial |
| Baeldung — Functional Java | https://www.baeldung.com/java-functional-programming | Tutoriales prácticos con Java |
| FP in Java — DZone | https://dzone.com/java | Artículos especializados |
| Functional Programming Jargon | https://github.com/hemanth/functional-programming-jargon | Glosario completo con ejemplos |
| JavaBrains — Functional Programming | https://javabrains.io | Cursos en video gratuitos |
| *Mostly Adequate Guide to FP* | https://mostly-adequate.gitbook.io/ | Guía interactiva en JavaScript (conceptos transferibles) |
| Vavr Library | https://www.vavr.io | Librería FP para Java (Option, Try, Either) |

### Cursos en Línea

| Plataforma | Curso | Nivel |
|------------|-------|-------|
| Coursera | *Functional Programming in Scala* (École Polytechnique) | Intermedio |
| edX | *Programming in Haskell* (U. of Adelaide) | Intermedio |
| Udemy | *Modern Java: Learn Java 8 features* | Principiante |
| YouTube | *Functional Design Patterns* (Scott Wlaschin) | Intermedio |

### Herramientas y Librerías

| Herramienta | Propósito | URL |
|-------------|-----------|-----|
| **Vavr** | FP completa en Java (colecciones persistentes, Try, Either) | https://www.vavr.io |
| **Project Reactor** | Programación reactiva funcional | https://projectreactor.io |
| **Cyclops** | Extensiones funcionales para Java | https://github.com/aol/cyclops |
| **jOOλ** | Lambdas y Streams extendidos | https://github.com/jOOQ/jOOL |

---

*Material creado para el curso PTEC102 Paradigmas de Programación. Los ejemplos de código están disponibles en `java/src/main/java/com/paradigmas/fp/`.*

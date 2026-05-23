# Programación Funcional en Java 26

Proyecto IntelliJ IDEA con ejemplos de programación funcional usando **OpenJDK 26**.

## Requisitos
- OpenJDK 26
- IntelliJ IDEA 2026.x (Community o Ultimate)

## Abrir en IntelliJ IDEA
1. **File → Open** → seleccionar esta carpeta `java/`
2. IntelliJ detectará el módulo `functional-fp.iml` automáticamente
3. Si pide configurar SDK: **File → Project Structure → SDK → Add SDK → JDK** → apuntar a tu JDK 26
4. Ejecutar cualquier clase con el botón ▶ en el `main()`

## Estructura

```
src/main/java/com/paradigmas/fp/
├── funciones_puras/   FuncionesPuras.java
├── inmutabilidad/     Inmutabilidad.java        (records)
├── streams/           MapFilterReduce.java      (Stream API)
├── currying/          Currying.java             (Function<A, Function<B,C>>)
├── composicion/       Composicion.java          (andThen / compose)
├── recursion/         Recursion.java            (memoización, quicksort)
└── monadas/           Maybe.java                (sealed interface)
                       Result.java               (sealed interface)
                       MonadasDemo.java          (demo integrado)
```

## Características de Java 26 utilizadas
| Feature | Dónde |
|---------|-------|
| **Records** | `Inmutabilidad`, `Recursion`, `MonadasDemo` |
| **Sealed classes** | `Maybe`, `Result` |
| **Pattern matching switch** | `MonadasDemo` |
| **Stream API** (`toList()`, `mapToDouble`) | `MapFilterReduce`, `Recursion` |
| **var** | Todos los ejemplos |
| **Lambdas & Method references** | Todos los ejemplos |
| **`List.of()` / colecciones inmutables** | `Inmutabilidad`, `Recursion` |

## Ejecutar desde terminal
```bash
# Compilar
javac -d out --source-path src/main/java \
  $(find src -name "*.java")

# Ejecutar un ejemplo
java -cp out com.paradigmas.fp.streams.MapFilterReduce
```

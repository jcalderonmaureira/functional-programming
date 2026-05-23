"""
COMPOSICIÓN DE FUNCIONES
=========================
La composición permite construir funciones complejas
a partir de funciones simples: (f ∘ g)(x) = f(g(x))

Principio: "una función hace una sola cosa bien"
→ componer funciones pequeñas para crear pipelines.
"""

from functools import reduce
from typing import Callable, TypeVar

T = TypeVar("T")


# -----------------------------------------------
# compose: aplica funciones de derecha a izquierda
# -----------------------------------------------
def compose(*funcs: Callable) -> Callable:
    """compose(f, g, h)(x) == f(g(h(x)))"""
    return reduce(lambda f, g: lambda x: f(g(x)), funcs)


# -----------------------------------------------
# pipe: aplica funciones de izquierda a derecha (más legible)
# -----------------------------------------------
def pipe(*funcs: Callable) -> Callable:
    """pipe(f, g, h)(x) == h(g(f(x)))"""
    return reduce(lambda f, g: lambda x: g(f(x)), funcs)


# -----------------------------------------------
# Funciones atómicas para componer
# -----------------------------------------------
def limpiar(texto: str) -> str:
    return texto.strip()

def normalizar(texto: str) -> str:
    return texto.lower()

def capitalizar_palabras(texto: str) -> str:
    return texto.title()

def quitar_puntuacion(texto: str) -> str:
    import re
    return re.sub(r"[^\w\s]", "", texto)

def agregar_exclamacion(texto: str) -> str:
    return texto + "!"


# -----------------------------------------------
# Composición de transformaciones numéricas
# -----------------------------------------------
doble   = lambda x: x * 2
sumar_1 = lambda x: x + 1
negativo = lambda x: -x

# (negativo ∘ sumar_1 ∘ doble)(5) = negativo(sumar_1(doble(5)))
#                                  = negativo(sumar_1(10))
#                                  = negativo(11)
#                                  = -11
transform = compose(negativo, sumar_1, doble)
pipeline  = pipe(doble, sumar_1, negativo)   # mismo resultado


# -----------------------------------------------
# Pipeline de procesamiento de texto
# -----------------------------------------------
formatear_nombre = pipe(
    limpiar,
    normalizar,
    quitar_puntuacion,
    capitalizar_palabras
)

saludo_festivo = pipe(
    formatear_nombre,
    lambda nombre: f"Hola, {nombre}",
    agregar_exclamacion
)


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Composición numérica ===")
    print(transform(5))   # -11
    print(pipeline(5))    # -11  (misma operación, diferente orden de lectura)

    print("\n=== Pipeline de texto ===")
    nombres_sucios = ["  juan   ", "MARÍA JOSÉ!", "  carlos  "]
    for n in nombres_sucios:
        print(f"'{n}' → '{formatear_nombre(n)}'")

    print("\n=== Saludo festivo ===")
    print(saludo_festivo("  ana  "))

    print("\n=== Composición aplicada a lista ===")
    numeros = [1, 2, 3, 4, 5]
    resultado = list(map(pipeline, numeros))
    print(f"pipe(doble, +1, negativo) sobre {numeros} → {resultado}")

"""
FUNCTORES Y MÓNADAS (introducción)
====================================
Conceptos avanzados de programación funcional:

Functor: tipo que implementa `map` (puede transformar su contenido
         sin cambiar la "forma" del contenedor).

Mónada:  functor que además implementa `flatMap`/`bind` para
         encadenar computaciones que pueden fallar o tener contexto.

Ejemplos prácticos con:
  - Maybe (manejo de None / valores opcionales)
  - Result / Either (manejo de errores sin excepciones)
"""

from __future__ import annotations
from typing import TypeVar, Generic, Callable, Optional

A = TypeVar("A")
B = TypeVar("B")


# -----------------------------------------------
# MAYBE MONAD: manejo funcional de valores nulos
# -----------------------------------------------
class Maybe(Generic[A]):
    """Envuelve un valor que puede ser None (Just) o vacío (Nothing)."""

    def __init__(self, value: Optional[A]):
        self._value = value

    @classmethod
    def of(cls, value: Optional[A]) -> "Maybe[A]":
        return cls(value)

    @property
    def is_nothing(self) -> bool:
        return self._value is None

    def map(self, func: Callable[[A], B]) -> "Maybe[B]":
        """Aplica func si hay valor; propaga Nothing si es None."""
        if self.is_nothing:
            return Maybe(None)
        return Maybe(func(self._value))

    def flat_map(self, func: Callable[[A], "Maybe[B]"]) -> "Maybe[B]":
        if self.is_nothing:
            return Maybe(None)
        return func(self._value)

    def get_or_else(self, default: A) -> A:
        return default if self.is_nothing else self._value

    def __repr__(self):
        return "Nothing" if self.is_nothing else f"Just({self._value})"


# -----------------------------------------------
# RESULT MONAD: éxito o error sin excepciones
# -----------------------------------------------
class Result(Generic[A]):
    """Ok(valor) o Err(mensaje): nunca lanza excepciones."""

    def __init__(self, value: A = None, error: str = None):
        self._value = value
        self._error = error

    @classmethod
    def ok(cls, value: A) -> "Result[A]":
        return cls(value=value)

    @classmethod
    def err(cls, error: str) -> "Result[A]":
        return cls(error=error)

    @property
    def is_ok(self) -> bool:
        return self._error is None

    def map(self, func: Callable[[A], B]) -> "Result[B]":
        if not self.is_ok:
            return Result.err(self._error)
        try:
            return Result.ok(func(self._value))
        except Exception as e:
            return Result.err(str(e))

    def flat_map(self, func: Callable[[A], "Result[B]"]) -> "Result[B]":
        if not self.is_ok:
            return self
        return func(self._value)

    def get_or_else(self, default: A) -> A:
        return default if not self.is_ok else self._value

    def __repr__(self):
        return f"Err({self._error})" if not self.is_ok else f"Ok({self._value})"


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Maybe Monad ===")

    # Pipeline seguro: si cualquier paso devuelve None, se propaga
    usuarios = {
        1: {"nombre": "Ana", "direccion": {"ciudad": "Bogotá"}},
        2: {"nombre": "Luis"},
    }

    def obtener_usuario(uid):
        return Maybe.of(usuarios.get(uid))

    def obtener_ciudad(usuario):
        return Maybe.of(usuario.get("direccion", {}).get("ciudad"))

    ciudad_ana  = obtener_usuario(1).flat_map(obtener_ciudad)
    ciudad_luis = obtener_usuario(2).flat_map(obtener_ciudad)
    ciudad_x    = obtener_usuario(99).flat_map(obtener_ciudad)

    print(ciudad_ana)                               # Just(Bogotá)
    print(ciudad_luis.get_or_else("Sin ciudad"))    # Sin ciudad
    print(ciudad_x.get_or_else("Usuario no existe")) # Usuario no existe

    print("\n=== Result Monad ===")

    def dividir(a: float, b: float) -> Result:
        if b == 0:
            return Result.err("División por cero")
        return Result.ok(a / b)

    def raiz(x: float) -> Result:
        if x < 0:
            return Result.err("Raíz de número negativo")
        return Result.ok(x ** 0.5)

    # Pipeline de cálculos que puede fallar:
    res1 = dividir(100, 4).flat_map(raiz)
    res2 = dividir(100, 0).flat_map(raiz)
    res3 = dividir(-25, 1).flat_map(raiz)

    print(res1)  # Ok(5.0)
    print(res2)  # Err(División por cero)
    print(res3)  # Err(Raíz de número negativo)

    print("\n=== Encadenamiento con map ===")
    resultado = (
        Result.ok(16)
        .map(lambda x: x / 4)      # → Ok(4.0)
        .map(lambda x: x + 1)      # → Ok(5.0)
        .map(lambda x: x * 2)      # → Ok(10.0)
    )
    print(resultado)  # Ok(10.0)

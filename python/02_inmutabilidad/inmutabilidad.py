"""
INMUTABILIDAD
=============
En programación funcional los datos no se modifican:
en lugar de mutar una estructura, se crea una nueva versión.

Python no impone inmutabilidad, pero la podemos modelar con:
  - Tuplas en vez de listas
  - namedtuple / dataclass(frozen=True)
  - Patrones de copia-y-actualización
"""

from dataclasses import dataclass
from typing import Tuple

# -----------------------------------------------
# MUTABLE (estilo imperativo) ← evitar
# -----------------------------------------------
def agregar_impuesto_mutable(precios: list, tasa: float) -> list:
    for i in range(len(precios)):
        precios[i] *= (1 + tasa)   # MUTA la lista original!
    return precios


# -----------------------------------------------
# INMUTABLE (estilo funcional) ← preferir
# -----------------------------------------------
def agregar_impuesto(precios: Tuple[float, ...], tasa: float) -> Tuple[float, ...]:
    return tuple(p * (1 + tasa) for p in precios)


# -----------------------------------------------
# dataclass inmutable con frozen=True
# -----------------------------------------------
@dataclass(frozen=True)
class Punto:
    x: float
    y: float

    def mover(self, dx: float, dy: float) -> "Punto":
        # No muta self, retorna un NUEVO Punto
        return Punto(self.x + dx, self.y + dy)

    def __repr__(self):
        return f"Punto({self.x}, {self.y})"


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Mutable vs Inmutable ===")
    precios_originales = [100.0, 200.0, 50.0]
    print(f"Antes (mutable): {precios_originales}")
    agregar_impuesto_mutable(precios_originales, 0.19)
    print(f"Después (mutable): {precios_originales}")  # ¡lista modificada!

    print()
    precios_tupla = (100.0, 200.0, 50.0)
    nuevos_precios = agregar_impuesto(precios_tupla, 0.19)
    print(f"Original (inmutable): {precios_tupla}")    # sin cambios
    print(f"Con impuesto:         {nuevos_precios}")

    print("\n=== Punto inmutable ===")
    p1 = Punto(0, 0)
    p2 = p1.mover(3, 4)
    print(f"p1 = {p1}")   # sigue siendo (0, 0)
    print(f"p2 = {p2}")   # nuevo punto (3, 4)

    # Intentar mutar dispara error:
    try:
        p1.x = 10
    except Exception as e:
        print(f"Error al mutar: {e}")

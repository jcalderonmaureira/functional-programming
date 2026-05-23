"""
RECURSIÓN
==========
En programación funcional se prefiere la recursión sobre los bucles
(especialmente en lenguajes que optimizan tail calls).

Python no optimiza tail-call, pero podemos usar:
  - Recursión estándar para estructuras pequeñas
  - Acumuladores para simular tail-call
  - Generadores / itertools para colecciones grandes
"""

import sys
from functools import lru_cache

# -----------------------------------------------
# Factorial
# -----------------------------------------------
def factorial(n: int) -> int:
    if n <= 1:
        return 1
    return n * factorial(n - 1)

def factorial_tail(n: int, acumulador: int = 1) -> int:
    """Versión con acumulador (simula tail-call)."""
    if n <= 1:
        return acumulador
    return factorial_tail(n - 1, n * acumulador)


# -----------------------------------------------
# Fibonacci con memoización
# -----------------------------------------------
@lru_cache(maxsize=None)
def fibonacci(n: int) -> int:
    if n <= 1:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)


# -----------------------------------------------
# Suma de lista (recursiva)
# -----------------------------------------------
def suma_lista(lst: list) -> int:
    if not lst:
        return 0
    cabeza, *cola = lst
    return cabeza + suma_lista(cola)

def suma_lista_tail(lst: list, acc: int = 0) -> int:
    if not lst:
        return acc
    return suma_lista_tail(lst[1:], acc + lst[0])


# -----------------------------------------------
# Aplanar lista anidada (recursiva)
# -----------------------------------------------
def aplanar(lst: list) -> list:
    if not lst:
        return []
    cabeza, *cola = lst
    if isinstance(cabeza, list):
        return aplanar(cabeza) + aplanar(cola)
    return [cabeza] + aplanar(cola)


# -----------------------------------------------
# Ordenamiento quicksort funcional
# -----------------------------------------------
def quicksort(lst: list) -> list:
    if len(lst) <= 1:
        return lst
    pivote = lst[0]
    menores  = [x for x in lst[1:] if x <= pivote]
    mayores  = [x for x in lst[1:] if x > pivote]
    return quicksort(menores) + [pivote] + quicksort(mayores)


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Factorial ===")
    print(factorial(10))           # 3628800
    print(factorial_tail(10))      # 3628800

    print("\n=== Fibonacci (con caché) ===")
    print([fibonacci(i) for i in range(10)])  # [0,1,1,2,3,5,8,13,21,34]

    print("\n=== Suma recursiva ===")
    datos = [1, 2, 3, 4, 5]
    print(suma_lista(datos))       # 15
    print(suma_lista_tail(datos))  # 15

    print("\n=== Aplanar lista ===")
    anidada = [1, [2, 3], [4, [5, 6]], 7]
    print(aplanar(anidada))        # [1, 2, 3, 4, 5, 6, 7]

    print("\n=== Quicksort funcional ===")
    desordenada = [3, 6, 8, 10, 1, 2, 1]
    print(quicksort(desordenada))  # [1, 1, 2, 3, 6, 8, 10]

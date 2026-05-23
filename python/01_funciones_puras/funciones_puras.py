"""
FUNCIONES PURAS
===============
Una función pura:
  1. Dado el mismo input, siempre retorna el mismo output.
  2. No produce efectos secundarios (no modifica estado externo).
"""

# -----------------------------------------------
# FUNCIÓN PURA: solo depende de sus argumentos
# -----------------------------------------------
def suma(a: int, b: int) -> int:
    return a + b

def area_circulo(radio: float) -> float:
    import math
    return math.pi * radio ** 2

def saludar(nombre: str) -> str:
    return f"Hola, {nombre}!"


# -----------------------------------------------
# FUNCIÓN IMPURA: tiene efectos secundarios
# -----------------------------------------------
total_acumulado = 0  # estado global

def suma_impura(a: int, b: int) -> int:
    global total_acumulado
    total_acumulado += a + b   # modifica estado externo -> IMPURA
    return a + b

def saludar_impura(nombre: str) -> str:
    print(f"Hola, {nombre}!")  # efecto secundario (I/O) -> IMPURA
    return f"Hola, {nombre}!"


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Funciones Puras ===")
    print(suma(3, 4))         # siempre 7
    print(suma(3, 4))         # siempre 7
    print(area_circulo(5))
    print(saludar("Ana"))

    print("\n=== Función Impura ===")
    print(suma_impura(3, 4))
    print(f"total_acumulado = {total_acumulado}")  # estado cambia!
    print(suma_impura(3, 4))
    print(f"total_acumulado = {total_acumulado}")  # cambia de nuevo!

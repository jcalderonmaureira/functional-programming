"""
CURRYING Y APLICACIÓN PARCIAL
==============================
Currying: transformar f(a, b, c) en f(a)(b)(c).
  → Cada llamada recibe UN argumento y retorna una función
    que espera el siguiente.

Aplicación Parcial: fijar algunos argumentos de una función
  → Usando functools.partial
"""

from functools import partial


# -----------------------------------------------
# CURRYING manual
# -----------------------------------------------
def suma_curry(a):
    def inner(b):
        return a + b
    return inner

# Equivalente con lambda:
suma_curry_lambda = lambda a: lambda b: a + b

# Uso:
suma_5  = suma_curry(5)      # función especializada: "suma 5 a algo"
suma_10 = suma_curry(10)


# -----------------------------------------------
# Currying de 3 argumentos
# -----------------------------------------------
def volumen_curry(largo):
    def _ancho(ancho):
        def _alto(alto):
            return largo * ancho * alto
        return _alto
    return _ancho

# Equivalente arrow-style:
volumen = lambda l: lambda w: lambda h: l * w * h


# -----------------------------------------------
# APLICACIÓN PARCIAL con functools.partial
# -----------------------------------------------
def potencia(base, exponente):
    return base ** exponente

cuadrado = partial(potencia, exponente=2)
cubo     = partial(potencia, exponente=3)

def log(nivel, mensaje):
    print(f"[{nivel}] {mensaje}")

log_info  = partial(log, "INFO")
log_error = partial(log, "ERROR")


# -----------------------------------------------
# Currying genérico con decorador
# -----------------------------------------------
import inspect

def curry(func):
    """Decorador que convierte cualquier función en curried."""
    n = len(inspect.signature(func).parameters)

    def acumular(*args):
        if len(args) >= n:
            return func(*args[:n])
        return lambda *more: acumular(*(args + more))

    return acumular

@curry
def multiplicar(a, b, c):
    return a * b * c


# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== Currying básico ===")
    print(suma_curry(3)(4))       # 7
    print(suma_5(8))              # 13
    print(suma_10(7))             # 17

    print("\n=== Currying 3 argumentos ===")
    print(volumen_curry(2)(3)(4)) # 24
    vol_caja = volumen(5)(5)      # función parcial: volumen de 5x5x?
    print(vol_caja(10))           # 250

    print("\n=== Aplicación parcial ===")
    print(cuadrado(5))    # 25
    print(cubo(3))        # 27
    log_info("Servidor iniciado")
    log_error("No se encontró el archivo")

    print("\n=== Decorador @curry ===")
    print(multiplicar(2)(3)(4))    # 24
    doble = multiplicar(2)         # aplica parcialmente
    print(doble(5)(6))             # 60
    print(multiplicar(2, 3, 4))    # también funciona directo: 24

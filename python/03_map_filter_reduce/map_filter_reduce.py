"""
MAP / FILTER / REDUCE
=====================
Las tres funciones de orden superior más usadas en programación funcional
para transformar colecciones de forma declarativa.

  map(f, colección)    → aplica f a cada elemento
  filter(pred, col)    → conserva elementos donde pred es True
  reduce(f, col)       → combina todos los elementos en uno
"""

from functools import reduce

numeros = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

# -----------------------------------------------
# MAP: transformar cada elemento
# -----------------------------------------------
cuadrados      = list(map(lambda x: x ** 2, numeros))
cuadrados_gen  = [x ** 2 for x in numeros]   # list comprehension equivalente

# -----------------------------------------------
# FILTER: seleccionar elementos
# -----------------------------------------------
pares    = list(filter(lambda x: x % 2 == 0, numeros))
pares_lc = [x for x in numeros if x % 2 == 0]

# -----------------------------------------------
# REDUCE: acumular / colapsar
# -----------------------------------------------
suma_total   = reduce(lambda acc, x: acc + x, numeros, 0)
producto     = reduce(lambda acc, x: acc * x, numeros, 1)
maximo       = reduce(lambda acc, x: x if x > acc else acc, numeros)

# -----------------------------------------------
# Encadenamiento: pipeline funcional
# -----------------------------------------------
# "La suma de los cuadrados de los números impares"
resultado = reduce(
    lambda acc, x: acc + x,
    map(lambda x: x ** 2,
        filter(lambda x: x % 2 != 0, numeros)),
    0
)

# Con list comprehension equivalente:
resultado_lc = sum(x ** 2 for x in numeros if x % 2 != 0)


# -----------------------------------------------
# Ejemplo práctico: procesar lista de productos
# -----------------------------------------------
productos = [
    {"nombre": "Laptop",  "precio": 1200, "categoria": "tecnologia"},
    {"nombre": "Libro",   "precio": 25,   "categoria": "educacion"},
    {"nombre": "Monitor", "precio": 350,  "categoria": "tecnologia"},
    {"nombre": "Silla",   "precio": 180,  "categoria": "muebles"},
    {"nombre": "Teclado", "precio": 80,   "categoria": "tecnologia"},
]

# Solo tecnología con precio < 500, mostrar nombres en mayúsculas
nombres_tech_baratos = list(map(
    lambda p: p["nombre"].upper(),
    filter(lambda p: p["categoria"] == "tecnologia" and p["precio"] < 500, productos)
))

total_tecnologia = reduce(
    lambda acc, p: acc + p["precio"],
    filter(lambda p: p["categoria"] == "tecnologia", productos),
    0
)

# -----------------------------------------------
# DEMO
# -----------------------------------------------
if __name__ == "__main__":
    print("=== map ===")
    print(f"Cuadrados: {cuadrados}")

    print("\n=== filter ===")
    print(f"Pares: {pares}")

    print("\n=== reduce ===")
    print(f"Suma: {suma_total}")
    print(f"Producto: {producto}")
    print(f"Máximo: {maximo}")

    print("\n=== Pipeline encadenado ===")
    print(f"Suma de cuadrados de impares: {resultado}")
    print(f"Equivalente con comprehension: {resultado_lc}")

    print("\n=== Ejemplo productos ===")
    print(f"Tech barata (< $500): {nombres_tech_baratos}")
    print(f"Total inversión tecnología: ${total_tecnologia}")

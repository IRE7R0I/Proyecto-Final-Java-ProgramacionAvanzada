# 🧵 Sistema de Gestión de Tienda Textil (Java + MySQL)

Este proyecto es una aplicación de consola en Java que simula la operación básica de una tienda de ropa. Permite registrar productos, clientes y ventas, validando stock y almacenando la información en una base de datos MySQL.

> 🔧 Desarrollado siguiendo el principio de **Inversión de Dependencias (DIP)** para una arquitectura desacoplada y extensible.

---

## 🚀 Funcionalidades

- ✅ Alta de productos
- ✅ Alta de clientes
- ✅ Registro de ventas con múltiples productos
- ✅ Validación de stock antes de la venta
- ✅ Persistencia en base de datos (MySQL)
- ✅ Inicialización automática de base de datos y tablas
- ✅ Visualización de historial de ventas con total acumulado

---

## 💾 Base de datos

- El sistema crea automáticamente la base de datos `textil` y las tablas necesarias si no existen.
- El acceso a la base está configurado por defecto con:
  - **Usuario**: `root`
  - **Contraseña**: `root`
  - **URL**: `jdbc:mysql://localhost:3306/`

> 🛠️ Podés cambiar estos datos en la clase `MySQL_Repository.java`.

---

## 🧪 Ejecución

1. Asegurate de tener MySQL corriendo en `localhost`.
2. Importá el proyecto en tu IDE favorito (NetBeans, IntelliJ, Eclipse, etc).
3. Ejecutá la clase `Main.java`.
4. Observá en consola la creación de productos, clientes y ventas.

---

## 🔌 Dependencias

- Java 8 o superior
- Conector JDBC para MySQL (`mysql-connector-java`)

---

## 🧠 Principio aplicado: Inversión de Dependencias (DIP)

El código principal depende de interfaces (`ProductoRepository`, `ClienteRepository`, `VentaRepository`) en lugar de implementaciones concretas, lo que facilita:

- Reemplazar fácilmente el backend (por ejemplo, usar MongoDB en vez de MySQL)
- Testear con mocks o fakes sin tocar la base de datos real
- Separar la lógica de negocio de la lógica de persistencia

---

## 👨‍💻 Autor

Este proyecto fue desarrollado con fines educativos.  
Podés adaptarlo, mejorarlo o usarlo como base para sistemas más complejos.

---

## 🏁 Licencia

Libre para usar, modificar o compartir.

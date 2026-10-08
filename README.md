# Sistema de Gestión de Biblioteca 

Evaluación Final Transversal (Semana 9) — Desarrollo Orientado a Objetos II, Duoc UC.

**_Aplicación de escritorio en Java para registrar, prestar y devolver libros, 
y gestionar estudiantes e inventario._**

**Tecnologías**
- Java (JFrame / Swing)
- MySQL con JDBC (MySQL Connector/J)
- IntelliJ IDEA

**Patrones y conceptos aplicados**
- MVC: paquetes `modelo`, `vista` y `controlador`.
- DAO: una clase DAO por entidad, con la interfaz genérica `CrudDAO<T>`.
- Singleton:`DatabaseConnection` mantiene una única conexión a la base de datos.
- Herencia y polimorfismo:`Usuario` y `Estudiante` heredan de la clase abstracta `Persona`.
- Concurrencia:el préstamo se procesa en un hilo independiente para no congelar la interfaz.
- Sincronización:los métodos que modifican el stock (`LibroDAO`) son `synchronized` para evitar condiciones de carrera.

**Estructura**

- src/conexion — conexión Singleton
- src/modelo — clases del sistema
- src/dao — acceso a datos
- src/controlador — lógica entre vista y datos
- src/vista — ventanas JFrame
- src/main — clase principal
- sql — scripts de creación y poblado de la base de datos
- lib — MySQL Connector/J
- ejecutable — archivo .jar del sistema

**Cómo ejecutar**
1. Ejecutar en MySQL `sql/PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql` y luego `sql/PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql`.
2. En `src/conexion/DatabaseConnection.java`, ajustar el usuario y la clave de MySQL.
3. Ejecutar `src/main/Main.java`, o el archivo `ejecutable/BibliotecaEFT_Semana9.jar`.

**Usuarios de prueba**
| Bibliotecario | antonia@correo.cl | clave123 |
| Estudiante | carlos@correo.cl | clave123 |

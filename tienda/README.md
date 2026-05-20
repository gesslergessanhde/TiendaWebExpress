# Tienda Web Express

Mini e-commerce desarrollado con **Spring Boot 3 + Thymeleaf + H2 + CSS responsivo + JavaScript**.

## Requisitos
- Java 17+
- Maven 3.9+ (o usa el wrapper de tu IDE)

## Como ejecutar

```bash
mvn spring-boot:run
```

Luego abre: http://localhost:8080

Consola H2 (opcional): http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:tiendadb`
- Usuario: `sa` (sin contrasena)

## Estructura

```
src/main/java/com/tienda/express/
  TiendaWebExpressApplication.java   # punto de entrada
  controller/TiendaController.java   # rutas: /, /catalogo, /producto/{id}, /pedido
  model/Producto.java                # entidad JPA
  model/PedidoForm.java              # DTO con validaciones
  repository/ProductoRepository.java # acceso a datos

src/main/resources/
  templates/        layout.html, index.html, catalogo.html, detalle.html, pedido.html, confirmacion.html
  static/css/       styles.css   (responsivo)
  static/js/        app.js       (filtro, contador, toggle, validacion, toast)
  data.sql          datos semilla (10 productos)
  application.properties
```

## Funcionalidades

1. **Inicio**: logo, banner, menu, productos destacados, responsivo.
2. **Catalogo**: lista con imagen, nombre, precio, categoria y boton "Ver detalle".
3. **Detalle**: imagen ampliada, descripcion, precio, disponibilidad, botones "Agregar" y "Solicitar".
4. **JavaScript**: filtro por categoria, contador del carrito (persistente con localStorage), mostrar/ocultar descripcion, validacion del formulario, toast emergente.
5. **Formulario de pedido**: nombre, correo, producto, cantidad, comentario; validacion en cliente (JS) y servidor (Bean Validation).

## Notas
- Base de datos en memoria H2 (se reinicia con cada arranque).
- Para personalizar productos edita `src/main/resources/data.sql`.

package com.tienda.express.model;

import jakarta.validation.constraints.*;

public class PedidoForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo no valido")
    private String correo;

    @NotBlank(message = "Selecciona un producto")
    private String producto;

    @NotNull(message = "Indica la cantidad")
    @Min(value = 1, message = "Minimo 1")
    @Max(value = 99, message = "Maximo 99")
    private Integer cantidad;

    @Size(max = 500)
    private String comentario;

    // --- GETTERS Y SETTERS ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
package com.tienda.express.repository;

import com.tienda.express.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByDestacadoTrue();

    List<Producto> findByCategoriaIgnoreCase(String categoria);

    // Consulta combinada dinámica: nombre + categoría (ambos opcionales)
    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR :nombre = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:categoria IS NULL OR :categoria = '' OR LOWER(p.categoria) = LOWER(:categoria))")
    List<Producto> buscarProductos(@Param("nombre") String nombre, @Param("categoria") String categoria);
}
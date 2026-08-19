package com.petshop.repository;

import com.petshop.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    List<Producto> findByIdCategoria(
            Integer idCategoria);

    List<Producto> findByIdCategoriaAndIdProductoNot(
            Integer idCategoria,
            Integer idProducto);

    List<Producto> findAllByOrderByPrecioAsc();
}
package com.petshop.repository;

import com.petshop.domain.MovimientoInventario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepository
        extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario>
            findByProductoIdProductoOrderByFechaDesc(
                    Integer idProducto);
}
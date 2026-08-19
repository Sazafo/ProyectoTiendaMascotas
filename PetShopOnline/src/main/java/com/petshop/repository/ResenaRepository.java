package com.petshop.repository;

import com.petshop.domain.Resena;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResenaRepository
        extends JpaRepository<Resena, Integer> {

    List<Resena> findByProductoIdProductoOrderByFechaDesc(
            Integer idProducto);

    boolean existsByUsuarioIdUsuarioAndProductoIdProducto(
            Integer idUsuario,
            Integer idProducto);
}
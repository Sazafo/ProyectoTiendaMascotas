package com.petshop.repository;

import com.petshop.domain.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioIdUsuarioOrderByFechaDesc(
            Integer idUsuario);

    List<Pedido> findTop5ByOrderByFechaDesc();

    long countByFechaBetween(
            LocalDateTime inicio,
            LocalDateTime fin);
}
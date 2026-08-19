package com.petshop.repository;

import com.petshop.domain.DetallePedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByPedidoIdPedido(
            Integer idPedido);
}
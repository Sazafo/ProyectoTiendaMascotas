package com.petshop.service;

import com.petshop.domain.Pedido;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    // Guardado temporal mientras no exista la base de datos.
    // Cuando se conecte MySQL, este metodo guardar() pasa a ser
    // un PedidoRepository extends JpaRepository<Pedido, Integer>
    // y el CarritoController no necesita cambiar como lo llama.
    private final List<Pedido> pedidos = new ArrayList<>();
    private Integer siguienteId = 1;

    public Pedido guardar(Pedido pedido) {

        pedido.setIdPedido(siguienteId);
        siguienteId++;

        pedidos.add(pedido);

        return pedido;
    }

    public List<Pedido> listarPorUsuario(Integer idUsuario) {

        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getUsuario().getIdUsuario().equals(idUsuario)) {
                resultado.add(pedido);
            }
        }

        return resultado;
    }
}
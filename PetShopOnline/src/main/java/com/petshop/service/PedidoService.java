package com.petshop.service;

import com.petshop.domain.Pedido;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PedidoService {

    
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

    public List<Pedido> listarTodos() {
        return pedidos;
    }

    public Pedido buscarPorId(Integer idPedido) {

        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido().equals(idPedido)) {
                return pedido;
            }
        }

        return null;
    }

    public boolean cambiarEstado(Integer idPedido, String estado) {

        Pedido pedido = buscarPorId(idPedido);

        if (pedido == null) {
            return false;
        }

        pedido.setEstado(estado);

        return true;
    }

    public int contarPedidosHoy() {

        LocalDate hoy = LocalDate.now();

        int contador = 0;

        for (Pedido pedido : pedidos) {
            if (pedido.getFecha().toLocalDate().equals(hoy)) {
                contador++;
            }
        }

        return contador;
    }

    public List<Pedido> listarRecientes(int cantidad) {

        List<Pedido> ordenados = new ArrayList<>(pedidos);

        ordenados.sort(Comparator.comparing(Pedido::getFecha).reversed());

        if (ordenados.size() > cantidad) {
            return ordenados.subList(0, cantidad);
        }

        return ordenados;
    }
}
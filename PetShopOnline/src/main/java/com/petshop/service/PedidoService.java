package com.petshop.service;

import com.petshop.domain.DetallePedido;
import com.petshop.domain.ItemCarrito;
import com.petshop.domain.Pedido;
import com.petshop.repository.DetallePedidoRepository;
import com.petshop.repository.PedidoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository =
                detallePedidoRepository;
    }

    @Transactional
    public Pedido guardar(Pedido pedido) {

        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }

        List<ItemCarrito> items =
                new ArrayList<>(pedido.getItems());

        Pedido pedidoGuardado =
                pedidoRepository.save(pedido);

        for (ItemCarrito item : items) {

            DetallePedido detalle =
                    new DetallePedido();

            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(
                    item.getProducto().getPrecio());

            detallePedidoRepository.save(detalle);
        }

        pedidoGuardado.setItems(items);

        return pedidoGuardado;
    }

    public List<Pedido> listarPorUsuario(
            Integer idUsuario) {

        List<Pedido> pedidos =
                pedidoRepository
                        .findByUsuarioIdUsuarioOrderByFechaDesc(
                                idUsuario
                        );

        cargarItems(pedidos);

        return pedidos;
    }

    public List<Pedido> listarTodos() {

        List<Pedido> pedidos =
                pedidoRepository.findAll();

        cargarItems(pedidos);

        return pedidos;
    }

    public Pedido buscarPorId(Integer idPedido) {

        Pedido pedido =
                pedidoRepository
                        .findById(idPedido)
                        .orElse(null);

        if (pedido != null) {
            cargarItems(pedido);
        }

        return pedido;
    }

    public boolean cambiarEstado(
            Integer idPedido,
            String estado) {

        Pedido pedido =
                pedidoRepository
                        .findById(idPedido)
                        .orElse(null);

        if (pedido == null) {
            return false;
        }

        pedido.setEstado(estado);
        pedidoRepository.save(pedido);

        return true;
    }

    public int contarPedidosHoy() {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicio =
                hoy.atStartOfDay();

        LocalDateTime fin =
                hoy.plusDays(1).atStartOfDay();

        return (int) pedidoRepository
                .countByFechaBetween(inicio, fin);
    }

    public List<Pedido> listarRecientes(
            int cantidad) {

        List<Pedido> pedidos =
                pedidoRepository
                        .findTop5ByOrderByFechaDesc();

        cargarItems(pedidos);

        if (pedidos.size() > cantidad) {
            return pedidos.subList(0, cantidad);
        }

        return pedidos;
    }

    private void cargarItems(List<Pedido> pedidos) {

        for (Pedido pedido : pedidos) {
            cargarItems(pedido);
        }
    }

    private void cargarItems(Pedido pedido) {

        List<DetallePedido> detalles =
                detallePedidoRepository
                        .findByPedidoIdPedido(
                                pedido.getIdPedido());

        List<ItemCarrito> items =
                new ArrayList<>();

        for (DetallePedido detalle : detalles) {

            ItemCarrito item =
                    new ItemCarrito(
                            detalle.getProducto(),
                            detalle.getCantidad()
                    );

            items.add(item);
        }

        pedido.setItems(items);
    }
}
package com.petshop.service;

import com.petshop.domain.MovimientoInventario;
import com.petshop.domain.Producto;
import com.petshop.repository.MovimientoInventarioRepository;
import com.petshop.repository.ProductoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoInventarioService(
            MovimientoInventarioRepository movimientoRepository,
            ProductoRepository productoRepository) {

        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public void registrarSalida(
            Producto producto,
            Integer cantidad) {

        producto.setStock(
                producto.getStock() - cantidad
        );

        productoRepository.save(producto);

        MovimientoInventario movimiento =
                new MovimientoInventario();

        movimiento.setProducto(producto);
        movimiento.setTipo("SALIDA");
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(
                "Salida por venta"
        );

        movimientoRepository.save(movimiento);
    }
}
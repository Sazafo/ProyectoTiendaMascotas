package com.petshop.service;

import com.petshop.domain.Producto;
import com.petshop.repository.ProductoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    public List<Producto> listarPorCategoria(Integer idCategoria) {

        if (idCategoria == null) {
            return productoRepository.listarTodos();
        }

        return productoRepository.listarPorCategoria(idCategoria);
    }

    public Optional<Producto> buscarPorId(Integer idProducto) {
        return productoRepository.buscarPorId(idProducto);
    }

    public List<Producto> buscarRelacionados(
            Integer idCategoria,
            Integer idProductoActual) {

        List<Producto> relacionados = new ArrayList<>();

        if (idCategoria == null) {
            return relacionados;
        }

        List<Producto> productosCategoria =
                productoRepository.listarPorCategoria(idCategoria);

        for (Producto producto : productosCategoria) {

            if (!producto.getIdProducto().equals(idProductoActual)) {
                relacionados.add(producto);
            }
        }

        return relacionados;
    }

    public void guardar(Producto producto) {
        productoRepository.guardar(producto);
    }

    public void eliminar(Integer idProducto) {
        productoRepository.eliminar(idProducto);
    }

    public List<Producto> listarPorPrecio() {
        return productoRepository.listarPorPrecio();
    }
}
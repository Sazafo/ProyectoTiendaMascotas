package com.petshop.service;

import com.petshop.domain.Producto;
import com.petshop.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(
            ProductoRepository productoRepository) {

        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarPorCategoria(
            Integer idCategoria) {

        if (idCategoria == null) {
            return productoRepository.findAll();
        }

        return productoRepository
                .findByIdCategoria(idCategoria);
    }

    public Optional<Producto> buscarPorId(
            Integer idProducto) {

        return productoRepository
                .findById(idProducto);
    }

    public List<Producto> buscarRelacionados(
            Integer idCategoria,
            Integer idProductoActual) {

        if (idCategoria == null) {
            return List.of();
        }

        return productoRepository
                .findByIdCategoriaAndIdProductoNot(
                        idCategoria,
                        idProductoActual
                );
    }

    public void guardar(Producto producto) {
        productoRepository.save(producto);
    }

    public void eliminar(Integer idProducto) {
        productoRepository.deleteById(idProducto);
    }

    public List<Producto> listarPorPrecio() {
        return productoRepository
                .findAllByOrderByPrecioAsc();
    }
}
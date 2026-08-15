package com.petshop.service;

import com.petshop.domain.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();

    private Integer siguienteId = 5;

    public ProductoService() {

        productos.add(new Producto(
                1,
                "Alimento Premium",
                "Alimento balanceado para perros adultos.",
                25.00,
                "https://images.unsplash.com/photo-1589924691995-400dc9ecc119",
                1
        ));

        productos.add(new Producto(
                2,
                "Juguete para Perro",
                "Juguete resistente para mascotas activas.",
                8.50,
                "https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd",
                2
        ));

        productos.add(new Producto(
                3,
                "Arena para Gato",
                "Arena absorbente con control de olores.",
                12.00,
                "https://images.unsplash.com/photo-1574158622682-e40e69881006",
                3
        ));

        productos.add(new Producto(
                4,
                "Correa Ajustable",
                "Correa cómoda y resistente.",
                10.00,
                "https://images.unsplash.com/photo-1548199973-03cce0bbc87b",
                2
        ));
    }

    // LISTAR TODOS
    public List<Producto> listarTodos() {
        return productos;
    }

    // LISTAR POR CATEGORÍA
    public List<Producto> listarPorCategoria(Integer idCategoria) {

        if (idCategoria == null) {
            return productos;
        }

        List<Producto> filtrados = new ArrayList<>();

        for (Producto producto : productos) {

            if (producto.getIdCategoria() != null
                    && producto.getIdCategoria().equals(idCategoria)) {

                filtrados.add(producto);
            }
        }

        return filtrados;
    }

    // BUSCAR POR ID
    public Optional<Producto> buscarPorId(Integer idProducto) {

        for (Producto producto : productos) {

            if (producto.getIdProducto().equals(idProducto)) {
                return Optional.of(producto);
            }
        }

        return Optional.empty();
    }

    // BUSCAR PRODUCTOS RELACIONADOS
    public List<Producto> buscarRelacionados(
            Integer idCategoria,
            Integer idProductoActual) {

        List<Producto> relacionados = new ArrayList<>();

        if (idCategoria == null) {
            return relacionados;
        }

        for (Producto producto : productos) {

            boolean mismaCategoria =
                    idCategoria.equals(producto.getIdCategoria());

            boolean productoDistinto =
                    !producto.getIdProducto().equals(idProductoActual);

            if (mismaCategoria && productoDistinto) {
                relacionados.add(producto);
            }
        }

        return relacionados;
    }

    // GUARDAR O EDITAR
    public void guardar(Producto producto) {

        if (producto.getIdProducto() == null) {

            producto.setIdProducto(siguienteId++);
            productos.add(producto);

        } else {

            for (int i = 0; i < productos.size(); i++) {

                if (productos.get(i)
                        .getIdProducto()
                        .equals(producto.getIdProducto())) {

                    productos.set(i, producto);
                    return;
                }
            }
        }
    }

    // ELIMINAR
    public void eliminar(Integer idProducto) {

        productos.removeIf(
                producto -> producto.getIdProducto().equals(idProducto)
        );
    }
}
package com.petshop.service;

import com.petshop.domain.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    // Catálogo temporal mientras no exista la base de datos.
    // Cuando se conecte MySQL, esta lista se reemplaza por un
    // ProductoRepository extends JpaRepository<Producto, Integer>.
    private final List<Producto> productos = new ArrayList<>();
    private Integer siguienteId = 5;

    public ProductoService() {

        productos.add(new Producto(1, "Alimento Premium",
                "Alimento balanceado para perros adultos.",
                25.00,
                "https://images.unsplash.com/photo-1589924691995-400dc9ecc119",
                1));

        productos.add(new Producto(2, "Juguete para Perro",
                "Juguete resistente para mascotas activas.",
                8.50,
                "https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd",
                2));

        productos.add(new Producto(3, "Arena para Gato",
                "Arena absorbente con control de olores.",
                12.00,
                "https://images.unsplash.com/photo-1574158622682-e40e69881006",
                3));

        productos.add(new Producto(4, "Correa Ajustable",
                "Correa cómoda y resistente.",
                10.00,
                "https://images.unsplash.com/photo-1548199973-03cce0bbc87b",
                2));
    }

    public List<Producto> listarTodos() {
        return productos;
    }

    // Devuelve Optional, igual que JpaRepository.findById(id)
    public Optional<Producto> buscarPorId(Integer idProducto) {
        for (Producto p : productos) {
            if (p.getIdProducto().equals(idProducto)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public void guardar(Producto producto) {
        if (producto.getIdProducto() == null) {
            // Nuevo producto
            producto.setIdProducto(siguienteId++);
            productos.add(producto);
        } else {
            // Editar existente
            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getIdProducto().equals(producto.getIdProducto())) {
                    productos.set(i, producto);
                    return;
                }
            }
        }
    }

    public void eliminar(Integer idProducto) {
        productos.removeIf(p -> p.getIdProducto().equals(idProducto));
    }
}

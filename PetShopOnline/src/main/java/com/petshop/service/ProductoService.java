package com.petshop.service;

import com.petshop.domain.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    // Catalogo temporal mientras no exista la base de datos.
    // Cuando se conecte MySQL, esta lista se reemplaza por un
    // ProductoRepository extends JpaRepository<Producto, Integer>
    // y este service pasa a delegarle listarTodos()/buscarPorId().
    private final List<Producto> productos = new ArrayList<>();

    public ProductoService() {

        productos.add(new Producto(1, "Alimento Premium",
                "Alimento balanceado para perros adultos.",
                25.00,
                "https://images.unsplash.com/photo-1589924691995-400dc9ecc119"));

        productos.add(new Producto(2, "Juguete para Perro",
                "Juguete resistente para mascotas activas.",
                8.50,
                "https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd"));

        productos.add(new Producto(3, "Arena para Gato",
                "Arena absorbente con control de olores.",
                12.00,
                "https://images.unsplash.com/photo-1574158622682-e40e69881006"));

        productos.add(new Producto(4, "Correa Ajustable",
                "Correa cómoda y resistente.",
                10.00,
                "https://images.unsplash.com/photo-1548199973-03cce0bbc87b"));
    }

    public List<Producto> listarTodos() {
        return productos;
    }

    // Devuelve Optional, igual que JpaRepository.findById(id),
    // para que cuando llegue MySQL este metodo no cambie de firma.
    public Optional<Producto> buscarPorId(Integer idProducto) {

        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(idProducto)) {
                return Optional.of(producto);
            }
        }

        return Optional.empty();
    }
}
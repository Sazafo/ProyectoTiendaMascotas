package com.petshop.service;

import com.petshop.domain.Categoria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    // Lista temporal mientras no exista la base de datos.
    // Cuando se conecte MySQL, se reemplaza por CategoriaRepository
    // extends JpaRepository<Categoria, Integer>.
    private final List<Categoria> categorias = new ArrayList<>();
    private Integer siguienteId = 4;

    public CategoriaService() {
        categorias.add(new Categoria(1, "Alimentos",      "Alimentos y nutrición para mascotas"));
        categorias.add(new Categoria(2, "Accesorios",     "Correas, collares y accesorios varios"));
        categorias.add(new Categoria(3, "Higiene",        "Productos de higiene y cuidado personal"));
    }

    public List<Categoria> listarTodas() {
        return categorias;
    }

    public Optional<Categoria> buscarPorId(Integer idCategoria) {
        for (Categoria c : categorias) {
            if (c.getIdCategoria().equals(idCategoria)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public void guardar(Categoria categoria) {
        if (categoria.getIdCategoria() == null) {
            // Nueva categoría
            categoria.setIdCategoria(siguienteId++);
            categorias.add(categoria);
        } else {
            // Editar existente
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getIdCategoria().equals(categoria.getIdCategoria())) {
                    categorias.set(i, categoria);
                    return;
                }
            }
        }
    }

    public void eliminar(Integer idCategoria) {
        categorias.removeIf(c -> c.getIdCategoria().equals(idCategoria));
    }
}

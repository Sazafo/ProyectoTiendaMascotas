package com.petshop.service;

import com.petshop.domain.Categoria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final List<Categoria> categorias = new ArrayList<>();

    private Integer siguienteId = 4;

    public CategoriaService() {

        categorias.add(new Categoria(
                1,
                "Alimentos",
                "Alimentos y nutrición para mascotas."
        ));

        categorias.add(new Categoria(
                2,
                "Accesorios",
                "Accesorios, juguetes y artículos para mascotas."
        ));

        categorias.add(new Categoria(
                3,
                "Higiene",
                "Productos para higiene y cuidado."
        ));
    }

    // LISTAR TODAS
    public List<Categoria> listarTodas() {
        return categorias;
    }

    // BUSCAR POR ID
    public Categoria buscarPorId(Integer idCategoria) {

        for (Categoria categoria : categorias) {

            if (categoria.getIdCategoria().equals(idCategoria)) {
                return categoria;
            }
        }

        return null;
    }

    // GUARDAR O EDITAR
    public void guardar(Categoria categoria) {

        if (categoria.getIdCategoria() == null) {

            categoria.setIdCategoria(siguienteId++);
            categorias.add(categoria);

        } else {

            for (int i = 0; i < categorias.size(); i++) {

                if (categorias.get(i)
                        .getIdCategoria()
                        .equals(categoria.getIdCategoria())) {

                    categorias.set(i, categoria);
                    return;
                }
            }
        }
    }

    // ELIMINAR
    public void eliminar(Integer idCategoria) {

        categorias.removeIf(
                categoria ->
                    categoria.getIdCategoria().equals(idCategoria)
        );
    }
}
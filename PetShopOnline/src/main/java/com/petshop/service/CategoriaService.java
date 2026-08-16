package com.petshop.service;

import com.petshop.domain.Categoria;
import com.petshop.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }

    public Categoria buscarPorId(Integer idCategoria) {
        return categoriaRepository.buscarPorId(idCategoria);
    }

    public void guardar(Categoria categoria) {
        categoriaRepository.guardar(categoria);
    }

    public void eliminar(Integer idCategoria) {
        categoriaRepository.eliminar(idCategoria);
    }
}
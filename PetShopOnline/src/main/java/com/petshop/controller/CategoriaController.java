package com.petshop.controller;

import com.petshop.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias")
    public String categorias(Model model) {

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        return "categorias";
    }
}
package com.petshop.controller;

import com.petshop.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ProductoService productoService;

    public IndexController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String inicio(Model model) {

        model.addAttribute("productos", productoService.listarTodos());

        return "index";
    }
}
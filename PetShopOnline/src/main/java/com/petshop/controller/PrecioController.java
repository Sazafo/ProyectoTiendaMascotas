package com.petshop.controller;

import com.petshop.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrecioController {

    private final ProductoService productoService;

    public PrecioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/precios")
    public String precios(Model model) {

        model.addAttribute(
                "productos",
                productoService.listarPorPrecio()
        );

        return "precios";
    }
}
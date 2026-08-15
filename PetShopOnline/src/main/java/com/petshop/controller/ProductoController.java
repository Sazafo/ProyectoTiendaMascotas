package com.petshop.controller;

import com.petshop.domain.Producto;
import com.petshop.service.CategoriaService;
import com.petshop.service.ProductoService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(
            ProductoService productoService,
            CategoriaService categoriaService) {

        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // LISTADO Y FILTRO POR CATEGORÍA
    @GetMapping("/productos")
    public String listar(
            @RequestParam(required = false) Integer categoria,
            Model model) {

        model.addAttribute(
                "productos",
                productoService.listarPorCategoria(categoria)
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        model.addAttribute(
                "categoriaActual",
                categoria
        );

        return "productos";
    }

    // DETALLE DEL PRODUCTO
@GetMapping("/productos/detalle/{id}")
public String detalle(
        @PathVariable Integer id,
        Model model) {

    Optional<Producto> producto =
            productoService.buscarPorId(id);

    if (producto.isPresent()) {

        Producto productoActual = producto.get();

        model.addAttribute(
                "producto",
                productoActual
        );

        model.addAttribute(
                "categoria",
                categoriaService.buscarPorId(
                        productoActual.getIdCategoria()
                )
        );

        model.addAttribute(
                "relacionados",
                productoService.buscarRelacionados(
                        productoActual.getIdCategoria(),
                        productoActual.getIdProducto()
                )
        );

        return "detalleProducto";
    }

    return "redirect:/productos";
}
}
package com.petshop.controller;

import com.petshop.domain.Producto;
import com.petshop.domain.Usuario;
import com.petshop.service.CategoriaService;
import com.petshop.service.ProductoService;
import com.petshop.service.ResenaService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ResenaService resenaService;

    public ProductoController(
            ProductoService productoService,
            CategoriaService categoriaService,
            ResenaService resenaService) {

        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.resenaService = resenaService;
    }

    // =========================================================
    // LISTADO Y FILTRO POR CATEGORÍA
    // =========================================================

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

    // =========================================================
    // DETALLE DEL PRODUCTO
    // =========================================================

    @GetMapping("/productos/detalle/{id}")
    public String detalle(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        Optional<Producto> producto =
                productoService.buscarPorId(id);

        if (producto.isEmpty()) {
            return "redirect:/productos";
        }

        Producto productoActual =
                producto.get();

        model.addAttribute(
                "producto",
                productoActual
        );

        if (productoActual.getIdCategoria() != null) {

            model.addAttribute(
                    "categoria",
                    categoriaService.buscarPorId(
                            productoActual.getIdCategoria()
                    )
            );

        } else {

            model.addAttribute(
                    "categoria",
                    null
            );
        }

        model.addAttribute(
                "relacionados",
                productoService.buscarRelacionados(
                        productoActual.getIdCategoria(),
                        productoActual.getIdProducto()
                )
        );

        model.addAttribute(
                "resenas",
                resenaService.listarPorProducto(
                        productoActual.getIdProducto()
                )
        );

        Usuario usuario =
                (Usuario) session.getAttribute(
                        "usuarioSesion"
                );

        model.addAttribute(
                "usuarioSesion",
                usuario
        );

        return "detalleProducto";
    }

    // =========================================================
    // GUARDAR RESEÑA
    // =========================================================

    @PostMapping("/productos/{id}/resena")
    public String guardarResena(
            @PathVariable Integer id,
            @RequestParam Integer calificacion,
            @RequestParam(required = false) String comentario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario =
                (Usuario) session.getAttribute(
                        "usuarioSesion"
                );

        if (usuario == null) {

            return "redirect:/login";
        }

        Producto producto =
                productoService
                        .buscarPorId(id)
                        .orElse(null);

        if (producto == null) {

            return "redirect:/productos";
        }

        boolean guardada =
                resenaService.guardar(
                        usuario,
                        producto,
                        calificacion,
                        comentario
                );

        if (guardada) {

            redirectAttributes.addFlashAttribute(
                    "mensajeResena",
                    "Reseña publicada correctamente."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "errorResena",
                    "No se pudo guardar la reseña. Solo puedes realizar una reseña por producto."
            );
        }

        return "redirect:/productos/detalle/" + id;
    }
}
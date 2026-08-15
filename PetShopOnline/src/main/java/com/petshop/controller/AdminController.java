package com.petshop.controller;

import com.petshop.domain.Categoria;
import com.petshop.domain.Producto;
import com.petshop.service.CategoriaService;
import com.petshop.service.ProductoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public AdminController(
            ProductoService productoService,
            CategoriaService categoriaService) {

        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // URI actual para marcar el enlace activo en el sidebar
    @ModelAttribute
    public void agregarUriActual(
            HttpServletRequest request,
            Model model) {

        model.addAttribute(
                "currentUri",
                request.getRequestURI()
        );
    }

    // Comprueba si existe un usuario autenticado
    private boolean noAutenticado(HttpSession session) {

        return session.getAttribute("usuarioSesion") == null;
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping({"", "/"})
    public String dashboard(
            HttpSession session,
            Model model) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        int totalProductos =
                productoService.listarTodos().size();

        int totalCategorias =
                categoriaService.listarTodas().size();

        model.addAttribute(
                "totalProductos",
                totalProductos
        );

        model.addAttribute(
                "totalCategorias",
                totalCategorias
        );

        model.addAttribute(
                "productos",
                productoService.listarTodos()
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        return "admin/dashboard";
    }


    // =========================================================
    // CRUD PRODUCTOS
    // =========================================================

    @GetMapping("/productos")
    public String listarProductos(
            HttpSession session,
            Model model) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "productos",
                productoService.listarTodos()
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        model.addAttribute(
                "producto",
                new Producto()
        );

        return "admin/productos";
    }


    @GetMapping("/productos/editar/{id}")
    public String editarProducto(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        Producto producto =
                productoService.buscarPorId(id)
                        .orElse(new Producto());

        model.addAttribute(
                "producto",
                producto
        );

        model.addAttribute(
                "productos",
                productoService.listarTodos()
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        return "admin/productos";
    }


    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @RequestParam(required = false) Integer idProducto,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam Double precio,
            @RequestParam String imagen,
            @RequestParam(required = false) Integer idCategoria,
            HttpSession session) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        Producto producto = new Producto();

        producto.setIdProducto(idProducto);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setImagen(imagen);
        producto.setIdCategoria(idCategoria);

        productoService.guardar(producto);

        return "redirect:/admin/productos";
    }


    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(
            @PathVariable Integer id,
            HttpSession session) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        productoService.eliminar(id);

        return "redirect:/admin/productos";
    }


    // =========================================================
    // CRUD CATEGORÍAS
    // =========================================================

    @GetMapping("/categorias")
    public String listarCategorias(
            HttpSession session,
            Model model) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        model.addAttribute(
                "categoria",
                new Categoria()
        );

        return "admin/categorias";
    }


    @GetMapping("/categorias/editar/{id}")
    public String editarCategoria(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        Categoria categoria =
                categoriaService.buscarPorId(id);

        if (categoria == null) {
            categoria = new Categoria();
        }

        model.addAttribute(
                "categoria",
                categoria
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarTodas()
        );

        return "admin/categorias";
    }


    @PostMapping("/categorias/guardar")
    public String guardarCategoria(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            HttpSession session) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        Categoria categoria = new Categoria();

        categoria.setIdCategoria(idCategoria);
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        categoriaService.guardar(categoria);

        return "redirect:/admin/categorias";
    }


    @PostMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(
            @PathVariable Integer id,
            HttpSession session) {

        if (noAutenticado(session)) {
            return "redirect:/login";
        }

        categoriaService.eliminar(id);

        return "redirect:/admin/categorias";
    }
}
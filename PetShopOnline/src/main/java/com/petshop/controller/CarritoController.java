package com.petshop.controller;

import com.petshop.domain.Carrito;
import com.petshop.domain.Pedido;
import com.petshop.domain.Usuario;
import com.petshop.service.PedidoService;
import com.petshop.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarritoController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public CarritoController(ProductoService productoService, PedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    // Obtiene el carrito de la sesion actual, o crea uno nuevo si no existe
    private Carrito obtenerCarrito(HttpSession session) {

        Carrito carrito = (Carrito) session.getAttribute("carritoSesion");

        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carritoSesion", carrito);
        }

        return carrito;
    }

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {

        Carrito carrito = obtenerCarrito(session);

        model.addAttribute("carrito", carrito);

        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarProducto(
            @RequestParam Integer idProducto,
            @RequestParam(defaultValue = "1") Integer cantidad,
            HttpSession session) {

        productoService.buscarPorId(idProducto).ifPresent(producto -> {
            Carrito carrito = obtenerCarrito(session);
            carrito.agregarProducto(producto, cantidad);
        });

        return "redirect:/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidad,
            HttpSession session) {

        Carrito carrito = obtenerCarrito(session);
        carrito.actualizarCantidad(idProducto, cantidad);

        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarProducto(
            @RequestParam Integer idProducto,
            HttpSession session) {

        Carrito carrito = obtenerCarrito(session);
        carrito.eliminarProducto(idProducto);

        return "redirect:/carrito";
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        Carrito carrito = obtenerCarrito(session);

        if (carrito.estaVacio()) {
            return "redirect:/carrito";
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("usuario", usuario);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String procesarCheckout(
            @RequestParam String direccion,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        Carrito carrito = obtenerCarrito(session);

        if (carrito.estaVacio()) {
            return "redirect:/carrito";
        }

        // Se arma y se guarda el pedido. Cuando exista MySQL,
        // pedidoService.guardar() hara un INSERT real en vez de
        // guardarlo en una lista en memoria; nada mas cambia aqui.
        Pedido pedido = new Pedido(usuario, carrito.getItems(), carrito.getTotal(), direccion);
        pedido = pedidoService.guardar(pedido);

        carrito.vaciar();

        model.addAttribute("compraExitosa", true);
        model.addAttribute("pedido", pedido);
        model.addAttribute("usuario", usuario);

        return "checkout";
    }
}
package com.petshop.controller;

import com.petshop.domain.Carrito;
import com.petshop.domain.Pedido;
import com.petshop.domain.Usuario;
import com.petshop.service.MovimientoInventarioService;
import com.petshop.service.PagoService;
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
    private final PagoService pagoService;
    private final MovimientoInventarioService movimientoInventarioService;

    public CarritoController(
            ProductoService productoService,
            PedidoService pedidoService,
            PagoService pagoService,
            MovimientoInventarioService movimientoInventarioService) {

        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.pagoService = pagoService;
        this.movimientoInventarioService = movimientoInventarioService;
    }

    // =========================================================
    // OBTENER CARRITO DE LA SESIÓN
    // =========================================================

    private Carrito obtenerCarrito(HttpSession session) {

        Carrito carrito =
                (Carrito) session.getAttribute("carritoSesion");

        if (carrito == null) {

            carrito = new Carrito();

            session.setAttribute(
                    "carritoSesion",
                    carrito
            );
        }

        return carrito;
    }


    // =========================================================
    // MOSTRAR CARRITO
    // =========================================================

    @GetMapping("/carrito")
    public String mostrarCarrito(
            HttpSession session,
            Model model) {

        Carrito carrito =
                obtenerCarrito(session);

        model.addAttribute(
                "carrito",
                carrito
        );

        return "carrito";
    }


    // =========================================================
    // AGREGAR PRODUCTO
    // =========================================================

    @PostMapping("/carrito/agregar")
    public String agregarProducto(
            @RequestParam Integer idProducto,
            @RequestParam(defaultValue = "1") Integer cantidad,
            HttpSession session) {

        productoService
                .buscarPorId(idProducto)
                .ifPresent(producto -> {

                    // Evitar agregar más de lo disponible
                    if (producto.getStock() != null
                            && producto.getStock() >= cantidad
                            && cantidad > 0) {

                        Carrito carrito =
                                obtenerCarrito(session);

                        carrito.agregarProducto(
                                producto,
                                cantidad
                        );
                    }
                });

        return "redirect:/carrito";
    }


    // =========================================================
    // ACTUALIZAR CANTIDAD
    // =========================================================

    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidad,
            HttpSession session) {

        Carrito carrito =
                obtenerCarrito(session);

        productoService
                .buscarPorId(idProducto)
                .ifPresent(producto -> {

                    if (cantidad > 0
                            && producto.getStock() != null
                            && cantidad <= producto.getStock()) {

                        carrito.actualizarCantidad(
                                idProducto,
                                cantidad
                        );
                    }
                });

        return "redirect:/carrito";
    }


    // =========================================================
    // ELIMINAR PRODUCTO
    // =========================================================

    @PostMapping("/carrito/eliminar")
    public String eliminarProducto(
            @RequestParam Integer idProducto,
            HttpSession session) {

        Carrito carrito =
                obtenerCarrito(session);

        carrito.eliminarProducto(
                idProducto
        );

        return "redirect:/carrito";
    }


    // =========================================================
    // MOSTRAR CHECKOUT
    // =========================================================

    @GetMapping("/checkout")
    public String mostrarCheckout(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(
                        "usuarioSesion"
                );

        if (usuario == null) {
            return "redirect:/login";
        }

        Carrito carrito =
                obtenerCarrito(session);

        if (carrito.estaVacio()) {
            return "redirect:/carrito";
        }

        model.addAttribute(
                "carrito",
                carrito
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "checkout";
    }


    // =========================================================
    // PROCESAR COMPRA
    // =========================================================

    @PostMapping("/checkout")
    public String procesarCheckout(
            @RequestParam String direccion,
            @RequestParam(defaultValue = "TARJETA") String metodoPago,
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute(
                        "usuarioSesion"
                );

        if (usuario == null) {
            return "redirect:/login";
        }

        Carrito carrito =
                obtenerCarrito(session);

        if (carrito.estaVacio()) {
            return "redirect:/carrito";
        }


        // =====================================================
        // VALIDAR STOCK ANTES DE COMPRAR
        // =====================================================

        for (var item : carrito.getItems()) {

            var productoActual =
                    productoService
                            .buscarPorId(
                                    item.getProducto()
                                            .getIdProducto()
                            )
                            .orElse(null);

            if (productoActual == null
                    || productoActual.getStock() == null
                    || productoActual.getStock()
                            < item.getCantidad()) {

                model.addAttribute(
                        "error",
                        "No hay suficiente stock para uno de los productos."
                );

                model.addAttribute(
                        "carrito",
                        carrito
                );

                model.addAttribute(
                        "usuario",
                        usuario
                );

                return "checkout";
            }
        }


        // =====================================================
        // CREAR PEDIDO
        // =====================================================

        Pedido pedido =
                new Pedido(
                        usuario,
                        carrito.getItems(),
                        carrito.getTotal(),
                        direccion
                );


        // =====================================================
        // GUARDAR PEDIDO + DETALLES
        // =====================================================

        pedido =
                pedidoService.guardar(
                        pedido
                );


        // =====================================================
        // REGISTRAR PAGO
        // =====================================================

        pagoService.registrarPago(
                pedido,
                metodoPago
        );


        // =====================================================
        // DESCONTAR INVENTARIO
        // =====================================================

        carrito.getItems().forEach(item -> {

            productoService
                    .buscarPorId(
                            item.getProducto()
                                    .getIdProducto()
                    )
                    .ifPresent(producto -> {

                        movimientoInventarioService
                                .registrarSalida(
                                        producto,
                                        item.getCantidad()
                                );
                    });
        });


        // =====================================================
        // VACIAR CARRITO
        // =====================================================

        carrito.vaciar();


        // =====================================================
        // MOSTRAR CONFIRMACIÓN
        // =====================================================

        model.addAttribute(
                "compraExitosa",
                true
        );

        model.addAttribute(
                "pedido",
                pedido
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "checkout";
    }
}
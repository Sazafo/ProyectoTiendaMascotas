package com.petshop.controller;

import com.petshop.domain.Pedido;
import com.petshop.domain.Usuario;
import com.petshop.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos")
    public String listarPedidos(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "pedidos",
                pedidoService.listarPorUsuario(usuario.getIdUsuario()));

        return "pedidos";
    }

    @GetMapping("/pedidos/detalle/{id}")
    public String detallePedido(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        Pedido pedido = pedidoService.buscarPorId(id);

        if (pedido == null || !pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/pedidos";
        }

        model.addAttribute("pedido", pedido);

        return "detallePedido";
    }
}
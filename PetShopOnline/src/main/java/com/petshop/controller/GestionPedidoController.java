package com.petshop.controller;

import com.petshop.domain.Usuario;
import com.petshop.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionPedidoController {

    private final PedidoService pedidoService;

    public GestionPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/admin/pedidos")
    public String listarPedidos(HttpSession session, Model model) {

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuarioSesion.getRol())) {
            return "redirect:/perfil";
        }

        model.addAttribute(
                "pedidos",
                pedidoService.listarTodos());

        return "admin/pedidos";
    }

    @PostMapping("/admin/pedidos/estado")
    public String cambiarEstado(
            @RequestParam Integer idPedido,
            @RequestParam String estado,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuarioSesion.getRol())) {
            return "redirect:/perfil";
        }

        boolean actualizado = pedidoService.cambiarEstado(idPedido, estado);

        if (!actualizado) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se encontró el pedido");

            return "redirect:/admin/pedidos";
        }

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Estado del pedido actualizado");

        return "redirect:/admin/pedidos";
    }
}
package com.petshop.controller;

import com.petshop.domain.Usuario;
import com.petshop.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionUsuarioController {

    private final UsuarioService usuarioService;

    public GestionUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin/usuarios")
    public String listarUsuarios(
            HttpSession session,
            Model model) {

        Usuario usuarioSesion =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuarioSesion.getRol())) {
            return "redirect:/perfil";
        }

        model.addAttribute(
                "usuarios",
                usuarioService.listarUsuarios());

        return "admin/usuarios";
    }

    @PostMapping("/admin/usuario/estado")
    public String cambiarEstado(
            @RequestParam Integer idUsuario,
            @RequestParam boolean activo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuarioSesion =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuarioSesion.getRol())) {
            return "redirect:/perfil";
        }

        // Evita que el administrador desactive su propia cuenta
        if (usuarioSesion.getIdUsuario().equals(idUsuario) && !activo) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No puede desactivar su propia cuenta");

            return "redirect:/admin/usuarios";
        }

        boolean actualizado =
                usuarioService.cambiarEstado(idUsuario, activo);

        if (!actualizado) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se encontró el usuario");

            return "redirect:/admin/usuarios";
        }

        if (activo) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Usuario activado correctamente");

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Usuario desactivado correctamente");
        }

        return "redirect:/admin/usuarios";
    }
}
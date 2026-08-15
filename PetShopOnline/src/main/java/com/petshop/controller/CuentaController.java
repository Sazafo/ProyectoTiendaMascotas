package com.petshop.controller;

import com.petshop.domain.Usuario;
import com.petshop.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CuentaController {

    private final UsuarioService usuarioService;

    public CuentaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/cuenta/cambiar-password")
    public String cambiarPassword() {
        return "cuenta/cambiarPassword";
    }

    @PostMapping("/cuenta/cambiar-password")
    public String cambiarPassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String confirmarPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!passwordNueva.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Las contraseñas nuevas no coinciden");

            return "redirect:/cuenta/cambiar-password";
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        boolean actualizado = usuarioService.cambiarPassword(
                usuario.getIdUsuario(),
                passwordActual,
                passwordNueva);

        if (!actualizado) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "La contraseña actual es incorrecta");

            return "redirect:/cuenta/cambiar-password";
        }

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Contraseña actualizada correctamente");

        return "redirect:/cuenta/cambiar-password";
    }

    @GetMapping("/recuperar-password")
    public String recuperarPassword() {
        return "cuenta/recuperarPassword";
    }

    @PostMapping("/recuperar-password")
    public String recuperarPassword(
            @RequestParam String correo,
            @RequestParam String passwordNueva,
            @RequestParam String confirmarPassword,
            RedirectAttributes redirectAttributes) {

        if (!passwordNueva.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Las contraseñas no coinciden");

            return "redirect:/recuperar-password";
        }

        boolean actualizado = usuarioService.recuperarPassword(
                correo,
                passwordNueva);

        if (!actualizado) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se encontró un usuario con ese correo");

            return "redirect:/recuperar-password";
        }

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Contraseña actualizada correctamente");

        return "redirect:/login";
    }

    @GetMapping("/admin/cambiar-password")
    public String cambiarPasswordAdmin(
            @RequestParam(required = false) Integer idUsuario,
            HttpSession session,
            org.springframework.ui.Model model) {

        Usuario usuarioSesion =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioSesion == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuarioSesion.getRol())) {
            return "redirect:/perfil";
        }

        if (idUsuario != null) {
            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            model.addAttribute("usuario", usuario);
        }

        return "cuenta/cambiarPasswordAdmin";
    }

    @PostMapping("/admin/cambiar-password")
    public String cambiarPasswordAdmin(
            @RequestParam Integer idUsuario,
            @RequestParam String passwordNueva,
            @RequestParam String confirmarPassword,
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

        if (!passwordNueva.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Las contraseñas no coinciden");

            return "redirect:/admin/cambiar-password?idUsuario=" + idUsuario;
        }

        boolean actualizado = usuarioService.cambiarPasswordAdmin(
                idUsuario,
                passwordNueva);

        if (!actualizado) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Usuario no encontrado");

            return "redirect:/admin/cambiar-password";
        }

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Contraseña del usuario actualizada");

        return "redirect:/admin/cambiar-password?idUsuario=" + idUsuario;
    }
}
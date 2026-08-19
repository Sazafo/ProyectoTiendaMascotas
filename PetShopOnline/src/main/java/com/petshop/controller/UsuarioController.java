package com.petshop.controller;

import com.petshop.domain.Usuario;
import com.petshop.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(
            @RequestParam String correo,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioService.buscarPorCorreo(correo);

        if (usuario != null
                && usuario.isActivo()
                && usuario.getPassword().equals(password)) {

            session.setAttribute("usuarioSesion", usuario);

            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/admin";
            }

            return "redirect:/perfil";
        }

        model.addAttribute(
                "error",
                "Correo o contraseña incorrectos");

        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String password,
            @RequestParam String direccion,
            HttpSession session,
            Model model) {

        boolean registrado =
                usuarioService.registrarUsuario(
                        nombre,
                        correo,
                        password,
                        direccion);

        if (!registrado) {

            model.addAttribute(
                    "error",
                    "El correo ya se encuentra registrado");

            return "registro";
        }

        Usuario usuario =
                usuarioService.buscarPorCorreo(correo);

        session.setAttribute(
                "usuarioSesion",
                usuario);

        return "redirect:/perfil";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(
            HttpSession session,
            Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "editarPerfil";
    }

    @PostMapping("/perfil/editar")
    public String guardarPerfil(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String direccion,
            HttpSession session) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setDireccion(direccion);

        usuarioService.actualizarPerfil(usuario);

        Usuario usuarioActualizado =
                usuarioService.buscarPorId(usuario.getIdUsuario());

        session.setAttribute(
                "usuarioSesion",
                usuarioActualizado);

        return "redirect:/perfil";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
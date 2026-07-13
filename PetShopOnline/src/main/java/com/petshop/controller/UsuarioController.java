package com.petshop.controller;

import com.petshop.domain.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

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

        // Login temporal mientras no exista la base de datos
        if (correo.equals("admin@petshop.com")
                && password.equals("1234")) {

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(1);
            usuario.setNombre("Administrador");
            usuario.setCorreo(correo);

            session.setAttribute("usuarioSesion", usuario);

            return "redirect:/perfil";
        }

        model.addAttribute("error", "Correo o contraseña incorrectos");

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
            HttpSession session) {

        // Registro temporal mientras no exista la base de datos
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(password);

        session.setAttribute("usuarioSesion", usuario);

        return "redirect:/perfil";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {

        Usuario usuario =
                (Usuario) session.getAttribute("usuarioSesion");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
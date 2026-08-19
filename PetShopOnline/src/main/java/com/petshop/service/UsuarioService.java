package com.petshop.service;

import com.petshop.domain.Usuario;
import com.petshop.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.buscarPorCorreo(correo);
    }

    public Usuario buscarPorId(Integer idUsuario) {
        return usuarioRepository.buscarPorId(idUsuario);
    }

    public boolean cambiarPassword(
            Integer idUsuario,
            String passwordActual,
            String passwordNueva) {

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        if (!usuario.getPassword().equals(passwordActual)) {
            return false;
        }

        usuarioRepository.cambiarPassword(
                idUsuario,
                passwordNueva);

        return true;
    }

    public boolean cambiarPasswordAdmin(
            Integer idUsuario,
            String passwordNueva) {

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        usuarioRepository.cambiarPassword(
                idUsuario,
                passwordNueva);

        return true;
    }

    public boolean recuperarPassword(
            String correo,
            String passwordNueva) {

        Usuario usuario = usuarioRepository.buscarPorCorreo(correo);

        if (usuario == null) {
            return false;
        }

        usuarioRepository.cambiarPassword(
                usuario.getIdUsuario(),
                passwordNueva);

        return true;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarUsuarios();
    }

    public boolean cambiarEstado(
            Integer idUsuario,
            boolean activo) {

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        usuarioRepository.cambiarEstado(
                idUsuario,
                activo);

        return true;
    }

    public boolean registrarUsuario(
            String nombre,
            String correo,
            String password,
            String direccion) {

        if (usuarioRepository.existeCorreo(correo)) {
            return false;
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(password);
        usuario.setDireccion(direccion);
        usuario.setRol("USER");
        usuario.setActivo(true);

        usuarioRepository.registrarUsuario(usuario);

        return true;
    }

    public void actualizarPerfil(Usuario usuario) {
        usuarioRepository.actualizarPerfil(usuario);
    }
}
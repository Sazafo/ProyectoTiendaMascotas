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

    // =====================================================
    // BUSCAR POR CORREO
    // =====================================================
    public Usuario buscarPorCorreo(String correo) {

        return usuarioRepository
                .findByCorreo(correo)
                .orElse(null);
    }


    // =====================================================
    // BUSCAR POR ID
    // =====================================================
    public Usuario buscarPorId(Integer idUsuario) {

        return usuarioRepository
                .findById(idUsuario)
                .orElse(null);
    }


    // =====================================================
    // LISTAR USUARIOS
    // =====================================================
    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll();
    }


    // =====================================================
    // REGISTRAR USUARIO
    // =====================================================
    public boolean registrarUsuario(
            String nombre,
            String correo,
            String password,
            String direccion) {

        // Verifica que el correo no exista
        if (usuarioRepository.existsByCorreo(correo)) {
            return false;
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(password);
        usuario.setDireccion(direccion);
        usuario.setRol("USER");
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        return true;
    }


    // =====================================================
    // ACTUALIZAR PERFIL
    // =====================================================
    public void actualizarPerfil(Usuario usuario) {

        usuarioRepository.save(usuario);
    }


    // =====================================================
    // CAMBIAR CONTRASEÑA
    // =====================================================
    public boolean cambiarPassword(
            Integer idUsuario,
            String passwordActual,
            String passwordNueva) {

        Usuario usuario = buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        if (!usuario.getPassword().equals(passwordActual)) {
            return false;
        }

        usuario.setPassword(passwordNueva);

        usuarioRepository.save(usuario);

        return true;
    }


    // =====================================================
    // CAMBIAR CONTRASEÑA ADMIN
    // =====================================================
    public boolean cambiarPasswordAdmin(
            Integer idUsuario,
            String passwordNueva) {

        Usuario usuario = buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        usuario.setPassword(passwordNueva);

        usuarioRepository.save(usuario);

        return true;
    }


    // =====================================================
    // RECUPERAR CONTRASEÑA
    // =====================================================
    public boolean recuperarPassword(
            String correo,
            String passwordNueva) {

        Usuario usuario = buscarPorCorreo(correo);

        if (usuario == null) {
            return false;
        }

        usuario.setPassword(passwordNueva);

        usuarioRepository.save(usuario);

        return true;
    }


    // =====================================================
    // ACTIVAR / DESACTIVAR USUARIO
    // =====================================================
    public boolean cambiarEstado(
            Integer idUsuario,
            boolean activo) {

        Usuario usuario = buscarPorId(idUsuario);

        if (usuario == null) {
            return false;
        }

        usuario.setActivo(activo);

        usuarioRepository.save(usuario);

        return true;
    }
}
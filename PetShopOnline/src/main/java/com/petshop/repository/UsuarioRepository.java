package com.petshop.repository;

import com.petshop.domain.Usuario;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Usuario buscarPorCorreo(String correo) {

        String sql = "SELECT id_usuario, nombre, correo, password, direccion, rol, activo "
                + "FROM usuario WHERE correo = ?";

        List<Usuario> usuarios = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("direccion"),
                        rs.getString("rol"),
                        rs.getBoolean("activo")
                ),
                correo
        );

        if (usuarios.isEmpty()) {
            return null;
        }

        return usuarios.get(0);
    }

    public Usuario buscarPorId(Integer idUsuario) {

        String sql = "SELECT id_usuario, nombre, correo, password, direccion, rol, activo "
                + "FROM usuario WHERE id_usuario = ?";

        List<Usuario> usuarios = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("direccion"),
                        rs.getString("rol"),
                        rs.getBoolean("activo")
                ),
                idUsuario
        );

        if (usuarios.isEmpty()) {
            return null;
        }

        return usuarios.get(0);
    }

    public void cambiarPassword(Integer idUsuario, String password) {

        String sql = "UPDATE usuario SET password = ? WHERE id_usuario = ?";

        jdbcTemplate.update(sql, password, idUsuario);
    }

    public List<Usuario> listarUsuarios() {

        String sql = "SELECT id_usuario, nombre, correo, password, direccion, rol, activo "
                + "FROM usuario ORDER BY nombre";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("direccion"),
                        rs.getString("rol"),
                        rs.getBoolean("activo")
                )
        );
    }

    public void cambiarEstado(Integer idUsuario, boolean activo) {

        String sql = "UPDATE usuario SET activo = ? WHERE id_usuario = ?";

        jdbcTemplate.update(sql, activo, idUsuario);
    }

    public boolean existeCorreo(String correo) {

        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";

        Integer cantidad = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                correo
        );

        return cantidad != null && cantidad > 0;
    }

    public void registrarUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuario "
                + "(nombre, correo, password, direccion, rol, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getPassword(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.isActivo()
        );
    }

    public void actualizarPerfil(Usuario usuario) {

        String sql = "UPDATE usuario "
                + "SET nombre = ?, correo = ?, direccion = ? "
                + "WHERE id_usuario = ?";

        jdbcTemplate.update(
                sql,
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getDireccion(),
                usuario.getIdUsuario()
        );
    }
}
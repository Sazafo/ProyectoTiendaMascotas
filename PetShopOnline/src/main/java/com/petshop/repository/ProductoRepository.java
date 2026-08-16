package com.petshop.repository;

import com.petshop.domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Producto> listarTodos() {

        String sql = """
                SELECT id_producto, nombre, descripcion,
                       precio, imagen, id_categoria
                FROM producto
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("imagen"),
                        rs.getObject("id_categoria", Integer.class)
                )
        );
    }

    public Optional<Producto> buscarPorId(Integer idProducto) {

        String sql = """
                SELECT id_producto, nombre, descripcion,
                       precio, imagen, id_categoria
                FROM producto
                WHERE id_producto = ?
                """;

        List<Producto> resultado = jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new Producto(
                                rs.getInt("id_producto"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                rs.getString("imagen"),
                                rs.getObject("id_categoria", Integer.class)
                        ),
                idProducto
        );

        return resultado.stream().findFirst();
    }

    public List<Producto> listarPorCategoria(Integer idCategoria) {

        String sql = """
                SELECT id_producto, nombre, descripcion,
                       precio, imagen, id_categoria
                FROM producto
                WHERE id_categoria = ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new Producto(
                                rs.getInt("id_producto"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                rs.getString("imagen"),
                                rs.getObject("id_categoria", Integer.class)
                        ),
                idCategoria
        );
    }

    public List<Producto> listarPorPrecio() {

        String sql = """
                SELECT id_producto, nombre, descripcion,
                       precio, imagen, id_categoria
                FROM producto
                ORDER BY precio ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("imagen"),
                        rs.getObject("id_categoria", Integer.class)
                )
        );
    }

    public void guardar(Producto producto) {

        if (producto.getIdProducto() == null) {

            String sql = """
                    INSERT INTO producto
                    (nombre, descripcion, precio, imagen, id_categoria)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(
                    sql,
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getImagen(),
                    producto.getIdCategoria()
            );

        } else {

            String sql = """
                    UPDATE producto
                    SET nombre = ?,
                        descripcion = ?,
                        precio = ?,
                        imagen = ?,
                        id_categoria = ?
                    WHERE id_producto = ?
                    """;

            jdbcTemplate.update(
                    sql,
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getImagen(),
                    producto.getIdCategoria(),
                    producto.getIdProducto()
            );
        }
    }

    public void eliminar(Integer idProducto) {

        String sql = """
                DELETE FROM producto
                WHERE id_producto = ?
                """;

        jdbcTemplate.update(sql, idProducto);
    }
}
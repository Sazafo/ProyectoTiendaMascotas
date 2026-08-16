package com.petshop.repository;

import com.petshop.domain.Categoria;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoriaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Categoria> listarTodas() {

        String sql = """
                SELECT id_categoria, nombre, descripcion
                FROM categoria
                ORDER BY id_categoria
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                )
        );
    }

    public Categoria buscarPorId(Integer idCategoria) {

        String sql = """
                SELECT id_categoria, nombre, descripcion
                FROM categoria
                WHERE id_categoria = ?
                """;

        List<Categoria> resultado = jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new Categoria(
                                rs.getInt("id_categoria"),
                                rs.getString("nombre"),
                                rs.getString("descripcion")
                        ),
                idCategoria
        );

        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }

    public void guardar(Categoria categoria) {

        if (categoria.getIdCategoria() == null) {

            String sql = """
                    INSERT INTO categoria
                    (nombre, descripcion)
                    VALUES (?, ?)
                    """;

            jdbcTemplate.update(
                    sql,
                    categoria.getNombre(),
                    categoria.getDescripcion()
            );

        } else {

            String sql = """
                    UPDATE categoria
                    SET nombre = ?,
                        descripcion = ?
                    WHERE id_categoria = ?
                    """;

            jdbcTemplate.update(
                    sql,
                    categoria.getNombre(),
                    categoria.getDescripcion(),
                    categoria.getIdCategoria()
            );
        }
    }

    public void eliminar(Integer idCategoria) {

        String sql = """
                DELETE FROM categoria
                WHERE id_categoria = ?
                """;

        jdbcTemplate.update(sql, idCategoria);
    }
}
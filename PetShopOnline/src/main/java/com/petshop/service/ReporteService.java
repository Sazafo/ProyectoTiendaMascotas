package com.petshop.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    public ReporteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ----------------------------------------------------------
    // Total de ventas en un rango de fechas
    // ----------------------------------------------------------
    public Double totalVentas(LocalDate desde, LocalDate hasta) {

        String sql = "SELECT COALESCE(SUM(total), 0) "
                + "FROM pedido "
                + "WHERE DATE(fecha) BETWEEN ? AND ?";

        Double resultado = jdbcTemplate.queryForObject(
                sql, Double.class,
                desde.toString(), hasta.toString());

        return resultado != null ? resultado : 0.0;
    }

    // ----------------------------------------------------------
    // Cantidad de pedidos en un rango de fechas
    // ----------------------------------------------------------
    public Integer totalPedidos(LocalDate desde, LocalDate hasta) {

        String sql = "SELECT COUNT(*) "
                + "FROM pedido "
                + "WHERE DATE(fecha) BETWEEN ? AND ?";

        Integer resultado = jdbcTemplate.queryForObject(
                sql, Integer.class,
                desde.toString(), hasta.toString());

        return resultado != null ? resultado : 0;
    }

    // ----------------------------------------------------------
    // Ventas agrupadas por fecha (para la tabla de rango)
    // ----------------------------------------------------------
    public List<Map<String, Object>> ventasPorFecha(LocalDate desde, LocalDate hasta) {

        String sql = "SELECT DATE(fecha) AS fecha, "
                + "       COUNT(*)       AS pedidos, "
                + "       SUM(total)     AS total "
                + "FROM pedido "
                + "WHERE DATE(fecha) BETWEEN ? AND ? "
                + "GROUP BY DATE(fecha) "
                + "ORDER BY DATE(fecha) DESC";

        return jdbcTemplate.queryForList(
                sql, desde.toString(), hasta.toString());
    }

    // ----------------------------------------------------------
    // Top 10 productos más vendidos (por cantidad de unidades)
    // ----------------------------------------------------------
    public List<Map<String, Object>> productosMasVendidos(LocalDate desde, LocalDate hasta) {

        String sql = "SELECT p.nombre                  AS producto, "
                + "       SUM(dp.cantidad)          AS unidades, "
                + "       SUM(dp.cantidad * dp.precio_unitario) AS ingresos "
                + "FROM detalle_pedido dp "
                + "JOIN producto  p  ON p.id_producto = dp.id_producto "
                + "JOIN pedido    pe ON pe.id_pedido  = dp.id_pedido "
                + "WHERE DATE(pe.fecha) BETWEEN ? AND ? "
                + "GROUP BY p.id_producto, p.nombre "
                + "ORDER BY unidades DESC "
                + "LIMIT 10";

        return jdbcTemplate.queryForList(
                sql, desde.toString(), hasta.toString());
    }

    // ----------------------------------------------------------
    // Ventas agrupadas por categoría
    // ----------------------------------------------------------
    public List<Map<String, Object>> ventasPorCategoria(LocalDate desde, LocalDate hasta) {

        String sql = "SELECT c.nombre                              AS categoria, "
                + "       SUM(dp.cantidad)                     AS unidades, "
                + "       SUM(dp.cantidad * dp.precio_unitario) AS ingresos "
                + "FROM detalle_pedido dp "
                + "JOIN producto  p  ON p.id_producto  = dp.id_producto "
                + "JOIN categoria c  ON c.id_categoria = p.id_categoria "
                + "JOIN pedido    pe ON pe.id_pedido   = dp.id_pedido "
                + "WHERE DATE(pe.fecha) BETWEEN ? AND ? "
                + "GROUP BY c.id_categoria, c.nombre "
                + "ORDER BY ingresos DESC";

        return jdbcTemplate.queryForList(
                sql, desde.toString(), hasta.toString());
    }
}

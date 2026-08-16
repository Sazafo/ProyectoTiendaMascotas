package com.petshop.controller;

import com.petshop.domain.Usuario;
import com.petshop.service.ReporteService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // Protección de acceso admin (igual que los demás controllers)
    private boolean noAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioSesion");
        return u == null || !"ADMIN".equals(u.getRol());
    }

    // ----------------------------------------------------------
    // GET /admin/reportes  — muestra el formulario y los resultados
    // ----------------------------------------------------------
    @GetMapping
    public String mostrarReportes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session,
            Model model) {

        if (noAdmin(session)) {
            return "redirect:/login";
        }

        // Rango por defecto: mes actual
        if (desde == null) {
            desde = LocalDate.now().withDayOfMonth(1);
        }
        if (hasta == null) {
            hasta = LocalDate.now();
        }

        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        model.addAttribute("totalVentas",
                reporteService.totalVentas(desde, hasta));

        model.addAttribute("totalPedidos",
                reporteService.totalPedidos(desde, hasta));

        model.addAttribute("ventasPorFecha",
                reporteService.ventasPorFecha(desde, hasta));

        model.addAttribute("productosMasVendidos",
                reporteService.productosMasVendidos(desde, hasta));

        model.addAttribute("ventasPorCategoria",
                reporteService.ventasPorCategoria(desde, hasta));

        return "admin/reportes";
    }

    // ----------------------------------------------------------
    // GET /admin/reportes/exportar  — descarga CSV
    // ----------------------------------------------------------
    @GetMapping("/exportar")
    public void exportarCsv(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session,
            HttpServletResponse response) throws IOException {

        if (noAdmin(session)) {
            response.sendRedirect("/login");
            return;
        }

        if (desde == null) {
            desde = LocalDate.now().withDayOfMonth(1);
        }
        if (hasta == null) {
            hasta = LocalDate.now();
        }

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"reporte_ventas_" + desde + "_" + hasta + ".csv\"");

        PrintWriter writer = response.getWriter();

        // --- Totales ---
        writer.println("REPORTE DE VENTAS");
        writer.println("Desde:," + desde + ",Hasta:," + hasta);
        writer.println("Total ventas (₡):," + reporteService.totalVentas(desde, hasta));
        writer.println("Total pedidos:," + reporteService.totalPedidos(desde, hasta));
        writer.println();

        // --- Ventas por fecha ---
        writer.println("VENTAS POR FECHA");
        writer.println("Fecha,Pedidos,Total (₡)");
        for (Map<String, Object> fila : reporteService.ventasPorFecha(desde, hasta)) {
            writer.println(
                    fila.get("fecha") + ","
                    + fila.get("pedidos") + ","
                    + fila.get("total"));
        }
        writer.println();

        // --- Productos más vendidos ---
        writer.println("PRODUCTOS MAS VENDIDOS");
        writer.println("Producto,Unidades vendidas,Ingresos (₡)");
        for (Map<String, Object> fila : reporteService.productosMasVendidos(desde, hasta)) {
            writer.println(
                    "\"" + fila.get("producto") + "\","
                    + fila.get("unidades") + ","
                    + fila.get("ingresos"));
        }
        writer.println();

        // --- Ventas por categoría ---
        writer.println("VENTAS POR CATEGORIA");
        writer.println("Categoria,Unidades,Ingresos (₡)");
        for (Map<String, Object> fila : reporteService.ventasPorCategoria(desde, hasta)) {
            writer.println(
                    "\"" + fila.get("categoria") + "\","
                    + fila.get("unidades") + ","
                    + fila.get("ingresos"));
        }

        writer.flush();
    }
}

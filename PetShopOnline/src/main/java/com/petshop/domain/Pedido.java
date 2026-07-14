package com.petshop.domain;

import java.time.LocalDateTime;
import java.util.List;

// Representa una compra ya confirmada.
// Cuando exista MySQL, esta clase se convierte en @Entity
// (idPedido -> @Id @GeneratedValue, usuario -> @ManyToOne,
// items -> tabla detalle_pedido con @OneToMany).
public class Pedido {

    private Integer idPedido;
    private Usuario usuario;
    private List<ItemCarrito> items;
    private Double total;
    private String direccionEnvio;
    private LocalDateTime fecha;

    public Pedido() {
    }

    public Pedido(Usuario usuario, List<ItemCarrito> items, Double total, String direccionEnvio) {
        this.usuario = usuario;
        this.items = items;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
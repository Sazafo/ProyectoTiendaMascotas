package com.petshop.domain;

import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private List<ItemCarrito> items = new ArrayList<>();

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    // Agrega un producto al carrito. Si ya existe, suma la cantidad.
    public void agregarProducto(Producto producto, Integer cantidad) {

        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto().equals(producto.getIdProducto())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }

        items.add(new ItemCarrito(producto, cantidad));
    }

    // Cambia la cantidad de un producto ya existente en el carrito.
    public void actualizarCantidad(Integer idProducto, Integer cantidad) {

        if (cantidad <= 0) {
            eliminarProducto(idProducto);
            return;
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                item.setCantidad(cantidad);
                return;
            }
        }
    }

    // Elimina un producto del carrito.
    public void eliminarProducto(Integer idProducto) {
        items.removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public Integer getCantidadTotal() {
        int total = 0;
        for (ItemCarrito item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public Double getTotal() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}
package com.petshop.domain;

public class Producto {

    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    private Integer idCategoria;

    public Producto() {
    }

    public Producto(Integer idProducto, String nombre, String descripcion,
                    Double precio, String imagen, Integer idCategoria) {
        this.idProducto  = idProducto;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.imagen      = imagen;
        this.idCategoria = idCategoria;
    }

    // Constructor sin categoría para compatibilidad con código existente
    public Producto(Integer idProducto, String nombre, String descripcion,
                    Double precio, String imagen) {
        this(idProducto, nombre, descripcion, precio, imagen, null);
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
}

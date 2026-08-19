package com.petshop.domain;

public class Usuario {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String password;
    private String direccion;
    private String rol;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String nombre, String correo,
            String password, String direccion, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.direccion = direccion;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
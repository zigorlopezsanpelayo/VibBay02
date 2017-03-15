package com.example.zigorlopezsanpelayo.vibbayza;

/**
 * Created by zigorlopezsanpelayo on 15/3/17.
 */

public class Articulos {

    private int id;
    private String titulo;
    private String nombreImagen;
    private String email;
    private boolean estado;
    private float precio;

    public Articulos(int id, String titulo, String nombreImagen, String email, boolean estado, float precio) {
        this.id = id;
        this.titulo = titulo;
        this.nombreImagen = nombreImagen;
        this.email = email;
        this.estado = estado;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEstado() {
        return estado;
    }

    public float getPrecio() {
        return precio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}

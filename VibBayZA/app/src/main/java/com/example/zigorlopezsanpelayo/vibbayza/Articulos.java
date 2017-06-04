package com.example.zigorlopezsanpelayo.vibbayza;


/**
 * Created by zigorlopezsanpelayo on 15/3/17.
 */

public class Articulos {

    private String titulo;
    private String nombreImagen;
    private String email;
    private boolean estado;
    private double precio;
    private double pujaMaxima;

    public Articulos(String titulo, String nombreImagen, String email, boolean estado, double precio, double pujaMaxima) {
        this.titulo = titulo;
        this.nombreImagen = nombreImagen;
        this.email = email;
        this.estado = estado;
        this.precio = precio;
        this.pujaMaxima = pujaMaxima;
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

    public double getPrecio() {
        return precio;
    }

    public double getPujaMaxima() {
        return pujaMaxima;
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

    public void setPujaMaxima(float pujaMaxima) {
        this.pujaMaxima = pujaMaxima;
    }
}

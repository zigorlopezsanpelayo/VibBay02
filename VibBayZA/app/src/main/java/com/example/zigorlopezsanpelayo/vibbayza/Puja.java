package com.example.zigorlopezsanpelayo.vibbayza;

/**
 * Created by zigorlopezsanpelayo on 14/4/17.
 */

public class Puja {

    private String email;
    private String cantidad;
    private String titulo;

    public Puja() {

    }

    public Puja(String email, String cantidad, String titulo) {
        this.email = email;
        this.cantidad = cantidad;
        this. titulo = titulo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

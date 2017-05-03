package com.example.zigorlopezsanpelayo.vibbayza;

/**
 * Created by zigorlopezsanpelayo on 14/4/17.
 */

public class Puja {

    private String email;
    private double cantidad;
    private String titulo;

    public Puja() {

    }

    public Puja(String email, double cantidad, String titulo) {
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

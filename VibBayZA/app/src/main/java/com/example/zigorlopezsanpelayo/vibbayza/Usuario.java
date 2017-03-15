package com.example.zigorlopezsanpelayo.vibbayza;

/**
 * Created by zigorlopezsanpelayo on 15/3/17.
 */

public class Usuario {
    private String email;
    private String pass;

    public Usuario (String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

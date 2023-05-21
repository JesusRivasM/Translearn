package com.example.translearn;

public class Traduccion {
    private String aTraducir;
    private String Traducido;

    public Traduccion(){

    }
    public Traduccion(String aTraducir, String traducido) {
        this.aTraducir = aTraducir;
        Traducido = traducido;
    }
    public String getaTraducir() {
        return aTraducir;
    }

    public void setaTraducir(String aTraducir) {
        this.aTraducir = aTraducir;
    }

    public String getTraducido() {
        return Traducido;
    }

    public void setTraducido(String traducido) {
        Traducido = traducido;
    }
}

package com.practica.ismael.foodcal.data.model;

public class Food implements Comparable<Food> {

    private int idcomida;
    private String idusuario;
    private String nombrecomida;

    public Food(int idComida, String idUsuario, String nombreComida) {
        this.idcomida = idComida;
        this.idusuario = idUsuario;
        this.nombrecomida = nombreComida;
    }

    public int getIdcomida() {
        return idcomida;
    }

    public void setIdcomida(int idcomida) {
        this.idcomida = idcomida;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombrecomida() {
        return nombrecomida;
    }

    public void setNombrecomida(String nombrecomida) {
        this.nombrecomida = nombrecomida;
    }

    @Override
    public int compareTo(Food o) {
        return this.getNombrecomida().compareTo(o.getNombrecomida());
    }
}

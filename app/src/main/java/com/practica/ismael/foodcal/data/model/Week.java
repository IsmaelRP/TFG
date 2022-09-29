package com.practica.ismael.foodcal.data.model;

public class Week implements Comparable<Week>{

    private int idSemana;
    private String idUsuario;
    private String nombreSemana;

    public Week(int idSemana, String idUsuario, String nombreSemana) {
        this.idSemana = idSemana;
        this.idUsuario = idUsuario;
        this.nombreSemana = nombreSemana;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreSemana() {
        return nombreSemana;
    }

    public void setNombreSemana(String nombreSemana) {
        this.nombreSemana = nombreSemana;
    }

    @Override
    public int compareTo(Week o) {
        return this.getNombreSemana().compareTo(o.getNombreSemana());
    }
}

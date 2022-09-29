package com.practica.ismael.foodcal.data.model;

public class Stretch {

    private String idtramo;
    private String idusuario;
    private int idsemana;
    private String tipotramo;
    private String dia;

    public Stretch(String idtramo, String idusuario, int idsemana, String tipotramo, String dia) {
        this.idtramo = idtramo;
        this.idusuario = idusuario;
        this.idsemana = idsemana;
        this.tipotramo = tipotramo;
        this.dia = dia;
    }

    public String getIdtramo() {
        return idtramo;
    }

    public void setIdtramo(String idtramo) {
        this.idtramo = idtramo;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdsemana() {
        return idsemana;
    }

    public void setIdsemana(int idsemana) {
        this.idsemana = idsemana;
    }

    public String getTipotramo() {
        return tipotramo;
    }

    public void setTipotramo(String tipotramo) {
        this.tipotramo = tipotramo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}

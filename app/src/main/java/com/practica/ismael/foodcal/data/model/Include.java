package com.practica.ismael.foodcal.data.model;

public class Include {

    private String idtramo;
    private String idusuario;
    private int idsemana;
    private int secuencialidad;
    private int idcomidaprincipal;

    public Include(String idtramo, String idusuario, int idsemana, int secuencialidad, int idcomidaprincipal) {
        this.idtramo = idtramo;
        this.idusuario = idusuario;
        this.idsemana = idsemana;
        this.secuencialidad = secuencialidad;
        this.idcomidaprincipal = idcomidaprincipal;
    }

    public Include() {
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

    public int getSecuencialidad() {
        return secuencialidad;
    }

    public void setSecuencialidad(int secuencialidad) {
        this.secuencialidad = secuencialidad;
    }

    public int getIdcomidaprincipal() {
        return idcomidaprincipal;
    }

    public void setIdcomidaprincipal(int idcomidaprincipal) {
        this.idcomidaprincipal = idcomidaprincipal;
    }

}

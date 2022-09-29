package com.practica.ismael.foodcal.data.model;


public class Contains {

    private int idcalendario;

    private String idusuariocalendario;

    private String fecha;

    private int idcomidaprincipal;

    private String tipocomida;

    public Contains(int idcalendario, String idusuariocalendario, String fecha, int idcomida, String tipocomida) {
        this.idcalendario = idcalendario;
        this.idusuariocalendario = idusuariocalendario;
        this.fecha = fecha;
        this.idcomidaprincipal = idcomida;
        this.tipocomida = tipocomida;
    }

    public Contains() {
    }

    public int getIdcalendario() {
        return idcalendario;
    }

    public void setIdcalendario(int idcalendario) {
        this.idcalendario = idcalendario;
    }

    public String getIdusuariocalendario() {
        return idusuariocalendario;
    }

    public void setIdusuariocalendario(String idusuariocalendario) {
        this.idusuariocalendario = idusuariocalendario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdcomidaprincipal() {
        return idcomidaprincipal;
    }

    public void setIdcomidaprincipal(int idcomidaprincipal) {
        this.idcomidaprincipal = idcomidaprincipal;
    }

    public String getTipocomida() {
        return tipocomida;
    }

    public void setTipocomida(String tipocomida) {
        this.tipocomida = tipocomida;
    }

}

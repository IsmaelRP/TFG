package com.practica.ismael.foodcal.data.model;

public class User {

    private String emailusuario;
    private String nombreusuario;
    private String contrasenausuario;

    public User(){

    }

    public User(String emailusuario, String nombreUsuario, String contrasenausuario) {
        this.emailusuario = emailusuario;
        this.nombreusuario = nombreUsuario;
        this.contrasenausuario = contrasenausuario;
    }

    public String getNombreUsuario() {
        return nombreusuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreusuario = nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailusuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailusuario = emailUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenausuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenausuario = contrasenaUsuario;
    }
}

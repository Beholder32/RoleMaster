package modelo;

import java.io.InputStream;

public class Personaje {
    
    int id;
    String nom;
    String raza;
    String clase;
    int fuerza;
    int inteligencia;
    int arma;
    int grupo;
    byte[] foto;
    
    
    
    public Personaje(){}

    public Personaje(int id, String nom, String raza, String clase, int fuerza, int inteligencia, int arma, int grupo, byte[] foto) {
        this.id = id;
        this.nom = nom;
        this.raza = raza;
        this.clase = clase;
        this.fuerza = fuerza;
        this.inteligencia = inteligencia;
        this.arma = arma;
        this.grupo = grupo;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public int getArma() {
        return arma;
    }

    public void setArma(int arma) {
        this.arma = arma;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
}

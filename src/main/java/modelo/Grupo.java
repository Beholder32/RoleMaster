package modelo;

public class Grupo {
    int id;
    String nom;
    String area;
    
    public Grupo(){}

    public Grupo(int id, String nom, String area) {
        this.id = id;
        this.nom = nom;
        this.area = area;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }   
}

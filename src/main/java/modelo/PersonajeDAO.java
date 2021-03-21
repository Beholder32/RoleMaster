package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonajeDAO {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    FileInputStream fi;
    
    public int agregar(Personaje p){
        String sql="INSERT INTO aventurero (nombreAv, raza, clase, fuerza, inteligencia, arma, grupo, foto) VALUES (?,?,?,?,?,?,?,?);";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getRaza());
            ps.setString(3, p.getClase());
            ps.setInt(4, p.getFuerza());
            ps.setInt(5, p.getInteligencia());
            ps.setInt(6, p.getArma());
            ps.setInt(7, p.getGrupo());
            ps.setBytes(8, p.getFoto());
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al agregar Aventurero");
        }
        return 1; 
    }
    
    public List listar(){
        List<Personaje>datos = new ArrayList<>();
        String sql = "SELECT * FROM aventurero";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Personaje p = new Personaje();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setRaza(rs.getString(3));
                p.setClase(rs.getString(4));
                p.setFuerza(rs.getInt(5));
                p.setInteligencia(rs.getInt(6));
                p.setArma(rs.getInt(7));
                p.setGrupo(rs.getInt(8));
                p.setFoto(rs.getBytes(9));
                datos.add(p);
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en el listado");
        }
        return datos;
    }
    
    public void eliminar(int id){
        String sql = "DELETE FROM aventurero WHERE idAventurero="+id;
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al borrar un aventurero");
        }
    }
    
    public int actualizar(Personaje p){
        String sql = "UPDATE aventurero SET nombreAv=?, raza=?, clase=?, fuerza=?, inteligencia=?, arma=?, grupo=? WHERE idAventurero=?";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getRaza());
            ps.setString(3, p.getClase());
            ps.setInt(4, p.getFuerza());
            ps.setInt(5, p.getInteligencia());
            ps.setInt(6, p.getArma());
            ps.setInt(7, p.getGrupo());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch (Exception e){
            System.out.println("Error al actualizar los datos");
        }
        return 1;
    }
    
    public List listarPorNombre(String nom){
        List<Personaje>datos = new ArrayList<>();
        String sql = "SELECT * FROM aventurero WHERE nombreAv = ?";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);
            rs = ps.executeQuery();
            while (rs.next()){
                Personaje p = new Personaje();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setRaza(rs.getString(3));
                p.setClase(rs.getString(4));
                p.setFuerza(rs.getInt(5));
                p.setInteligencia(rs.getInt(6));
                p.setArma(rs.getInt(7));
                p.setGrupo(rs.getInt(8));
                p.setFoto(rs.getBytes(9));
                datos.add(p);
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en el listado");
        }
        return datos;
    }
    
    public List listarPorClase(String nom){
        List<Personaje>datos = new ArrayList<>();
        String sql = "SELECT * FROM aventurero WHERE clase = ?";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);
            rs = ps.executeQuery();
            while (rs.next()){
                Personaje p = new Personaje();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setRaza(rs.getString(3));
                p.setClase(rs.getString(4));
                p.setFuerza(rs.getInt(5));
                p.setInteligencia(rs.getInt(6));
                p.setArma(rs.getInt(7));
                p.setGrupo(rs.getInt(8));
                p.setFoto(rs.getBytes(9));
                datos.add(p);
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en el listado");
        }
        return datos;
    }
    
    public List listarPorGrupo(String nom){
        List<Personaje>datos = new ArrayList<>();
        String sql = "SELECT * FROM aventurero INNER JOIN grupoaventureros ON aventurero.grupo = grupoaventureros.idGrupo WHERE grupoaventureros.nombreGrupo = ?;";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);
            rs = ps.executeQuery();
            while (rs.next()){
                Personaje p = new Personaje();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setRaza(rs.getString(3));
                p.setClase(rs.getString(4));
                p.setFuerza(rs.getInt(5));
                p.setInteligencia(rs.getInt(6));
                p.setArma(rs.getInt(7));
                p.setGrupo(rs.getInt(8));
                p.setFoto(rs.getBytes(9));
                datos.add(p);
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en el listado");
        }
        return datos;
    }
    
}

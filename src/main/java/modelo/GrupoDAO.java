package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public List listar(){
        List<Grupo>datos = new ArrayList<>();
        String sql = "SELECT * FROM grupoaventureros";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Grupo g = new Grupo();
                g.setId(rs.getInt(1));
                g.setNom(rs.getString(2));
                g.setArea(rs.getString(3));
                datos.add(g);
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error en el listado");
        }
        return datos;
    }
    
    public int agregar(Grupo g){
        String sql="INSERT INTO grupoaventureros (nombreGrupo, territorio) VALUES (?,?)";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, g.getNom());
            ps.setString(2, g.getArea());
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al agregar alemento");
        }
        return 1; 
    }
    
    public void eliminar(int id){
        String sql = "DELETE FROM grupoaventureros WHERE idGrupo="+id;
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al borrar un Grupo");
        }
    }
    
    public int actualizar(Grupo g){
        String sql = "UPDATE grupoaventureros SET nombreGrupo=?, territorio=? WHERE idGrupo=?";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, g.getNom());
            ps.setString(2, g.getArea());
            ps.setInt(3, g.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
        }catch (Exception e){
            System.out.println("Error al actualizar los datos");
        }
        return 1;
    }
    
}

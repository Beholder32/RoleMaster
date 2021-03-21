package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection con;
    
    public Connection getConnection(){
        String url = "jdbc:mysql://localhost:3306/rolemaster?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "admin";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            System.out.println("No se ha podido realizar la conexion");
        }
        return con;
    }
}

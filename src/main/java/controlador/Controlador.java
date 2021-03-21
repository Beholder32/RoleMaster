package controlador;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Conexion;
import modelo.Grupo;
import modelo.GrupoDAO;
import modelo.Personaje;
import modelo.PersonajeDAO;
import modelo.TablaImagen;
import vista.Vista;

public class Controlador implements ActionListener{
    
    //Importaciones para los ComboBox
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String filename = null;
    byte[] personaje_image = null;
    
    //Importacion del Objeto y los metodos Grupo
    GrupoDAO dao = new GrupoDAO();
    Grupo g = new Grupo();
    
    //Importacion del Objeto y los metodos Personaje
    PersonajeDAO dao2 = new PersonajeDAO();
    Personaje p = new Personaje();
    
    //Importacion de la Vista
    Vista vista = new Vista();
    
    //Variables para las tablas
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    
    //Activamos la vista y los botones
    public Controlador (Vista v){
        //Aquí se activa la vista
        this.vista = v;
        
        //Aqui se activan los elementos de la vista como los botones y tablas de los Grupos
        this.vista.btnGrupoGuardar.addActionListener(this);
        this.vista.btnGrupoListar.addActionListener(this);
        this.vista.btnGrupoEliminar.addActionListener(this);
        this.vista.btnGrupoEditar.addActionListener(this);
        this.vista.btnGrupoAceptar.addActionListener(this);
        
        //Aqui se activan los elementos de la vista como los botones y tablas de los Aventureros
        this.vista.btnPjCrear.addActionListener(this);
        this.vista.btnPjListar.addActionListener(this);
        this.vista.btnPjBorrar.addActionListener(this);
        this.vista.btnPjEditar.addActionListener(this);
        this.vista.btnPjAceptar.addActionListener(this); 
        this.vista.txtCbPjFuerza.addActionListener(this);
        this.vista.btnPjAbrir.addActionListener(this);
        
        //Aquí se activan los botones de las búsquedas
        this.vista.btnBuscar1.addActionListener(this);
        this.vista.btnBuscar2.addActionListener(this);
        this.vista.btnBuscar3.addActionListener(this);
        this.vista.buscarGrupo.addActionListener(this);
    }
    
    // Aquí asignamos las acciones para que los botones presionados ejecuten las acciones que queremos asignarles
    @Override
   
    public void actionPerformed(ActionEvent e) {
        //----- Acciones para Grupos --------
        if(e.getSource()==vista.btnGrupoListar){
            limpiarTabla();
            listar(vista.tablaGrupo);
        }
        if(e.getSource()==vista.btnGrupoGuardar){
            agregarGrupo();
            limpiarTabla();
            listar(vista.tablaGrupo);
        }
        if(e.getSource() == vista.btnGrupoEliminar){
            borrarGrupo();
            limpiarTabla();
            listar(vista.tablaGrupo);
        }
        if(e.getSource()==vista.btnGrupoEditar){
            int fila = vista.tablaGrupo.getSelectedRow();
            if(fila == -1){
                JOptionPane.showMessageDialog(vista, "No hay Grupo seleccionado");
            }else{
                int id=Integer.parseInt((String)vista.tablaGrupo.getValueAt(fila, 0).toString());
                String nom = (String)vista.tablaGrupo.getValueAt(fila, 1);
                String area = (String)vista.tablaGrupo.getValueAt(fila, 2);
                vista.txtIdGrupo.setText(""+id);
                vista.txtNombreGrupo.setText(nom);
                vista.txtCmbTerritorios.setSelectedItem(area);
            }
        }
        if(e.getSource() == vista.btnGrupoAceptar){
            actualizarGrupo();
            limpiarTabla();
            listar(vista.tablaGrupo);
        }
        
        //----- Acciones para Aventureros --------
        
        if(e.getSource()==vista.btnPjCrear){
            agregarPersonaje();
            limpiarTabla2();
            listarPersonajes(vista.tablaAventureros);
        }
        if(e.getSource()==vista.btnPjListar){
            limpiarTabla2();
            listarPersonajes(vista.tablaAventureros);
        }
        if(e.getSource()==vista.txtCbPjFuerza){
            String fuerza = vista.txtCbPjFuerza.getSelectedItem().toString();
            int fu = Integer.parseInt(fuerza);
            int inteligencia = 10 - fu;
            vista.txtPjInteligencia.setText(""+inteligencia);
        }
        if(e.getSource()==vista.btnPjAbrir){
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            filename = f.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(vista.huecoPjFoto.getWidth(),vista.huecoPjFoto.getHeight(), Image.SCALE_SMOOTH));
            vista.huecoPjFoto.setIcon(imageIcon);
            try{
                File image = new File(filename);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for(int readNum;(readNum=fis.read(buf))!=-1;){
                    bos.write(buf,0,readNum);
                }
                personaje_image = bos.toByteArray();
            }catch(Exception ex){
                System.out.println("Error al cargar la imagen");
            }
        }
        if(e.getSource() == vista.btnPjBorrar){
            borrarPersonaje();
            limpiarTabla2();
            listarPersonajes(vista.tablaAventureros);
        }
        if(e.getSource()==vista.btnPjEditar){
            int fila = vista.tablaAventureros.getSelectedRow();
            if(fila == -1){
                JOptionPane.showMessageDialog(vista, "No hay aventurero seleccionado");
            }else{
                int id=Integer.parseInt((String)vista.tablaAventureros.getValueAt(fila, 0).toString());
                String nom = (String)vista.tablaAventureros.getValueAt(fila, 1);
                String raza = (String)vista.tablaAventureros.getValueAt(fila, 2);
                String clase = (String)vista.tablaAventureros.getValueAt(fila, 3);
                String fuerza=(String)vista.tablaAventureros.getValueAt(fila, 4).toString();
                int inteligencia=Integer.parseInt((String)vista.tablaAventureros.getValueAt(fila, 5).toString());
                try{
                    String sql = "SELECT foto FROM aventurero WHERE idAventurero="+id+";";
                    con = conectar.getConnection();
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        Blob blob = rs.getBlob(1);
                        byte[] data = blob.getBytes(1, (int)blob.length());
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(data).getImage().getScaledInstance(vista.huecoPjFoto.getWidth(),vista.huecoPjFoto.getHeight(), Image.SCALE_SMOOTH));
                        vista.huecoPjFoto.setIcon(imageIcon);
                    }
                }catch(Exception ex){
                    System.out.println("Error al cargar la imagen");
                }
                vista.txtPjID.setText(""+id);
                vista.txtPjNombre.setText(nom);
                vista.txtCbPjRaza.setSelectedItem(raza);
                vista.txtCbPjClase.setSelectedItem(clase);
                vista.txtCbPjFuerza.setSelectedItem(fuerza);
                vista.txtPjInteligencia.setText(""+inteligencia);
                vista.txtEtiquetaDesc.setText(nom+" es un "+clase+" "+raza);
            }
        }
        if(e.getSource() == vista.btnPjAceptar){
            actualizarPersonaje();
            limpiarTabla2();
            listarPersonajes(vista.tablaAventureros);
        }
        //----- Acciones para las Busquedas --------
        if(e.getSource()==vista.btnBuscar1){
            limpiarTabla2();
            listarPersonajesNombre(vista.tablaAventureros);
        }
        if(e.getSource()==vista.btnBuscar2){
            limpiarTabla2();
            listarPersonajesClase(vista.tablaAventureros);
        }
        if(e.getSource()==vista.btnBuscar3){
            limpiarTabla2();
            listarPersonajesGrupo(vista.tablaAventureros);
        }
    }
    
    // --------- Métodos para los Grupos -----------
    
    void limpiarTabla(){
        for(int i = 0;i<vista.tablaGrupo.getRowCount();i++){
            modelo.removeRow(i);
            i=i-1;
        }
    }
    
    void limpiarTabla2(){
        for(int i = 0;i<vista.tablaAventureros.getRowCount();i++){
            modelo2.removeRow(i);
            i=i-1;
        }
    }
    
    public void listar(JTable tabla){
        modelo = (DefaultTableModel)tabla.getModel();
        List<Grupo>lista = dao.listar();
        Object[]object = new Object[3];
        for (int i = 0; i < lista.size(); i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNom();
            object[2] = lista.get(i).getArea();
            modelo.addRow(object);
        }
        vista.tablaGrupo.setModel(modelo);
    }
    
    public void agregarGrupo(){
        //recibimos la informacion de las cajas de texto
        String nom = vista.txtNombreGrupo.getText();
        String area = vista.txtCmbTerritorios.getSelectedItem().toString();
        g.setNom(nom);
        g.setArea(area);
        int respuesta = dao.agregar(g);
        if(respuesta==1){
            JOptionPane.showMessageDialog(vista, "Grupo agregado a los archivos");
        }else{
            JOptionPane.showMessageDialog(vista, "Ha habido un fallo al integrar al nuevo grupo");
        }
    }
    
    public void borrarGrupo(){
        int fila = vista.tablaGrupo.getSelectedRow();
            
        if(fila == -1){
            JOptionPane.showMessageDialog(vista, "No hay Grupo seleccionado");
        }else{
            int id=Integer.parseInt((String)vista.tablaGrupo.getValueAt(fila, 0).toString());
            dao.eliminar(id);
            JOptionPane.showMessageDialog(vista, "Grupo de aventureros enviado al Oblivion");
        }
    }
    
    public void actualizarGrupo(){
        int id=Integer.parseInt(vista.txtIdGrupo.getText());
        String nom = vista.txtNombreGrupo.getText();
        String area = vista.txtCmbTerritorios.getSelectedItem().toString();
        g.setId(id);
        g.setNom(nom);
        g.setArea(area);
        int respuesta = dao.actualizar(g);
        if(respuesta==1){
            JOptionPane.showMessageDialog(vista, "Grupo rearchivado con éxito");
        }else{
            JOptionPane.showMessageDialog(vista, "Error al transcribir tu Grupo");
        }
    }
    
    // --------- Métodos para los Aventureros -----------
      
    public void listarPersonajes(JTable tabla){
        tabla.setDefaultRenderer(Object.class, new TablaImagen());
        modelo2 = (DefaultTableModel)tabla.getModel();
        List<Personaje>lista = dao2.listar();
        Object[]object = new Object[9];
        for (int i = 0; i < lista.size(); i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNom();
            object[2] = lista.get(i).getRaza();
            object[3] = lista.get(i).getClase();
            object[4] = lista.get(i).getFuerza();
            object[5] = lista.get(i).getInteligencia();
            object[6] = lista.get(i).getArma();
            object[7] = lista.get(i).getGrupo();
            if(lista.get(i).getFoto() != null){
                try{
                    byte[] img = (lista.get(i).getFoto());
                    ImageIcon icono = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(64,100, Image.SCALE_SMOOTH));
                    object[8] = new JLabel(icono);
                }catch(Exception e){
                    System.out.println(e);
                }
            }else{
                object[8] = "No hay imagen";
            }
            modelo2.addRow(object);
        }
        tabla.setRowHeight(100);
        vista.tablaAventureros.setModel(modelo2);
    }
    
    public void agregarPersonaje(){
        //recibimos la informacion de las cajas de texto
        String nom = vista.txtPjNombre.getText();
        String raza = vista.txtCbPjRaza.getSelectedItem().toString();
        String clase = vista.txtCbPjClase.getSelectedItem().toString();
        String fuerza = vista.txtCbPjFuerza.getSelectedItem().toString();
        int fu = Integer.parseInt(fuerza);
        int inteligencia = 10 - fu;
        String arma = vista.txtCbPjArma.getSelectedItem().toString();
        String[] fragmento = arma.split(" ");
        int idArma = Integer.parseInt(fragmento[0]);
        String grupo = vista.txtCbPjGrupo.getSelectedItem().toString();
        String[] fragmento2 = grupo.split(" ");
        int idGrupo = Integer.parseInt(fragmento2[0]);
        
        p.setNom(nom);
        p.setRaza(raza);
        p.setClase(clase);
        p.setFuerza(fu);
        p.setInteligencia(inteligencia);
        p.setArma(idArma);
        p.setGrupo(idGrupo);
        p.setFoto(personaje_image);
        
        int respuesta = dao2.agregar(p);
        if(respuesta==1){
            JOptionPane.showMessageDialog(vista, "Aventurero registrado");
        }else{
            JOptionPane.showMessageDialog(vista, "Ha habido un fallo al crear al aventurero");
        }
    }
    
    public void borrarPersonaje(){
        int fila = vista.tablaAventureros.getSelectedRow();
            
        if(fila == -1){
            JOptionPane.showMessageDialog(vista, "No hay aventurero seleccionado");
        }else{
            int id=Integer.parseInt((String)vista.tablaAventureros.getValueAt(fila, 0).toString());
            dao2.eliminar(id);
            JOptionPane.showMessageDialog(vista, "Aventurero enviado al Oblivion");
        }
    }
    
    public void actualizarPersonaje(){
        int id=Integer.parseInt(vista.txtPjID.getText());
        String nom = vista.txtPjNombre.getText();
        String raza = vista.txtCbPjRaza.getSelectedItem().toString();
        String clase = vista.txtCbPjClase.getSelectedItem().toString();
        String fuerza = vista.txtCbPjFuerza.getSelectedItem().toString();
        int fu = Integer.parseInt(fuerza);
        int inteligencia = 10 - fu;
        String arma = vista.txtCbPjArma.getSelectedItem().toString();
        String[] fragmento = arma.split(" ");
        int idArma = Integer.parseInt(fragmento[0]);
        String grupo = vista.txtCbPjGrupo.getSelectedItem().toString();
        String[] fragmento2 = grupo.split(" ");
        int idGrupo = Integer.parseInt(fragmento2[0]);
        p.setId(id);
        p.setNom(nom);
        p.setRaza(raza);
        p.setClase(clase);
        p.setFuerza(fu);
        p.setInteligencia(inteligencia);
        p.setArma(idArma);
        p.setGrupo(idGrupo);
        int respuesta = dao2.actualizar(p);
        if(respuesta==1){
            JOptionPane.showMessageDialog(vista, "Aventurero actualizado");
        }else{
            JOptionPane.showMessageDialog(vista, "Ha habido un fallo al actualizar al aventurero");
        }
    }
    
    public void listarPersonajesNombre(JTable tabla){
        tabla.setDefaultRenderer(Object.class, new TablaImagen());
        modelo2 = (DefaultTableModel)tabla.getModel();
        String nom = (String)vista.buscarNombre.getText();
        List<Personaje>lista = dao2.listarPorNombre(nom);
        Object[]object = new Object[9];
        for (int i = 0; i < lista.size(); i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNom();
            object[2] = lista.get(i).getRaza();
            object[3] = lista.get(i).getClase();
            object[4] = lista.get(i).getFuerza();
            object[5] = lista.get(i).getInteligencia();
            object[6] = lista.get(i).getArma();
            object[7] = lista.get(i).getGrupo();
            if(lista.get(i).getFoto() != null){
                try{
                    byte[] img = (lista.get(i).getFoto());
                    ImageIcon icono = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(64,100, Image.SCALE_SMOOTH));
                    object[8] = new JLabel(icono);
                }catch(Exception e){
                    System.out.println(e);
                }
            }else{
                object[8] = "No hay imagen";
            }
            modelo2.addRow(object);
        }
        tabla.setRowHeight(100);
        vista.tablaAventureros.setModel(modelo2);
    }
    
    public void listarPersonajesClase(JTable tabla){
        tabla.setDefaultRenderer(Object.class, new TablaImagen());
        modelo2 = (DefaultTableModel)tabla.getModel();
        String nom = (String)vista.buscarClase.getSelectedItem().toString();
        List<Personaje>lista = dao2.listarPorClase(nom);
        Object[]object = new Object[9];
        for (int i = 0; i < lista.size(); i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNom();
            object[2] = lista.get(i).getRaza();
            object[3] = lista.get(i).getClase();
            object[4] = lista.get(i).getFuerza();
            object[5] = lista.get(i).getInteligencia();
            object[6] = lista.get(i).getArma();
            object[7] = lista.get(i).getGrupo();
            if(lista.get(i).getFoto() != null){
                try{
                    byte[] img = (lista.get(i).getFoto());
                    ImageIcon icono = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(64,100, Image.SCALE_SMOOTH));
                    object[8] = new JLabel(icono);
                }catch(Exception e){
                    System.out.println(e);
                }
            }else{
                object[8] = "No hay imagen";
            }
            modelo2.addRow(object);
        }
        tabla.setRowHeight(100);
        vista.tablaAventureros.setModel(modelo2);
    }
    
    public void listarPersonajesGrupo(JTable tabla){
        tabla.setDefaultRenderer(Object.class, new TablaImagen());
        modelo2 = (DefaultTableModel)tabla.getModel();
        String nom = (String)vista.buscarGrupo.getSelectedItem().toString();
        List<Personaje>lista = dao2.listarPorGrupo(nom);
        Object[]object = new Object[9];
        for (int i = 0; i < lista.size(); i++){
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNom();
            object[2] = lista.get(i).getRaza();
            object[3] = lista.get(i).getClase();
            object[4] = lista.get(i).getFuerza();
            object[5] = lista.get(i).getInteligencia();
            object[6] = lista.get(i).getArma();
            object[7] = lista.get(i).getGrupo();
            if(lista.get(i).getFoto() != null){
                try{
                    byte[] img = (lista.get(i).getFoto());
                    ImageIcon icono = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(64,100, Image.SCALE_SMOOTH));
                    object[8] = new JLabel(icono);
                }catch(Exception e){
                    System.out.println(e);
                }
            }else{
                object[8] = "No hay imagen";
            }
            modelo2.addRow(object);
        }
        tabla.setRowHeight(100);
        vista.tablaAventureros.setModel(modelo2);
    }
    
    // -------- Método carga ComboBox ----------
    public void consultar_armas(JComboBox cbox_armas){
        
        String sql = "SELECT idArma, nombreArma FROM armas";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbox_armas.addItem(rs.getInt("idArma")+" -"+rs.getString("nombreArma"));
            }
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al mostrar el listado");
        }
    }
    public void consultar_grupo(JComboBox cbox_grupo){
        
        String sql = "SELECT idGrupo, nombreGrupo FROM grupoaventureros";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbox_grupo.addItem(rs.getInt("idGrupo")+" -"+rs.getString("nombreGrupo"));
            }
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al mostrar el listado");
        }
    }
    
    public void buscar_grupo(JComboBox cbox_grupo){
        
        String sql = "SELECT nombreGrupo FROM grupoaventureros";
        try{
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbox_grupo.addItem(rs.getString("nombreGrupo"));
            }
            ps.close();
            con.close();
        }catch(Exception e){
            System.out.println("Error al mostrar el listado");
        }
    }
}

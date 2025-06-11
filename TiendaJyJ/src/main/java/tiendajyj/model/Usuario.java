package tiendajyj.model;

import java.util.Date;

/**
 * Modelo de Usuario con campo adicional para nombre del nivel de acceso
 * @author MINEDUCYT
 */
public class Usuario {
    private int id_usuario;
    private int id_nivel_usuario;
    private String username;
    private String nombres_usuario;
    private String apellidos_usuario;
    private String correo_usuario;
    private String telefono_usuario;
    private String password;
    private Date fecha_creacion_usuario;
    private Date fecha_actualizacion_usuario;
    
    // Campo adicional para mostrar el nombre del nivel de acceso
    private String nombre_nivel_acceso;
    
    // Constructors
    public Usuario() {
    }
    
    public Usuario(int id_usuario, int id_nivel_usuario, String username, String nombres_usuario, 
                   String apellidos_usuario, String correo_usuario, String telefono_usuario, 
                   String password, Date fecha_creacion_usuario, Date fecha_actualizacion_usuario) {
        this.id_usuario = id_usuario;
        this.id_nivel_usuario = id_nivel_usuario;
        this.username = username;
        this.nombres_usuario = nombres_usuario;
        this.apellidos_usuario = apellidos_usuario;
        this.correo_usuario = correo_usuario;
        this.telefono_usuario = telefono_usuario;
        this.password = password;
        this.fecha_creacion_usuario = fecha_creacion_usuario;
        this.fecha_actualizacion_usuario = fecha_actualizacion_usuario;
    }
    
    // Getters and Setters
    public int getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public int getId_nivel_usuario() {
        return id_nivel_usuario;
    }
    
    public void setId_nivel_usuario(int id_nivel_usuario) {
        this.id_nivel_usuario = id_nivel_usuario;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getNombres_usuario() {
        return nombres_usuario;
    }
    
    public void setNombres_usuario(String nombres_usuario) {
        this.nombres_usuario = nombres_usuario;
    }
    
    public String getApellidos_usuario() {
        return apellidos_usuario;
    }
    
    public void setApellidos_usuario(String apellidos_usuario) {
        this.apellidos_usuario = apellidos_usuario;
    }
    
    public String getCorreo_usuario() {
        return correo_usuario;
    }
    
    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }
    
    public String getTelefono_usuario() {
        return telefono_usuario;
    }
    
    public void setTelefono_usuario(String telefono_usuario) {
        this.telefono_usuario = telefono_usuario;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Date getFecha_creacion_usuario() {
        return fecha_creacion_usuario;
    }
    
    public void setFecha_creacion_usuario(Date fecha_creacion_usuario) {
        this.fecha_creacion_usuario = fecha_creacion_usuario;
    }
    
    public Date getFecha_actualizacion_usuario() {
        return fecha_actualizacion_usuario;
    }
    
    public void setFecha_actualizacion_usuario(Date fecha_actualizacion_usuario) {
        this.fecha_actualizacion_usuario = fecha_actualizacion_usuario;
    }

    public String getNombre_nivel_acceso() {
        return nombre_nivel_acceso;
    }

    public void setNombre_nivel_acceso(String nombre_nivel_acceso) {
        this.nombre_nivel_acceso = nombre_nivel_acceso;
    }
    
    // Método toString para debugging
    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", id_nivel_usuario=" + id_nivel_usuario +
                ", username='" + username + '\'' +
                ", nombres_usuario='" + nombres_usuario + '\'' +
                ", apellidos_usuario='" + apellidos_usuario + '\'' +
                ", correo_usuario='" + correo_usuario + '\'' +
                ", telefono_usuario='" + telefono_usuario + '\'' +
                ", nombre_nivel_acceso='" + nombre_nivel_acceso + '\'' +
                '}';
    }
}
package tiendajyj.model;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private int idNivelUsuario;
    private String username;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String correoUsuario;
    private String telefonoUsuario;
    private String password;
    private Timestamp fechaCreacionUsuario;
    private Timestamp fechaActualizacionUsuario;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros principales
    public Usuario(int idNivelUsuario, String username, String nombresUsuario, 
                   String apellidosUsuario, String correoUsuario, String telefonoUsuario, String password) {
        this.idNivelUsuario = idNivelUsuario;
        this.username = username;
        this.nombresUsuario = nombresUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.correoUsuario = correoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.password = password;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdNivelUsuario() {
        return idNivelUsuario;
    }

    public void setIdNivelUsuario(int idNivelUsuario) {
        this.idNivelUsuario = idNivelUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosUsuario(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getFechaCreacionUsuario() {
        return fechaCreacionUsuario;
    }

    public void setFechaCreacionUsuario(Timestamp fechaCreacionUsuario) {
        this.fechaCreacionUsuario = fechaCreacionUsuario;
    }

    public Timestamp getFechaActualizacionUsuario() {
        return fechaActualizacionUsuario;
    }

    public void setFechaActualizacionUsuario(Timestamp fechaActualizacionUsuario) {
        this.fechaActualizacionUsuario = fechaActualizacionUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", idNivelUsuario=" + idNivelUsuario +
                ", username='" + username + '\'' +
                ", nombresUsuario='" + nombresUsuario + '\'' +
                ", apellidosUsuario='" + apellidosUsuario + '\'' +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", telefonoUsuario='" + telefonoUsuario + '\'' +
                '}';
    }
}
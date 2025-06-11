/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.model;

import java.util.Date;

/**
 *
 * @author MINEDUCYT
 */

public class Cliente {
    private int id_cliente;
    private int id_usuario;
    private String dui;
    private String nombres_cliente;
    private String apellidos_cliente;
    private String telefono_cliente;
    private String correo_cliente;
    private Date fecha_inserccion_cliente;
    private Date fecha_actualizacion_cliente;

    // Constructors
    public Cliente() {
    }

    public Cliente(int id_cliente, int id_usuario, String dui, String nombres_cliente, 
                   String apellidos_cliente, String telefono_cliente, String correo_cliente,
                   Date fecha_inserccion_cliente, Date fecha_actualizacion_cliente) {
        this.id_cliente = id_cliente;
        this.id_usuario = id_usuario;
        this.dui = dui;
        this.nombres_cliente = nombres_cliente;
        this.apellidos_cliente = apellidos_cliente;
        this.telefono_cliente = telefono_cliente;
        this.correo_cliente = correo_cliente;
        this.fecha_inserccion_cliente = fecha_inserccion_cliente;
        this.fecha_actualizacion_cliente = fecha_actualizacion_cliente;
    }

    // Getters and Setters
    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNombres_cliente() {
        return nombres_cliente;
    }

    public void setNombres_cliente(String nombres_cliente) {
        this.nombres_cliente = nombres_cliente;
    }

    public String getApellidos_cliente() {
        return apellidos_cliente;
    }

    public void setApellidos_cliente(String apellidos_cliente) {
        this.apellidos_cliente = apellidos_cliente;
    }

    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getCorreo_cliente() {
        return correo_cliente;
    }

    public void setCorreo_cliente(String correo_cliente) {
        this.correo_cliente = correo_cliente;
    }

    public Date getFecha_inserccion_cliente() {
        return fecha_inserccion_cliente;
    }

    public void setFecha_inserccion_cliente(Date fecha_inserccion_cliente) {
        this.fecha_inserccion_cliente = fecha_inserccion_cliente;
    }

    public Date getFecha_actualizacion_cliente() {
        return fecha_actualizacion_cliente;
    }

    public void setFecha_actualizacion_cliente(Date fecha_actualizacion_cliente) {
        this.fecha_actualizacion_cliente = fecha_actualizacion_cliente;
    }

}

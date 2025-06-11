/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.model;

import java.util.Date;


    public class Empleado {
        private int id;
        private String nombreEmpleado;
        private String apellidoEmpleado;
        private String dui;
        private String telefono;
        private String direccion;
        private String correo;
        private Date fechaIngreso;
        private String cargo;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getApellidoEmpleado() { return apellidoEmpleado; }
    public void setApellidoEmpleado(String apellidoEmpleado) { this.apellidoEmpleado = apellidoEmpleado; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}
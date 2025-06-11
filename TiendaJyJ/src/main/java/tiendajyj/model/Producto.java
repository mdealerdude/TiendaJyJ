/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author MINEDUCYT
 */
public class Producto {
    private int id_producto;
    private int id_usuario;
    private String nombre_producto;
    private int id_marca;
    private int stock_producto;
    private BigDecimal precio_producto;
    private Date fecha_inserccion_producto;
    private Date fecha_actualizacion_producto;
    
    // Campo adicional para mostrar nombre de marca
    private String nombre_marca;

    // Constructores
    public Producto() {
    }

    public Producto(int id_producto, int id_usuario, String nombre_producto, int id_marca,
                   int stock_producto, BigDecimal precio_producto, Date fecha_inserccion_producto,
                   Date fecha_actualizacion_producto) {
        this.id_producto = id_producto;
        this.id_usuario = id_usuario;
        this.nombre_producto = nombre_producto;
        this.id_marca = id_marca;
        this.stock_producto = stock_producto;
        this.precio_producto = precio_producto;
        this.fecha_inserccion_producto = fecha_inserccion_producto;
        this.fecha_actualizacion_producto = fecha_actualizacion_producto;
    }

    // Getters and Setters
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public int getStock_producto() {
        return stock_producto;
    }

    public void setStock_producto(int stock_producto) {
        this.stock_producto = stock_producto;
    }

    public BigDecimal getPrecio_producto() {
        return precio_producto;
    }

    public void setPrecio_producto(BigDecimal precio_producto) {
        this.precio_producto = precio_producto;
    }

    public Date getFecha_inserccion_producto() {
        return fecha_inserccion_producto;
    }

    public void setFecha_inserccion_producto(Date fecha_inserccion_producto) {
        this.fecha_inserccion_producto = fecha_inserccion_producto;
    }

    public Date getFecha_actualizacion_producto() {
        return fecha_actualizacion_producto;
    }

    public void setFecha_actualizacion_producto(Date fecha_actualizacion_producto) {
        this.fecha_actualizacion_producto = fecha_actualizacion_producto;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }
}
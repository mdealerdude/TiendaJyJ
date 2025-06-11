package com.tiendajyj.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clase modelo para representar un Producto
 */
public class Producto {
    private int idProducto;
    private int idUsuario;
    private String nombreProducto;
    private int idMarca;
    private String nombreMarca; // Para mostrar el nombre de la marca
    private int stockProducto;
    private BigDecimal precioProducto;
    private Timestamp fechaInserccionProducto;
    private Timestamp fechaActualizacionProducto;
    
    // Constructor vacío
    public Producto() {}
    
    // Constructor completo
    public Producto(int idProducto, int idUsuario, String nombreProducto, 
                   int idMarca, String nombreMarca, int stockProducto, 
                   BigDecimal precioProducto, Timestamp fechaInserccionProducto, 
                   Timestamp fechaActualizacionProducto) {
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.nombreProducto = nombreProducto;
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.stockProducto = stockProducto;
        this.precioProducto = precioProducto;
        this.fechaInserccionProducto = fechaInserccionProducto;
        this.fechaActualizacionProducto = fechaActualizacionProducto;
    }
    
    // Constructor para inserción (sin fechas y sin ID)
    public Producto(int idUsuario, String nombreProducto, int idMarca, 
                   int stockProducto, BigDecimal precioProducto) {
        this.idUsuario = idUsuario;
        this.nombreProducto = nombreProducto;
        this.idMarca = idMarca;
        this.stockProducto = stockProducto;
        this.precioProducto = precioProducto;
    }
    
    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public int getIdMarca() {
        return idMarca;
    }
    
    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
    
    public String getNombreMarca() {
        return nombreMarca;
    }
    
    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }
    
    public int getStockProducto() {
        return stockProducto;
    }
    
    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }
    
    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }
    
    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }
    
    public Timestamp getFechaInserccionProducto() {
        return fechaInserccionProducto;
    }
    
    public void setFechaInserccionProducto(Timestamp fechaInserccionProducto) {
        this.fechaInserccionProducto = fechaInserccionProducto;
    }
    
    public Timestamp getFechaActualizacionProducto() {
        return fechaActualizacionProducto;
    }
    
    public void setFechaActualizacionProducto(Timestamp fechaActualizacionProducto) {
        this.fechaActualizacionProducto = fechaActualizacionProducto;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", idUsuario=" + idUsuario +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", idMarca=" + idMarca +
                ", nombreMarca='" + nombreMarca + '\'' +
                ", stockProducto=" + stockProducto +
                ", precioProducto=" + precioProducto +
                ", fechaInserccionProducto=" + fechaInserccionProducto +
                ", fechaActualizacionProducto=" + fechaActualizacionProducto +
                '}';
    }
}
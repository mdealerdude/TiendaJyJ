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
    private int idProducto;
    private int idUsuario;
    private String nombreProducto;
    private int idMarca;
    private int stockProducto;
    private BigDecimal precioProducto;
    private Date fechaInserccionProducto;
    private Date fechaActualizacionProducto;
    
    // Campo adicional para mostrar nombre de marca
    private String nombreMarca;
    
    // Constructores
    public Producto() {
    
    }
    
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
    
    public Date getFechaInserccionProducto() {
        return fechaInserccionProducto;
    }
    
    public void setFechaInserccionProducto(Date fechaInserccionProducto) {
        this.fechaInserccionProducto = fechaInserccionProducto;
    }
    
    public Date getFechaActualizacionProducto() {
        return fechaActualizacionProducto;
    }
    
    public void setFechaActualizacionProducto(Date fechaActualizacionProducto) {
        this.fechaActualizacionProducto = fechaActualizacionProducto;
    }
    
    public String getNombreMarca() {
        return nombreMarca;
    }
    
    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }
}
    
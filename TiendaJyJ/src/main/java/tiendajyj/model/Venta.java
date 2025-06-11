package tiendajyj.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author MINEDUCYT
 */
public class Venta {
    private int id_venta;
    private int id_usuario;
    private int id_cliente;
    private int id_producto;
    private int cantidad_producto;
    private BigDecimal total;
    private Date fecha_inserccion_venta;
    private Date fecha_actualizacion_venta;
    
    // Campos adicionales para mostrar información relacionada
    private String nombre_cliente;
    private String nombre_producto;
    private String nombre_usuario;
    private BigDecimal precio_unitario;

    // Constructores
    public Venta() {
    }

    public Venta(int id_venta, int id_usuario, int id_cliente, int id_producto, 
                 int cantidad_producto, BigDecimal total, Date fecha_inserccion_venta, 
                 Date fecha_actualizacion_venta) {
        this.id_venta = id_venta;
        this.id_usuario = id_usuario;
        this.id_cliente = id_cliente;
        this.id_producto = id_producto;
        this.cantidad_producto = cantidad_producto;
        this.total = total;
        this.fecha_inserccion_venta = fecha_inserccion_venta;
        this.fecha_actualizacion_venta = fecha_actualizacion_venta;
    }

    // Getters and Setters
    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad_producto() {
        return cantidad_producto;
    }

    public void setCantidad_producto(int cantidad_producto) {
        this.cantidad_producto = cantidad_producto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getFecha_inserccion_venta() {
        return fecha_inserccion_venta;
    }

    public void setFecha_inserccion_venta(Date fecha_inserccion_venta) {
        this.fecha_inserccion_venta = fecha_inserccion_venta;
    }

    public Date getFecha_actualizacion_venta() {
        return fecha_actualizacion_venta;
    }

    public void setFecha_actualizacion_venta(Date fecha_actualizacion_venta) {
        this.fecha_actualizacion_venta = fecha_actualizacion_venta;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
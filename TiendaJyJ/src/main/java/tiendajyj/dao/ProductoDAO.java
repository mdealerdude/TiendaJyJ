package com.tiendajyj.dao;

import com.tiendajyj.modelo.Producto;
import com.tiendajyj.conexion.Conexion; // Asumiendo que tienes esta clase
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Clase DAO para manejar operaciones CRUD de productos
 */
public class ProductoDAO {
    
    private Connection connection;
    
    public ProductoDAO() {
        // Inicializar conexión usando tu clase de conexión existente
        this.connection = Conexion.getConnection();
    }
    
    /**
     * Obtener todos los productos con información de marca
     */
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.id_usuario, p.nombre_producto, " +
                    "p.id_marca, m.nombre_marca, p.stock_producto, p.precio_producto, " +
                    "p.fecha_inserccion_producto, p.fecha_actualizacion_producto " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "ORDER BY p.id_producto DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setIdUsuario(rs.getInt("id_usuario"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setIdMarca(rs.getInt("id_marca"));
                producto.setNombreMarca(rs.getString("nombre_marca"));
                producto.setStockProducto(rs.getInt("stock_producto"));
                producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Obtener producto por ID
     */
    public Producto obtenerProductoPorId(int idProducto) {
        String sql = "SELECT p.id_producto, p.id_usuario, p.nombre_producto, " +
                    "p.id_marca, m.nombre_marca, p.stock_producto, p.precio_producto, " +
                    "p.fecha_inserccion_producto, p.fecha_actualizacion_producto " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.id_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setIdUsuario(rs.getInt("id_usuario"));
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                    producto.setNombreMarca(rs.getString("nombre_marca"));
                    producto.setStockProducto(rs.getInt("stock_producto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                    producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                    producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                    
                    return producto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Insertar nuevo producto
     */
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (id_usuario, nombre_producto, id_marca, " +
                    "stock_producto, precio_producto, fecha_actualizacion_producto) " +
                    "VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, producto.getIdUsuario());
            stmt.setString(2, producto.getNombreProducto());
            stmt.setInt(3, producto.getIdMarca());
            stmt.setInt(4, producto.getStockProducto());
            stmt.setBigDecimal(5, producto.getPrecioProducto());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualizar producto existente
     */
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre_producto = ?, id_marca = ?, " +
                    "stock_producto = ?, precio_producto = ?, fecha_actualizacion_producto = NOW() " +
                    "WHERE id_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombreProducto());
            stmt.setInt(2, producto.getIdMarca());
            stmt.setInt(3, producto.getStockProducto());
            stmt.setBigDecimal(4, producto.getPrecioProducto());
            stmt.setInt(5, producto.getIdProducto());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Eliminar producto
     */
    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtener todas las marcas para el dropdown
     */
    public List<String[]> obtenerMarcas() {
        List<String[]> marcas = new ArrayList<>();
        String sql = "SELECT id_marca, nombre_marca FROM marca ORDER BY nombre_marca";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String[] marca = new String[2];
                marca[0] = String.valueOf(rs.getInt("id_marca"));
                marca[1] = rs.getString("nombre_marca");
                marcas.add(marca);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener marcas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return marcas;
    }
    
    /**
     * Buscar productos por nombre
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.id_usuario, p.nombre_producto, " +
                    "p.id_marca, m.nombre_marca, p.stock_producto, p.precio_producto, " +
                    "p.fecha_inserccion_producto, p.fecha_actualizacion_producto " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.nombre_producto LIKE ? " +
                    "ORDER BY p.id_producto DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setIdUsuario(rs.getInt("id_usuario"));
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                    producto.setNombreMarca(rs.getString("nombre_marca"));
                    producto.setStockProducto(rs.getInt("stock_producto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                    producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                    producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                    
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Cerrar conexión
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
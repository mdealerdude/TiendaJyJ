/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.dao;

import tiendajyj.model.Venta;
import tiendajyj.model.Cliente;
import tiendajyj.model.Producto;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class VentaDao {

    private Connection conn;

    public VentaDao() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.*, " +
                    "CONCAT(c.nombres_cliente, ' ', c.apellidos_cliente) as nombre_cliente, " +
                    "p.nombre_producto, p.precio_producto, " +
                    "CONCAT(u.nombres_usuario, ' ', u.apellidos_usuario) as nombre_usuario " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id_cliente " +
                    "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                    "INNER JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                    "ORDER BY v.fecha_inserccion_venta DESC";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId_venta(rs.getInt("id_venta"));
                venta.setId_usuario(rs.getInt("id_usuario"));
                venta.setId_cliente(rs.getInt("id_cliente"));
                venta.setId_producto(rs.getInt("id_producto"));
                venta.setCantidad_producto(rs.getInt("cantidad_producto"));
                venta.setTotal(rs.getBigDecimal("total"));
                venta.setFecha_inserccion_venta(rs.getTimestamp("fecha_inserccion_venta"));
                venta.setFecha_actualizacion_venta(rs.getTimestamp("fecha_actualizacion_venta"));
                
                // Campos adicionales
                venta.setNombre_cliente(rs.getString("nombre_cliente"));
                venta.setNombre_producto(rs.getString("nombre_producto"));
                venta.setNombre_usuario(rs.getString("nombre_usuario"));
                venta.setPrecio_unitario(rs.getBigDecimal("precio_producto"));
                
                lista.add(venta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Venta obtenerVentaPorId(int id) {
        Venta venta = null;
        String sql = "SELECT v.*, " +
                    "CONCAT(c.nombres_cliente, ' ', c.apellidos_cliente) as nombre_cliente, " +
                    "p.nombre_producto, p.precio_producto, " +
                    "CONCAT(u.nombres_usuario, ' ', u.apellidos_usuario) as nombre_usuario " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id_cliente " +
                    "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                    "INNER JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                    "WHERE v.id_venta = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setId_venta(rs.getInt("id_venta"));
                    venta.setId_usuario(rs.getInt("id_usuario"));
                    venta.setId_cliente(rs.getInt("id_cliente"));
                    venta.setId_producto(rs.getInt("id_producto"));
                    venta.setCantidad_producto(rs.getInt("cantidad_producto"));
                    venta.setTotal(rs.getBigDecimal("total"));
                    venta.setFecha_inserccion_venta(rs.getTimestamp("fecha_inserccion_venta"));
                    venta.setFecha_actualizacion_venta(rs.getTimestamp("fecha_actualizacion_venta"));
                    
                    // Campos adicionales
                    venta.setNombre_cliente(rs.getString("nombre_cliente"));
                    venta.setNombre_producto(rs.getString("nombre_producto"));
                    venta.setNombre_usuario(rs.getString("nombre_usuario"));
                    venta.setPrecio_unitario(rs.getBigDecimal("precio_producto"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venta;
    }

    public boolean insertarVenta(Venta venta) {
        String sql = "INSERT INTO ventas (id_usuario, id_cliente, id_producto, cantidad_producto, " +
                    "total, fecha_inserccion_venta, fecha_actualizacion_venta) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, venta.getId_usuario());
            st.setInt(2, venta.getId_cliente());
            st.setInt(3, venta.getId_producto());
            st.setInt(4, venta.getCantidad_producto());
            st.setBigDecimal(5, venta.getTotal());
            st.setTimestamp(6, new Timestamp(venta.getFecha_inserccion_venta().getTime()));
            st.setTimestamp(7, new Timestamp(venta.getFecha_actualizacion_venta().getTime()));

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarVenta(Venta venta) {
        String sql = "UPDATE ventas SET id_usuario = ?, id_cliente = ?, id_producto = ?, " +
                    "cantidad_producto = ?, total = ?, fecha_actualizacion_venta = ? " +
                    "WHERE id_venta = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, venta.getId_usuario());
            st.setInt(2, venta.getId_cliente());
            st.setInt(3, venta.getId_producto());
            st.setInt(4, venta.getCantidad_producto());
            st.setBigDecimal(5, venta.getTotal());
            st.setTimestamp(6, new Timestamp(venta.getFecha_actualizacion_venta().getTime()));
            st.setInt(7, venta.getId_venta());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarVenta(int id) {
        String sql = "DELETE FROM ventas WHERE id_venta = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, CONCAT(nombres_cliente, ' ', apellidos_cliente) as nombre_completo " +
                    "FROM clientes ORDER BY nombres_cliente";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setNombres_cliente(rs.getString("nombre_completo"));
                lista.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre_producto, p.precio_producto, p.stock_producto, " +
                    "m.nombre_marca " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.stock_producto > 0 " +
                    "ORDER BY p.nombre_producto";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                producto.setStockProducto(rs.getInt("stock_producto"));
                // Usamos el campo nombre_marca como información adicional
                producto.setNombreMarca(rs.getString("nombre_marca"));
                lista.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public BigDecimal obtenerPrecioProducto(int idProducto) {
        String sql = "SELECT precio_producto FROM productos WHERE id_producto = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idProducto);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("precio_producto");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public boolean actualizarStockProducto(int idProducto, int cantidadVendida) {
        String sql = "UPDATE productos SET stock_producto = stock_producto - ? WHERE id_producto = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, cantidadVendida);
            st.setInt(2, idProducto);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
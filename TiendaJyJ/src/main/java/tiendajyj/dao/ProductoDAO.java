package tiendajyj.dao;

import tiendajyj.model.Producto;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductoDao {

    private Connection conn;

    public ProductoDao() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, m.nombre_marca, " +
                    "CONCAT(u.nombres_usuario, ' ', u.apellidos_usuario) as nombre_usuario " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "ORDER BY p.fecha_inserccion_producto DESC";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setIdUsuario(rs.getInt("id_usuario"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setIdMarca(rs.getInt("id_marca"));
                producto.setStockProducto(rs.getInt("stock_producto"));
                producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                
                // Campos adicionales
                producto.setNombreMarca(rs.getString("nombre_marca"));
                
                lista.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Producto obtenerProductoPorId(int id) {
        Producto producto = null;
        String sql = "SELECT p.*, m.nombre_marca, " +
                    "CONCAT(u.nombres_usuario, ' ', u.apellidos_usuario) as nombre_usuario " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "WHERE p.id_producto = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setIdUsuario(rs.getInt("id_usuario"));
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                    producto.setStockProducto(rs.getInt("stock_producto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                    producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                    producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                    
                    // Campos adicionales
                    producto.setNombreMarca(rs.getString("nombre_marca"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (id_usuario, nombre_producto, id_marca, " +
                    "stock_producto, precio_producto, fecha_inserccion_producto, " +
                    "fecha_actualizacion_producto) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, producto.getIdUsuario());
            st.setString(2, producto.getNombreProducto());
            st.setInt(3, producto.getIdMarca());
            st.setInt(4, producto.getStockProducto());
            st.setBigDecimal(5, producto.getPrecioProducto());
            st.setTimestamp(6, new Timestamp(producto.getFechaInserccionProducto().getTime()));
            st.setTimestamp(7, new Timestamp(producto.getFechaActualizacionProducto().getTime()));

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET id_usuario = ?, nombre_producto = ?, " +
                    "id_marca = ?, stock_producto = ?, precio_producto = ?, " +
                    "fecha_actualizacion_producto = ? WHERE id_producto = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, producto.getIdUsuario());
            st.setString(2, producto.getNombreProducto());
            st.setInt(3, producto.getIdMarca());
            st.setInt(4, producto.getStockProducto());
            st.setBigDecimal(5, producto.getPrecioProducto());
            st.setTimestamp(6, new Timestamp(producto.getFechaActualizacionProducto().getTime()));
            st.setInt(7, producto.getIdProducto());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Marca> listarMarcas() {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT id_marca, nombre_marca FROM marca ORDER BY nombre_marca";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("id_marca"));
                marca.setNombreMarca(rs.getString("nombre_marca"));
                lista.add(marca);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean verificarStockDisponible(int idProducto, int cantidadRequerida) {
        String sql = "SELECT stock_producto FROM productos WHERE id_producto = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idProducto);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int stockActual = rs.getInt("stock_producto");
                    return stockActual >= cantidadRequerida;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Producto> buscarProductos(String termino) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, m.nombre_marca " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.nombre_producto LIKE ? OR m.nombre_marca LIKE ? " +
                    "ORDER BY p.nombre_producto";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String busqueda = "%" + termino + "%";
            st.setString(1, busqueda);
            st.setString(2, busqueda);
            
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setIdUsuario(rs.getInt("id_usuario"));
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                    producto.setStockProducto(rs.getInt("stock_producto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                    producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                    producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                    
                    // Campos adicionales
                    producto.setNombreMarca(rs.getString("nombre_marca"));
                    
                    lista.add(producto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Producto> listarProductosPorMarca(int idMarca) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, m.nombre_marca " +
                    "FROM productos p " +
                    "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.id_marca = ? " +
                    "ORDER BY p.nombre_producto";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idMarca);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setIdUsuario(rs.getInt("id_usuario"));
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                    producto.setStockProducto(rs.getInt("stock_producto"));
                    producto.setPrecioProducto(rs.getBigDecimal("precio_producto"));
                    producto.setFechaInserccionProducto(rs.getTimestamp("fecha_inserccion_producto"));
                    producto.setFechaActualizacionProducto(rs.getTimestamp("fecha_actualizacion_producto"));
                    
                    // Campos adicionales
                    producto.setNombreMarca(rs.getString("nombre_marca"));
                    
                    lista.add(producto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Clase interna para Marca
    public static class Marca {
        private int idMarca;
        private String nombreMarca;

        public Marca() {}

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
    }
}
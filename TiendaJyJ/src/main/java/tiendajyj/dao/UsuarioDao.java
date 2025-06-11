package tiendajyj.dao;

import tiendajyj.model.Usuario;
import tiendajyj.model.NivelAcceso;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    private Connection conn;

    public UsuarioDao() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, n.nombre_nivel_acceso " +
                    "FROM usuarios u " +
                    "INNER JOIN nivel_acceso n ON u.id_nivel_usuario = n.id_nivel_acceso " +
                    "ORDER BY u.fecha_creacion_usuario DESC";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Usuario usr = new Usuario();
                usr.setId_usuario(rs.getInt("id_usuario"));
                usr.setId_nivel_usuario(rs.getInt("id_nivel_usuario"));
                usr.setUsername(rs.getString("username"));
                usr.setNombres_usuario(rs.getString("nombres_usuario"));
                usr.setApellidos_usuario(rs.getString("apellidos_usuario"));
                usr.setCorreo_usuario(rs.getString("correo_usuario"));
                usr.setTelefono_usuario(rs.getString("telefono_usuario"));
                usr.setPassword(rs.getString("password"));
                usr.setFecha_creacion_usuario(rs.getTimestamp("fecha_creacion_usuario"));
                usr.setFecha_actualizacion_usuario(rs.getTimestamp("fecha_actualizacion_usuario"));
                usr.setNombre_nivel_acceso(rs.getString("nombre_nivel_acceso"));
                lista.add(usr);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        Usuario usr = null;
        String sql = "SELECT u.*, n.nombre_nivel_acceso " +
                    "FROM usuarios u " +
                    "INNER JOIN nivel_acceso n ON u.id_nivel_usuario = n.id_nivel_acceso " +
                    "WHERE u.id_usuario = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setId_usuario(rs.getInt("id_usuario"));
                    usr.setId_nivel_usuario(rs.getInt("id_nivel_usuario"));
                    usr.setUsername(rs.getString("username"));
                    usr.setNombres_usuario(rs.getString("nombres_usuario"));
                    usr.setApellidos_usuario(rs.getString("apellidos_usuario"));
                    usr.setCorreo_usuario(rs.getString("correo_usuario"));
                    usr.setTelefono_usuario(rs.getString("telefono_usuario"));
                    usr.setPassword(rs.getString("password"));
                    usr.setFecha_creacion_usuario(rs.getTimestamp("fecha_creacion_usuario"));
                    usr.setFecha_actualizacion_usuario(rs.getTimestamp("fecha_actualizacion_usuario"));
                    usr.setNombre_nivel_acceso(rs.getString("nombre_nivel_acceso"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return usr;
    }

    public Usuario obtenerUsuarioPorUsername(String username) {
        Usuario usr = null;
        String sql = "SELECT u.*, n.nombre_nivel_acceso " +
                    "FROM usuarios u " +
                    "INNER JOIN nivel_acceso n ON u.id_nivel_usuario = n.id_nivel_acceso " +
                    "WHERE u.username = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setId_usuario(rs.getInt("id_usuario"));
                    usr.setId_nivel_usuario(rs.getInt("id_nivel_usuario"));
                    usr.setUsername(rs.getString("username"));
                    usr.setNombres_usuario(rs.getString("nombres_usuario"));
                    usr.setApellidos_usuario(rs.getString("apellidos_usuario"));
                    usr.setCorreo_usuario(rs.getString("correo_usuario"));
                    usr.setTelefono_usuario(rs.getString("telefono_usuario"));
                    usr.setPassword(rs.getString("password"));
                    usr.setFecha_creacion_usuario(rs.getTimestamp("fecha_creacion_usuario"));
                    usr.setFecha_actualizacion_usuario(rs.getTimestamp("fecha_actualizacion_usuario"));
                    usr.setNombre_nivel_acceso(rs.getString("nombre_nivel_acceso"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por username: " + e.getMessage());
            e.printStackTrace();
        }

        return usr;
    }

    public boolean insertarUsuario(Usuario usr) {
        String sql = "INSERT INTO usuarios (id_nivel_usuario, username, nombres_usuario, apellidos_usuario, " +
                    "correo_usuario, telefono_usuario, password, fecha_creacion_usuario) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usr.getId_nivel_usuario());
            st.setString(2, usr.getUsername());
            st.setString(3, usr.getNombres_usuario());
            st.setString(4, usr.getApellidos_usuario());
            st.setString(5, usr.getCorreo_usuario());
            st.setString(6, usr.getTelefono_usuario());
            st.setString(7, usr.getPassword());

            int filasAfectadas = st.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usr) {
        String sql = "UPDATE usuarios SET id_nivel_usuario = ?, nombres_usuario = ?, apellidos_usuario = ?, " +
                    "correo_usuario = ?, telefono_usuario = ?, fecha_actualizacion_usuario = NOW() " +
                    "WHERE id_usuario = ?";
       
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usr.getId_nivel_usuario());
            st.setString(2, usr.getNombres_usuario());
            st.setString(3, usr.getApellidos_usuario());
            st.setString(4, usr.getCorreo_usuario());
            st.setString(5, usr.getTelefono_usuario());
            st.setInt(6, usr.getId_usuario());

            int filasAfectadas = st.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPassword(int id_usuario, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ?, fecha_actualizacion_usuario = NOW() WHERE id_usuario = ?";
       
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, nuevaPassword);
            st.setInt(2, id_usuario);

            int filasAfectadas = st.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            int filasAfectadas = st.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean existeUsernameParaActualizar(String username, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND id_usuario != ?";
        
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            st.setInt(2, idUsuario);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar username para actualizar: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public List<NivelAcceso> listarNivelesAcceso() {
        List<NivelAcceso> niveles = new ArrayList<>();
        String sql = "SELECT id_nivel_acceso, nombre_nivel_acceso FROM nivel_acceso ORDER BY nombre_nivel_acceso";
        
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            
            while (rs.next()) {
                NivelAcceso nivel = new NivelAcceso();
                nivel.setId_nivel_acceso(rs.getInt("id_nivel_acceso"));
                nivel.setNombre_nivel_acceso(rs.getString("nombre_nivel_acceso"));
                niveles.add(nivel);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar niveles de acceso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return niveles;
    }

    public void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}             
package tiendajyj.dao;

import tiendajyj.model.Usuario;
import tiendajyj.servlet.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setIdNivelUsuario(rs.getInt("id_nivel_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setNombresUsuario(rs.getString("nombres_usuario"));
                usuario.setApellidosUsuario(rs.getString("apellidos_usuario"));
                usuario.setCorreoUsuario(rs.getString("correo_usuario"));
                usuario.setTelefonoUsuario(rs.getString("telefono_usuario"));
                usuario.setPassword(rs.getString("password"));
                // Agregar fechas si existen en la tabla
                try {
                    usuario.setFechaCreacionUsuario(rs.getTimestamp("fecha_creacion_usuario"));
                    usuario.setFechaActualizacionUsuario(rs.getTimestamp("fecha_actualizacion_usuario"));
                } catch (SQLException e) {
                    // Las fechas son opcionales
                }
                lista.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdNivelUsuario(rs.getInt("id_nivel_usuario"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setNombresUsuario(rs.getString("nombres_usuario"));
                    usuario.setApellidosUsuario(rs.getString("apellidos_usuario"));
                    usuario.setCorreoUsuario(rs.getString("correo_usuario"));
                    usuario.setTelefonoUsuario(rs.getString("telefono_usuario"));
                    usuario.setPassword(rs.getString("password"));
                    // Agregar fechas si existen
                    try {
                        usuario.setFechaCreacionUsuario(rs.getTimestamp("fecha_creacion_usuario"));
                        usuario.setFechaActualizacionUsuario(rs.getTimestamp("fecha_actualizacion_usuario"));
                    } catch (SQLException e) {
                        // Las fechas son opcionales
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public boolean insertarUsuario(Usuario usuario) {
        // Consulta simplificada sin fechas (la base de datos las manejará automáticamente)
        String sql = "INSERT INTO usuarios (id_nivel_usuario, username, nombres_usuario, apellidos_usuario, correo_usuario, telefono_usuario, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usuario.getIdNivelUsuario());
            st.setString(2, usuario.getUsername());
            st.setString(3, usuario.getNombresUsuario());
            st.setString(4, usuario.getApellidosUsuario());
            st.setString(5, usuario.getCorreoUsuario());
            st.setString(6, usuario.getTelefonoUsuario());
            st.setString(7, usuario.getPassword());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        // Consulta corregida - removemos fecha_actualizacion_usuario del SET ya que falta el parámetro
        String sql = "UPDATE usuarios SET id_nivel_usuario = ?, username = ?, nombres_usuario = ?, apellidos_usuario = ?, correo_usuario = ?, telefono_usuario = ?, password = ? WHERE id_usuario = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usuario.getIdNivelUsuario());
            st.setString(2, usuario.getUsername());
            st.setString(3, usuario.getNombresUsuario());
            st.setString(4, usuario.getApellidosUsuario());
            st.setString(5, usuario.getCorreoUsuario());
            st.setString(6, usuario.getTelefonoUsuario());
            st.setString(7, usuario.getPassword());
            st.setInt(8, usuario.getIdUsuario()); // Corregido: era posición 9, ahora es 8

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void cerrarConexion() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
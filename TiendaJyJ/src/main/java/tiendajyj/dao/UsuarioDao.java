/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.dao;




import tiendajyj.model.Usuario;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

import java.sql.Connection;

public class UsuarioDao {

    private Connection conn;

    public UsuarioDao() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY fecha_creacion_usuario DESC";

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
                lista.add(usr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        Usuario usr = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usr;
    }

    public Usuario obtenerUsuarioPorUsername(String username) {
        Usuario usr = null;
        String sql = "SELECT * FROM usuarios WHERE username = ?";

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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usr;
    }

    public boolean insertarUsuario(Usuario usr) {
        String sql = "INSERT INTO usuarios (id_nivel_usuario, username, nombres_usuario, apellidos_usuario, correo_usuario, telefono_usuario, password, fecha_creacion_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usr.getId_nivel_usuario());
            st.setString(2, usr.getUsername());
            st.setString(3, usr.getNombres_usuario());
            st.setString(4, usr.getApellidos_usuario());
            st.setString(5, usr.getCorreo_usuario());
            st.setString(6, usr.getTelefono_usuario());
            st.setString(7, usr.getPassword());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usr) {
        String sql = "UPDATE usuarios SET id_nivel_usuario = ?, nombres_usuario = ?, apellidos_usuario = ?, correo_usuario = ?, telefono_usuario = ?, fecha_actualizacion_usuario = NOW() WHERE id_usuario = ?";
       
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, usr.getId_nivel_usuario());
            st.setString(2, usr.getNombres_usuario());
            st.setString(3, usr.getApellidos_usuario());
            st.setString(4, usr.getCorreo_usuario());
            st.setString(5, usr.getTelefono_usuario());
            st.setInt(6, usr.getId_usuario());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPassword(int id_usuario, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ?, fecha_actualizacion_usuario = NOW() WHERE id_usuario = ?";
       
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, nuevaPassword);
            st.setInt(2, id_usuario);

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
            e.printStackTrace();
        }
        
        return false;
    }

    public void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
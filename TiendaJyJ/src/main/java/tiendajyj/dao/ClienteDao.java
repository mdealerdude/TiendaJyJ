/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.dao;
import tiendajyj.model.Cliente;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    private Connection conn;

    public ClienteDao() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setId_usuario(rs.getInt("id_usuario"));
                cli.setDui(rs.getString("dui"));
                cli.setNombres_cliente(rs.getString("nombres_cliente"));
                cli.setApellidos_cliente(rs.getString("apellidos_cliente"));
                cli.setTelefono_cliente(rs.getString("telefono_cliente"));
                cli.setCorreo_cliente(rs.getString("correo_cliente"));
                cli.setFecha_inserccion_cliente(rs.getTimestamp("fecha_inserccion_cliente"));
                cli.setFecha_actualizacion_cliente(rs.getTimestamp("fecha_actualizacion_cliente"));
                lista.add(cli);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Cliente obtenerClientePorId(int id) {
        Cliente cli = null;
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    cli = new Cliente();
                    cli.setId_cliente(rs.getInt("id_cliente"));
                    cli.setId_usuario(rs.getInt("id_usuario"));
                    cli.setDui(rs.getString("dui"));
                    cli.setNombres_cliente(rs.getString("nombres_cliente"));
                    cli.setApellidos_cliente(rs.getString("apellidos_cliente"));
                    cli.setTelefono_cliente(rs.getString("telefono_cliente"));
                    cli.setCorreo_cliente(rs.getString("correo_cliente"));
                    cli.setFecha_inserccion_cliente(rs.getTimestamp("fecha_inserccion_cliente"));
                    cli.setFecha_actualizacion_cliente(rs.getTimestamp("fecha_actualizacion_cliente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cli;
    }

    public boolean insertarCliente(Cliente cli) {
        String sql = "INSERT INTO clientes (id_usuario, dui, nombres_cliente, apellidos_cliente, "
                   + "telefono_cliente, correo_cliente, fecha_inserccion_cliente, fecha_actualizacion_cliente) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, cli.getId_usuario());
            st.setString(2, cli.getDui());
            st.setString(3, cli.getNombres_cliente());
            st.setString(4, cli.getApellidos_cliente());
            st.setString(5, cli.getTelefono_cliente());
            st.setString(6, cli.getCorreo_cliente());
            st.setTimestamp(7, new Timestamp(cli.getFecha_inserccion_cliente().getTime()));
            st.setTimestamp(8, new Timestamp(cli.getFecha_actualizacion_cliente().getTime()));

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCliente(Cliente cli) {
        String sql = "UPDATE clientes SET id_usuario = ?, dui = ?, nombres_cliente = ?, "
                   + "apellidos_cliente = ?, telefono_cliente = ?, correo_cliente = ?, "
                   + "fecha_actualizacion_cliente = ? WHERE id_cliente = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, cli.getId_usuario());
            st.setString(2, cli.getDui());
            st.setString(3, cli.getNombres_cliente());
            st.setString(4, cli.getApellidos_cliente());
            st.setString(5, cli.getTelefono_cliente());
            st.setString(6, cli.getCorreo_cliente());
            st.setTimestamp(7, new Timestamp(cli.getFecha_actualizacion_cliente().getTime()));
            st.setInt(8, cli.getId_cliente());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

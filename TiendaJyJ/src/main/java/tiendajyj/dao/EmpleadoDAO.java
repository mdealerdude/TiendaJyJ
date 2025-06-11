package tiendajyj.dao;

import tiendajyj.model.Empleado;
import tiendajyj.servlet.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private Connection conn;

    public EmpleadoDAO() throws SQLException {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConnection();
    }

    public List<Empleado> listarEmpleados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setId(rs.getInt("id"));
                emp.setNombreEmpleado(rs.getString("nombreEmpleado"));
                emp.setApellidoEmpleado(rs.getString("apellidoEmpleado"));
                emp.setDui(rs.getString("dui"));
                emp.setTelefono(rs.getString("telefono"));
                emp.setDireccion(rs.getString("direccion"));
                emp.setCorreo(rs.getString("correo"));
                emp.setFechaIngreso(rs.getDate("fecha_ingreso"));
                emp.setCargo(rs.getString("cargo"));
                lista.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Empleado obtenerEmpleadoPorId(int id) {
        Empleado emp = null;
        String sql = "SELECT * FROM empleados WHERE id = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    emp = new Empleado();
                    emp.setId(rs.getInt("id"));
                    emp.setNombreEmpleado(rs.getString("nombreEmpleado"));
                    emp.setApellidoEmpleado(rs.getString("apellidoEmpleado"));
                    emp.setDui(rs.getString("dui"));
                    emp.setTelefono(rs.getString("telefono"));
                    emp.setDireccion(rs.getString("direccion"));
                    emp.setCorreo(rs.getString("correo"));
                    emp.setFechaIngreso(rs.getDate("fecha_ingreso"));
                    emp.setCargo(rs.getString("cargo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emp;
    }

    public boolean insertarEmpleado(Empleado emp) {
        String sql = "INSERT INTO empleados (nombreEmpleado, apellidoEmpleado, dui, telefono, direccion, correo, fecha_ingreso, cargo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, emp.getNombreEmpleado());
            st.setString(2, emp.getApellidoEmpleado());
            st.setString(3, emp.getDui());
            st.setString(4, emp.getTelefono());
            st.setString(5, emp.getDireccion());
            st.setString(6, emp.getCorreo());
            st.setDate(7, new java.sql.Date(emp.getFechaIngreso().getTime()));
            st.setString(8, emp.getCargo());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEmpleado(Empleado emp) {
        String sql = "UPDATE empleados SET nombreEmpleado = ?, apellidoEmpleado = ?, dui = ?, telefono = ?, direccion = ?, correo = ?, fecha_ingreso = ?, cargo = ? WHERE id = ?";
       
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, emp.getNombreEmpleado());
            st.setString(2, emp.getApellidoEmpleado());
            st.setString(3, emp.getDui());
            st.setString(4, emp.getTelefono());
            st.setString(5, emp.getDireccion());
            st.setString(6, emp.getCorreo());
            st.setDate(7, new java.sql.Date(emp.getFechaIngreso().getTime()));
            st.setString(8, emp.getCargo());
            st.setInt(9, emp.getId());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
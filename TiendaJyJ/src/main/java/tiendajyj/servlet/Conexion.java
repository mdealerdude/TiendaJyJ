/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tiendajyj.servlet;

import java.sql.*;

public class Conexion {
    private Connection conexion;

    public Conexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_jyj",
                "root", ""
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL", e);
        }
    }

    public Connection getConnection() {
        return conexion;
    }

    public void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}
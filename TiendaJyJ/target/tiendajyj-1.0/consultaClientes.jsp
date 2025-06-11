<%-- 
    Document   : consultaClientes
    Created on : Jun 9, 2025, 3:34:46 PM
    Author     : MINEDUCYT
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row col-md-10 mt-4 mx-auto">
    <table class="table table-striped table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>Nombres</th>
                <th>Apellidos</th>
                <th>DUI</th>
                <th>Teléfono</th>
                <th>Correo</th>
                <th>Fecha de Registro</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
                Conexion conexionDB = null;
                Connection con = null;
                PreparedStatement st = null;
                ResultSet rs = null;

                try {
                    conexionDB = new Conexion();
                    con = conexionDB.getConnection();
                    st = con.prepareStatement("SELECT * FROM clientes");
                    rs = st.executeQuery();

                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("nombres_cliente") %></td>
                <td><%= rs.getString("apellidos_cliente") %></td>
                <td><%= rs.getString("dui") %></td>
                <td><%= rs.getString("telefono_cliente") %></td>
                <td><%= rs.getString("correo_cliente") %></td>
                <td><%= rs.getDate("fecha_inserccion_cliente") %></td>
                <td>
                    <a class="btn btn-warning btn-sm" href="editar.jsp?id=<%= rs.getInt("id_cliente") %>">Editar</a>
                    <a class="btn btn-danger btn-sm" href="eliminar.jsp?id=<%= rs.getInt("id_cliente") %>"
                       onclick="return confirm('¿Estás seguro de eliminar este cliente?')">Eliminar</a>
                </td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='7'>Error al cargar datos: " + e.getMessage() + "</td></tr>");
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                    if (st != null) try { st.close(); } catch (SQLException ignore) {}
                    if (conexionDB != null) try { conexionDB.cerrarConexion(); } catch (SQLException ignore) {}
                }
            %>
        </tbody>
    </table>
</div>
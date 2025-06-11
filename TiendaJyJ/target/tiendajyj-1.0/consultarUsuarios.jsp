<%-- 
    Document   : consultaUsuarios
    Created on : Jun 9, 2025, 3:34:46 PM
    Author     : MINEDUCYT
--%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row col-md-12 mt-4 mx-auto">
    <table class="table table-striped table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>Username</th>
                <th>Nombres</th>
                <th>Apellidos</th>
                <th>Correo</th>
                <th>Teléfono</th>
                <th>Nivel Usuario</th>
                <th>Fecha de Registro</th>
                <th>Última Actualización</th>
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
                    // Query con JOIN para obtener el nombre del nivel de usuario
                    String query = "SELECT u.*, nu.nombre_nivel " +
                                  "FROM usuarios u " +
                                  "LEFT JOIN nivel_usuario nu ON u.id_nivel_usuario = nu.id_nivel_usuario " +
                                  "ORDER BY u.fecha_creacion_usuario DESC";
                    st = con.prepareStatement(query);
                    rs = st.executeQuery();
                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("username") %></td>
                <td><%= rs.getString("nombres_usuario") %></td>
                <td><%= rs.getString("apellidos_usuario") %></td>
                <td><%= rs.getString("correo_usuario") %></td>
                <td><%= rs.getString("telefono_usuario") %></td>
                <td>
                    <% 
                    String nivelUsuario = rs.getString("nombre_nivel");
                    if (nivelUsuario != null) {
                    %>
                        <span class="badge bg-primary"><%= nivelUsuario %></span>
                    <% } else { %>
                        <span class="badge bg-secondary">Sin nivel</span>
                    <% } %>
                </td>
                <td><%= rs.getTimestamp("fecha_creacion_usuario") %></td>
                <td>
                    <% 
                    java.sql.Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion_usuario");
                    if (fechaActualizacion != null) {
                    %>
                        <%= fechaActualizacion %>
                    <% } else { %>
                        <span class="text-muted">Sin actualizar</span>
                    <% } %>
                </td>
                <td>
                    <div class="btn-group" role="group">
                        <a class="btn btn-info btn-sm" href="verUsuario.jsp?id=<%= rs.getInt("id_usuario") %>" 
                           title="Ver detalles">
                            <i class="fas fa-eye"></i>
                        </a>
                        <a class="btn btn-warning btn-sm" href="editarUsuario.jsp?id=<%= rs.getInt("id_usuario") %>"
                           title="Editar">
                            <i class="fas fa-edit"></i>
                        </a>
                        <a class="btn btn-secondary btn-sm" href="cambiarPassword.jsp?id=<%= rs.getInt("id_usuario") %>"
                           title="Cambiar contraseña">
                            <i class="fas fa-key"></i>
                        </a>
                        <% 
                        // No permitir eliminar al usuario actual (si tienes sesión activa)
                        Integer usuarioActualId = (Integer) session.getAttribute("id_usuario");
                        int idUsuarioFila = rs.getInt("id_usuario");
                        if (usuarioActualId == null || usuarioActualId != idUsuarioFila) {
                        %>
                        <a class="btn btn-danger btn-sm" href="eliminarUsuario.jsp?id=<%= idUsuarioFila %>"
                           onclick="return confirm('¿Estás seguro de eliminar este usuario? Esta acción no se puede deshacer.')"
                           title="Eliminar">
                            <i class="fas fa-trash"></i>
                        </a>
                        <% } %>
                    </div>
                </td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='9' class='text-danger text-center'>");
                    out.println("<i class='fas fa-exclamation-triangle'></i> ");
                    out.println("Error al cargar datos: " + e.getMessage());
                    out.println("</td></tr>");
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                    if (st != null) try { st.close(); } catch (SQLException ignore) {}
                    if (conexionDB != null) try { conexionDB.cerrarConexion(); } catch (SQLException ignore) {}
                }
            %>
        </tbody>
    </table>
</div>
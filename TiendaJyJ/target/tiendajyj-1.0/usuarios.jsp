<%-- 
    Document   : usuarios
    Created on : Jun 9, 2025, 3:29:59 PM
    Author     : MINEDUCYT
--%>
<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Usuarios</title>
    <link rel="stylesheet"href="css/bootstrap.min.css">
    <link rel="stylesheet"href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="mb-0">
                        <i class="fas fa-users"></i> Usuarios del Sistema
                    </h2>
                    <a href="registroUsuario.jsp" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Agregar Nuevo Usuario
                    </a>
                </div>
                
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Nombres</th>
                                        <th>Apellidos</th>
                                        <th>Correo</th>
                                        <th>Teléfono</th>
                                        <th>Nivel</th>
                                        <th>Fecha de Registro</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");
                                        if (lista != null && !lista.isEmpty()) {
                                            for (Usuario usr : lista) {
                                    %>
                                    <tr>
                                        <td><%= usr.getIdUsuario() %></td>
                                        <td>
                                            <strong><%= usr.getUsername() %></strong>
                                        </td>
                                        <td><%= usr.getNombresUsuario() %></td>
                                        <td><%= usr.getApellidosUsuario() %></td>
                                        <td>
                                            <a href="mailto:<%= usr.getCorreoUsuario() %>" class="text-decoration-none">
                                                <%= usr.getCorreoUsuario() %>
                                            </a>
                                        </td>
                                        <td><%= usr.getTelefonoUsuario() %></td>
                                        <td>
                                            <% 
                                            // Mostrar badge según el nivel de usuario
                                            int nivelUsuario = usr.getIdNivelUsuario();
                                            String badgeClass = "";
                                            String nivelTexto = "";
                                            
                                            switch(nivelUsuario) {
                                                case 1:
                                                    badgeClass = "bg-danger";
                                                    nivelTexto = "Administrador";
                                                    break;
                                                case 2:
                                                    badgeClass = "bg-warning";
                                                    nivelTexto = "Supervisor";
                                                    break;
                                                case 3:
                                                    badgeClass = "bg-info";
                                                    nivelTexto = "Empleado";
                                                    break;
                                                default:
                                                    badgeClass = "bg-secondary";
                                                    nivelTexto = "Usuario";
                                            }
                                            %>
                                            <span class="badge <%= badgeClass %>"><%= nivelTexto %></span>
                                        </td>
                                        <td>
                                            <% 
                                            if (usr.getFechaCreacionUsuario() != null) {
                                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                                            %>
                                                <%= sdf.format(usr.getFechaCreacionUsuario()) %>
                                            <% } else { %>
                                                <span class="text-muted">Sin fecha</span>
                                            <% } %>
                                        </td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <a href="UsuarioServlet?accion=ver&id=<%= usr.getIdUsuario() %>" 
                                                   class="btn btn-info btn-sm" title="Ver detalles">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="UsuarioServlet?accion=editar&id=<%= usr.getIdUsuario() %>" 
                                                   class="btn btn-warning btn-sm" title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="UsuarioServlet?accion=cambiarPassword&id=<%= usr.getIdUsuario() %>" 
                                                   class="btn btn-secondary btn-sm" title="Cambiar contraseña">
                                                    <i class="fas fa-key"></i>
                                                </a>
                                                <% 
                                                // Verificar si el usuario actual puede eliminar este usuario
                                                Integer usuarioActualId = (Integer) session.getAttribute("id_usuario");
                                                if (usuarioActualId == null || usuarioActualId != usr.getIdUsuario()) {
                                                %>
                                                <a href="UsuarioServlet?accion=eliminar&id=<%= usr.getIdUsuario() %>" 
                                                   class="btn btn-danger btn-sm" title="Eliminar"
                                                   onclick="return confirm('¿Estás seguro de eliminar al usuario <%= usr.getUsername() %>? Esta acción no se puede deshacer.')">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                                <% } else { %>
                                                <button class="btn btn-outline-secondary btn-sm" disabled title="No puedes eliminarte a ti mismo">
                                                    <i class="fas fa-ban"></i>
                                                </button>
                                                <% } %>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        } else {
                                    %>
                                    <tr>
                                        <td colspan="9" class="text-center py-4">
                                            <div class="text-muted">
                                                <i class="fas fa-user-slash fa-2x mb-2"></i>
                                                <p class="mb-0">No hay usuarios registrados.</p>
                                            </div>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
    
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
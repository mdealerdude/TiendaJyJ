<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("USER") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    String usuario = (String) session.getAttribute("USER");
    List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios - Tienda JyJ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .table th {
            border-top: none;
        }
        .btn-group .btn {
            margin: 0 1px;
        }
    </style>
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    
    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="fas fa-users"></i> Gestión de Usuarios
                        </h4>
                        <a href="UsuarioServlet?accion=nuevo" class="btn btn-light">
                            <i class="fas fa-plus"></i> Nuevo Usuario
                        </a>
                    </div>
                    <div class="card-body">
                        <% if (lista != null && !lista.isEmpty()) { %>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Nombres</th>
                                        <th>Apellidos</th>
                                        <th>Correo</th>
                                        <th>Teléfono</th>
                                        <th>Nivel Usuario</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Usuario u : lista) { %>
                                    <tr>
                                        <td><%= u.getIdUsuario() %></td>
                                        <td><strong><%= u.getUsername() %></strong></td>
                                        <td><%= u.getNombresUsuario() %></td>
                                        <td><%= u.getApellidosUsuario() %></td>
                                        <td><%= u.getCorreoUsuario() %></td>
                                        <td><%= u.getTelefonoUsuario() %></td>
                                        <td>
                                            <% if (u.getIdNivelUsuario() == 1) { %>
                                                <span class="badge bg-danger">Administrador</span>
                                            <% } else if (u.getIdNivelUsuario() == 2) { %>
                                                <span class="badge bg-warning">Usuario</span>
                                            <% } else { %>
                                                <span class="badge bg-secondary">Nivel <%= u.getIdNivelUsuario() %></span>
                                            <% } %>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group" role="group">
                                                <a class="btn btn-warning btn-sm" 
                                                   href="UsuarioServlet?accion=editar&id=<%= u.getIdUsuario() %>"
                                                   title="Editar">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <% 
                                                // No permitir eliminar al usuario actual
                                                Integer usuarioActualId = (Integer) session.getAttribute("id_usuario");
                                                if (usuarioActualId == null || usuarioActualId != u.getIdUsuario()) {
                                                %>
                                                <a class="btn btn-danger btn-sm" 
                                                   href="UsuarioServlet?accion=eliminar&id=<%= u.getIdUsuario() %>"
                                                   onclick="return confirm('¿Estás seguro de eliminar al usuario <%= u.getUsername() %>? Esta acción no se puede deshacer.')"
                                                   title="Eliminar">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                                <% } %>
                                            </div>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <div class="text-center py-5">
                            <i class="fas fa-users fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No hay usuarios registrados</h5>
                            <p class="text-muted">Comienza agregando el primer usuario</p>
                            <a href="UsuarioServlet?accion=nuevo" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Agregar Usuario
                            </a>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
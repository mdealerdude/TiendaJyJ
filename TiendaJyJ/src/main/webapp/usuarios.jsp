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
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
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
            background-color: #343a40;
            color: white;
            font-weight: 600;
        }
        .btn-group .btn {
            margin: 0 1px;
        }
        .table-hover tbody tr:hover {
            background-color: rgba(0,123,255,.075);
        }
        .badge {
            font-size: 0.75em;
        }
        .stats-card {
            border-left: 4px solid #007bff;
            background: linear-gradient(45deg, rgba(0,123,255,0.1), rgba(255,255,255,0.9));
        }
        .empty-state {
            text-align: center;
            padding: 3rem 1rem;
        }
        .empty-state i {
            font-size: 4rem;
            color: #6c757d;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    
    <div class="container-fluid mt-4">
        <!-- Mensajes de estado -->
        <% if (mensaje != null) { %>
        <div class="row mb-3">
            <div class="col-12">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i> <%= mensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </div>
        </div>
        <% } %>
        
        <% if (error != null) { %>
        <div class="row mb-3">
            <div class="col-12">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> <%= error %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </div>
        </div>
        <% } %>
        
        <!-- Estadísticas rápidas -->
        <% if (lista != null && !lista.isEmpty()) { %>
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card stats-card">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title text-muted mb-1">Total Usuarios</h6>
                                <h3 class="mb-0 text-primary"><%= lista.size() %></h3>
                            </div>
                            <i class="fas fa-users fa-2x text-primary opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card border-warning">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title text-muted mb-1">Administradores</h6>
                                <h3 class="mb-0 text-warning">
                                    <% 
                                    int admins = 0;
                                    for (Usuario u : lista) {
                                        if (u.getIdNivelUsuario() == 1) admins++;
                                    }
                                    %>
                                    <%= admins %>
                                </h3>
                            </div>
                            <i class="fas fa-user-shield fa-2x text-warning opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card border-success">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title text-muted mb-1">Usuarios Regulares</h6>
                                <h3 class="mb-0 text-success">
                                    <% 
                                    int users = 0;
                                    for (Usuario u : lista) {
                                        if (u.getIdNivelUsuario() == 2) users++;
                                    }
                                    %>
                                    <%= users %>
                                </h3>
                            </div>
                            <i class="fas fa-user fa-2x text-success opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card border-info">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title text-muted mb-1">Usuario Actual</h6>
                                <h6 class="mb-0 text-info"><%= usuario %></h6>
                            </div>
                            <i class="fas fa-user-circle fa-2x text-info opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
        
        <!-- Tabla de usuarios -->
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
                    <div class="card-body p-0">
                        <% if (lista != null && !lista.isEmpty()) { %>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col"><i class="fas fa-user me-1"></i>Username</th>
                                        <th scope="col"><i class="fas fa-id-card me-1"></i>Nombres</th>
                                        <th scope="col"><i class="fas fa-id-card me-1"></i>Apellidos</th>
                                        <th scope="col"><i class="fas fa-envelope me-1"></i>Correo</th>
                                        <th scope="col"><i class="fas fa-phone me-1"></i>Teléfono</th>
                                        <th scope="col"><i class="fas fa-shield-alt me-1"></i>Nivel</th>
                                        <th scope="col" class="text-center"><i class="fas fa-cogs me-1"></i>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                    int contador = 1;
                                    for (Usuario u : lista) { 
                                    %>
                                    <tr>
                                        <td><span class="badge bg-secondary"><%= contador++ %></span></td>
                                        <td>
                                            <strong class="text-primary"><%= u.getUsername() %></strong>
                                            <% if (u.getUsername().equals(usuario)) { %>
                                                <span class="badge bg-info ms-1">Tú</span>
                                            <% } %>
                                        </td>
                                        <td><%= u.getNombresUsuario() != null ? u.getNombresUsuario() : "<em class='text-muted'>Sin especificar</em>" %></td>
                                        <td><%= u.getApellidosUsuario() != null ? u.getApellidosUsuario() : "<em class='text-muted'>Sin especificar</em>" %></td>
                                        <td>
                                            <% if (u.getCorreoUsuario() != null && !u.getCorreoUsuario().trim().isEmpty()) { %>
                                                <a href="mailto:<%= u.getCorreoUsuario() %>" class="text-decoration-none">
                                                    <%= u.getCorreoUsuario() %>
                                                </a>
                                            <% } else { %>
                                                <em class="text-muted">Sin especificar</em>
                                            <% } %>
                                        </td>
                                        <td>
                                            <% if (u.getTelefonoUsuario() != null && !u.getTelefonoUsuario().trim().isEmpty()) { %>
                                                <a href="tel:<%= u.getTelefonoUsuario() %>" class="text-decoration-none">
                                                    <%= u.getTelefonoUsuario() %>
                                                </a>
                                            <% } else { %>
                                                <em class="text-muted">Sin especificar</em>
                                            <% } %>
                                        </td>
                                        <td>
                                            <% if (u.getIdNivelUsuario() == 1) { %>
                                                <span class="badge bg-danger">
                                                    <i class="fas fa-user-shield me-1"></i>Administrador
                                                </span>
                                            <% } else if (u.getIdNivelUsuario() == 2) { %>
                                                <span class="badge bg-warning text-dark">
                                                    <i class="fas fa-user me-1"></i>Usuario
                                                </span>
                                            <% } else { %>
                                                <span class="badge bg-secondary">
                                                    <i class="fas fa-question me-1"></i>Nivel <%= u.getIdNivelUsuario() %>
                                                </span>
                                            <% } %>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group" role="group" aria-label="Acciones de usuario">
                                                <a class="btn btn-outline-primary btn-sm" 
                                                   href="UsuarioServlet?accion=editar&id=<%= u.getIdUsuario() %>"
                                                   title="Editar usuario <%= u.getUsername() %>"
                                                   data-bs-toggle="tooltip">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <% 
                                                // No permitir eliminar al usuario actual
                                                Integer usuarioActualId = (Integer) session.getAttribute("id_usuario");
                                                if (usuarioActualId == null || usuarioActualId != u.getIdUsuario()) {
                                                %>
                                                <button type="button" class="btn btn-outline-danger btn-sm" 
                                                        onclick="confirmarEliminacion('<%= u.getUsername() %>', <%= u.getIdUsuario() %>)"
                                                        title="Eliminar usuario <%= u.getUsername() %>"
                                                        data-bs-toggle="tooltip">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                                <% } else { %>
                                                <button type="button" class="btn btn-outline-secondary btn-sm" 
                                                        disabled
                                                        title="No puedes eliminarte a ti mismo"
                                                        data-bs-toggle="tooltip">
                                                    <i class="fas fa-ban"></i>
                                                </button>
                                                <% } %>
                                            </div>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% } else { %>
                        <div class="empty-state">
                            <i class="fas fa-users"></i>
                            <h5 class="text-muted mb-3">No hay usuarios registrados</h5>
                            <p class="text-muted mb-4">Comienza agregando el primer usuario al sistema</p>
                            <a href="UsuarioServlet?accion=nuevo" class="btn btn-primary btn-lg">
                                <i class="fas fa-plus me-2"></i>Agregar Usuario
                            </a>
                        </div>
                        <% } %>
                    </div>
                    <% if (lista != null && !lista.isEmpty()) { %>
                    <div class="card-footer bg-light">
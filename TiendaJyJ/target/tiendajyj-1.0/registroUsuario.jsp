<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("USER") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    Usuario usr = (Usuario) request.getAttribute("usuario");
    boolean editar = (usr != null);
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><%= editar ? "Editar" : "Registrar" %> Usuario - Tienda JyJ</title>
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
        .required {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="<%= editar ? "fas fa-user-edit" : "fas fa-user-plus" %>"></i>
                            <%= editar ? "Editar" : "Registrar Nuevo" %> Usuario
                        </h4>
                    </div>
                    <div class="card-body">
                        <!-- Mostrar mensajes -->
                        <% if (error != null) { %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle"></i>
                                <%= error %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <% } %>
                        
                        <% if (mensaje != null) { %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle"></i>
                                <%= mensaje %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <% } %>

                        <form action="UsuarioServlet" method="post" id="formUsuario" novalidate>
                            <% if (editar) { %>
                                <input type="hidden" name="id_usuario" value="<%= usr.getIdUsuario() %>">
                                <input type="hidden" name="accion" value="actualizar">
                            <% } else { %>
                                <input type="hidden" name="accion" value="insertar">
                            <% } %>
                            
                            <!-- Información de acceso -->
                            <div class="row mb-4">
                                <div class="col-12">
                                    <h5 class="text-primary border-bottom pb-2">
                                        <i class="fas fa-key"></i> Información de Acceso
                                    </h5>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user"></i> Username <span class="required">*</span>
                                    </label>
                                    <input type="text" name="username" class="form-control" required 
                                           value="<%= editar ? usr.getUsername() : "" %>"
                                           placeholder="Nombre de usuario único"
                                           pattern="[a-zA-Z0-9_]+"
                                           title="Solo letras, números y guiones bajos"
                                           maxlength="50">
                                    <div class="form-text">Solo letras, números y guiones bajos (máx. 50 caracteres)</div>
                                    <div class="invalid-feedback">
                                        El username es requerido y solo puede contener letras, números y guiones bajos.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-users-cog"></i> Nivel de Usuario <span class="required">*</span>
                                    </label>
                                    <select name="id_nivel_usuario" class="form-select" required>
                                        <option value="">Seleccionar nivel...</option>
                                        <%
                                            // Cargar niveles de usuario desde la base de datos
                                            Conexion conexionDB = null;
                                            Connection con = null;
                                            PreparedStatement st = null;
                                            ResultSet rs = null;
                                            try {
                                                conexionDB = new Conexion();
                                                con = conexionDB.getConnection();
                                                st = con.prepareStatement("SELECT * FROM nivel_usuario ORDER BY id_nivel_usuario");
                                                rs = st.executeQuery();
                                                while (rs.next()) {
                                                    int idNivel = rs.getInt("id_nivel_usuario");
                                                    String nombreNivel = rs.getString("nombre_nivel");
                                                    boolean selected = editar && usr.getIdNivelUsuario() == idNivel;
                                        %>
                                        <option value="<%= idNivel %>" <%= selected ? "selected" : "" %>>
                                            <%= nombreNivel %>
                                        </option>
                                        <%
                                                }
                                            } catch (Exception e) {
                                                // Si no existe la tabla nivel_usuario, usar valores por defecto
                                        %>
                                        <option value="1" <%= editar && usr.getIdNivelUsuario() == 1 ? "selected" : "" %>>Administrador</option>
                                        <option value="2" <%= editar && usr.getIdNivelUsuario() == 2 ? "selected" : "" %>>Usuario</option>
                                        <%
                                            } finally {
                                                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                                                if (st != null) try { st.close(); } catch (SQLException ignore) {}
                                                if (conexionDB != null) try { conexionDB.cerrarConexion(); } catch (SQLException ignore) {}
                                            }
                                        %>
                                    </select>
                                    <div class="invalid-feedback">
                                        Debe seleccionar un nivel de usuario.
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-12">
                                    <label class="form-label">
                                        <i class="fas fa-lock"></i> Contraseña 
                                        <% if (!editar) { %><span class="required">*</span><% } %>
                                    </label>
                                    <% if (!editar) { %>
                                    <input type="password" name="password" class="form-control" required 
                                           placeholder="Ingrese la contraseña" minlength="4" maxlength="100">
                                    <div class="form-text">Mínimo 4 caracteres</div>
                                    <div class="invalid-feedback">
                                        La contraseña es requerida y debe tener al menos 4 caracteres.
                                    </div>
                                    <% } else { %>
                                    <input type="password" name="password" class="form-control" 
                                           placeholder="Dejar en blanco para mantener la actual" maxlength="100">
                                    <div class="form-text">Dejar vacío para mantener la contraseña actual</div>
                                    <% } %>
                                </div>
                            </div>
                            
                            <!-- Información personal -->
                            <div class="row mb-4 mt-4">
                                <div class="col-12">
                                    <h5 class="text-primary border-bottom pb-2">
                                        <i class="fas fa-id-card"></i> Información Personal
                                    </h5>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user"></i> Nombres <span class="required">*</span>
                                    </label>
                                    <input type="text" name="nombres_usuario" class="form-control" required 
                                           value="<%= editar ? (usr.getNombresUsuario() != null ? usr.getNombresUsuario() : "") : "" %>"
                                           placeholder="Nombres completos" maxlength="100">
                                    <div class="invalid-feedback">
                                        Los nombres son requeridos.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user"></i> Apellidos <span class="required">*</span>
                                    </label>
                                    <input type="text" name="apellidos_usuario" class="form-control" required 
                                           value="<%= editar ? (usr.getApellidosUsuario() != null ? usr.getApellidosUsuario() : "") : "" %>"
                                           placeholder="Apellidos completos" maxlength="100">
                                    <div class="invalid-feedback">
                                        Los apellidos son requeridos.
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Información de contacto -->
                            <div class="row mb-4 mt-4">
                                <div class="col-12">
                                    <h5 class="text-primary border-bottom pb-2">
                                        <i class="fas fa-address-book"></i> Información de Contacto
                                    </h5>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-envelope"></i> Correo Electrónico <span class="required">*</span>
                                    </label>
                                    <input type="email" name="correo_usuario" class="form-control" required 
                                           value="<%= editar ? (usr.getCorreoUsuario() != null ? usr.getCorreoUsuario() : "") : "" %>"
                                           placeholder="usuario@ejemplo.com" maxlength="100">
                                    <div class="invalid-feedback">
                                        Ingrese un correo electrónico válido.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-phone"></i> Teléfono <span class="required">*</span>
                                    </label>
                                    <input type="tel" name="telefono_usuario" class="form-control" required 
                                           value="<%= editar ? (usr.getTelefonoUsuario() != null ? usr.getTelefonoUsuario() : "") : "" %>"
                                           placeholder="0000-0000" maxlength="20">
                                    <div class="form-text">Ejemplo: 0000-0000</div>
                                    <div class="invalid-feedback">
                                        El teléfono es requerido.
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Botones -->
                            <div class="row mt-4">
                                <div class="col-12">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <a href="UsuarioServlet?accion=listar" class="btn btn-secondary me-md-2">
                                            <i class="fas fa-times"></i> Cancelar
                                        </a>
                                        <button type="submit" class="btn btn-success">
                                            <i class="<%= editar ? "fas fa-save" : "fas fa-plus" %>"></i>
                                            <%= editar ? "Actualizar" : "Registrar" %> Usuario
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
    
    <!-- Validación del formulario -->
    <script>
        (function() {
            'use strict';
            
            // Obtener el formulario
            const form = document.getElementById('formUsuario');
            
            // Validación en tiempo real
            form.addEventListener('submit', function(event) {
                event.preventDefault();
                event.stopPropagation();
                
                let isValid = true;
                
                // Validar username
                const username = document.querySelector('input[name="username"]');
                if (!/^[a-zA-Z0-9_]+$/.test(username.value.trim())) {
                    username.classList.add('is-invalid');
                    isValid = false;
                } else {
                    username.classList.remove('is-invalid');
                    username.classList.add('is-valid');
                }
                
                // Validar contraseña solo para nuevos usuarios
                <% if (!editar) { %>
                const password = document.querySelector('input[name="password"]');
                if (password.value.length < 4) {
                    password.classList.add('is-invalid');
                    isValid = false;
                } else {
                    password.classList.remove('is-invalid');
                    password.classList.add('is-valid');
                }
                <% } %>
                
                // Validar email
                const email = document.querySelector('input[name="correo_usuario"]');
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email.value.trim())) {
                    email.classList.add('is-invalid');
                    isValid = false;
                } else {
                    email.classList.remove('is-invalid');
                    email.classList.add('is-valid');
                }
                
                // Validar campos requeridos
                const requiredFields = form.querySelectorAll('[required]');
                requiredFields.forEach(function(field) {
                    if (!field.value.trim()) {
                        field.classList.add('is-invalid');
                        isValid = false;
                    } else {
                        field.classList.remove('is-invalid');
                        field.classList.add('is-valid');
                    }
                });
                
                form.classList.add('was-validated');
                
                if (isValid) {
                    // Enviar el formulario
                    form.submit();
                }
            });
            
            // Limpiar validación en tiempo real
            const inputs = form.querySelectorAll('input, select');
            inputs.forEach(function(input) {
                input.addEventListener('input', function() {
                    if (this.classList.contains('is-invalid')) {
                        this.classList.remove('is-invalid');
                    }
                });
            });
        })();
    </script>
</body>
</html>
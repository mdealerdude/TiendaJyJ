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
        .section-header {
            background: linear-gradient(45deg, #007bff, #0056b3);
            color: white;
            margin: -15px -15px 20px -15px;
            padding: 15px;
            border-radius: 0.375rem 0.375rem 0 0;
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
                        <!-- Mostrar mensajes de error si existen -->
                        <%
                            String error = request.getParameter("error");
                            if ("insert".equals(error)) {
                        %>
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-triangle"></i>
                                Error al insertar el usuario. Verifique que el username no esté duplicado.
                            </div>
                        <%
                            } else if ("update".equals(error)) {
                        %>
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-triangle"></i>
                                Error al actualizar el usuario. Inténtelo nuevamente.
                            </div>
                        <% } %>

                        <form action="UsuarioServlet" method="post" id="formUsuario">
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
                                        <i class="fas fa-user"></i> Username *
                                    </label>
                                    <input type="text" name="username" class="form-control" required 
                                           value="<%= editar ? usr.getUsername() : "" %>"
                                           placeholder="Nombre de usuario único"
                                           pattern="[a-zA-Z0-9_]+"
                                           title="Solo letras, números y guiones bajos">
                                    <div class="form-text">Solo letras, números y guiones bajos</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-users-cog"></i> Nivel de Usuario *
                                    </label>
                                    <select name="id_nivel_usuario" class="form-select" required>
                                        <option value="">Seleccionar nivel</option>
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
                                </div>
                            </div>
                            
                            <% if (!editar) { %>
                            <div class="row mb-3">
                                <div class="col-md-12">
                                    <label class="form-label">
                                        <i class="fas fa-lock"></i> Contraseña *
                                    </label>
                                    <input type="password" name="password" class="form-control" required 
                                           placeholder="Ingrese la contraseña" minlength="4">
                                    <div class="form-text">Mínimo 4 caracteres</div>
                                </div>
                            </div>
                            <% } else { %>
                            <div class="row mb-3">
                                <div class="col-md-12">
                                    <label class="form-label">
                                        <i class="fas fa-lock"></i> Contraseña
                                    </label>
                                    <input type="password" name="password" class="form-control" 
                                           value="<%= usr.getPassword() %>"
                                           placeholder="Dejar en blanco para mantener la actual">
                                    <div class="form-text">Dejar vacío para mantener la contraseña actual</div>
                                </div>
                            </div>
                            <% } %>
                            
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
                                        <i class="fas fa-user"></i> Nombres *
                                    </label>
                                    <input type="text" name="nombres_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getNombresUsuario() : "" %>"
                                           placeholder="Nombres completos">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user"></i> Apellidos *
                                    </label>
                                    <input type="text" name="apellidos_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getApellidosUsuario() : "" %>"
                                           placeholder="Apellidos completos">
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
                                        <i class="fas fa-envelope"></i> Correo Electrónico *
                                    </label>
                                    <input type="email" name="correo_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getCorreoUsuario() : "" %>"
                                           placeholder="usuario@ejemplo.com">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-phone"></i> Teléfono *
                                    </label>
                                    <input type="tel" name="telefono_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getTelefonoUsuario() : "" %>"
                                           placeholder="0000-0000">
                                    <div class="form-text">Formato: 0000-0000</div>
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
        document.getElementById('formUsuario').addEventListener('submit', function(e) {
            const password = document.querySelector('input[name="password"]');
            const username = document.querySelector('input[name="username"]');
            
            // Validar username
            if (!/^[a-zA-Z0-9_]+$/.test(username.value)) {
                e.preventDefault();
                alert('El username solo puede contener letras, números y guiones bajos');
                username.focus();
                return;
            }
            
            // Validar contraseña solo para nuevos usuarios
            <% if (!editar) { %>
            if (password.value.length < 4) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 4 caracteres');
                password.focus();
                return;
            }
            <% } %>
        });
    </script>
</body>
</html>
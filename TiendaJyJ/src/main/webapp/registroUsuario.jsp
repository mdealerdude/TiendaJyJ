
<%-- 
    Document   : registroUsuario
    Created on : Jun 9, 2025, 3:41:29 PM
    Author     : MINEDUCYT
--%>
<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    Usuario usr = (Usuario) request.getAttribute("Usuario");
    boolean editar = (usr != null);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible"content="IE=edge">
<meta name="viewport"content="width=device-width, initial-scale=1">
<link rel="stylesheet"href="css/bootstrap.min.css">
<link rel="stylesheet"href="css/estilos.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<title><%= editar ? "Editar" : "Registrar" %> Usuario</title>
    
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="<%= editar ? "fas fa-user-edit" : "fas fa-user-plus" %>"></i>
                            <%= editar ? "Editar" : "Registrar Nuevo" %> Usuario
                        </h4>
                    </div>
                    <div class="card-body">
                        <form action="UsuarioServlet" method="post" id="formUsuario">
                            <% if (editar) { %>
                                <input type="hidden" name="id_usuario" value="<%= usr.getId_usuario() %>">
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
                                           <%= editar ? "readonly" : "" %>>
                                    <div class="form-text">Solo letras, números y guiones bajos</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-users-cog"></i> Nivel de Usuario *
                                    </label>
                                    <select name="id_nivel_usuario" class="form-select" required>
                                        <option value="">Seleccionar nivel</option>
                                        <option value="">1</option>
                                        <option value="">2</option>
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
                                                    boolean selected = editar && usr.getId_nivel_usuario() == idNivel;
                                        %>
                                        <option value="<%= idNivel %>" <%= selected ? "selected" : "" %>>
                                            <%= nombreNivel %>
                                        </option>
                                        <%
                                                }
                                            } catch (Exception e) {
                                                out.println("<!-- Error al cargar niveles: " + e.getMessage() + " -->");
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
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-lock"></i> Contraseña *
                                    </label>
                                    <input type="password" name="password" class="form-control" required 
                                           placeholder="Mínimo 6 caracteres" minlength="6">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-lock"></i> Confirmar Contraseña *
                                    </label>
                                    <input type="password" name="confirmar_password" class="form-control" required 
                                           placeholder="Repetir contraseña" minlength="6">
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
                                           value="<%= editar ? usr.getNombres_usuario() : "" %>"
                                           placeholder="Nombres completos">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user"></i> Apellidos *
                                    </label>
                                    <input type="text" name="apellidos_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getApellidos_usuario() : "" %>"
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
                                           value="<%= editar ? usr.getCorreo_usuario() : "" %>"
                                           placeholder="usuario@ejemplo.com">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-phone"></i> Teléfono *
                                    </label>
                                    <input type="tel" name="telefono_usuario" class="form-control" required 
                                           value="<%= editar ? usr.getTelefono_usuario() : "" %>"
                                           placeholder="0000-0000" pattern="[0-9]{4}-[0-9]{4}">
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
<%-- 
    Document   : registroEmpleado
    Created on : 13 may 2025, 11:41:29 p. m.
    Author     : juandiaz
--%>

<%@page import="tiendajyj.model.Empleado"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    Empleado emp = (Empleado) request.getAttribute("Empleado");
    boolean editar = (emp != null);
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

<title><%= editar ? "Editar" : "Registrar" %> Empleado</title>
    
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-5">
        <h2 class="mb-4"><%= editar ? "Editar" : "Registrar Nuevo" %> Empleado</h2>
        <form action="EmpleadoServlet" method="post">
            <% if (editar) { %>
                <input type="hidden" name="id" value="<%= emp.getId() %>">
                <input type="hidden" name="accion" value="actualizar">
            <% } else { %>
                <input type="hidden" name="accion" value="insertar">
            <% } %>

            <div class="mb-3">
                <label class="form-label">Nombre</label>
                <input type="text" name="nombreEmpleado" class="form-control" required value="<%= editar ? emp.getNombreEmpleado() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Apellido</label>
                <input type="text" name="apellidoEmpleado" class="form-control" required value="<%= editar ? emp.getApellidoEmpleado() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">DUI</label>
                <input type="text" name="dui" class="form-control" required value="<%= editar ? emp.getDui() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-control" required value="<%= editar ? emp.getTelefono() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Dirección</label>
                <input type="text" name="direccion" class="form-control" required value="<%= editar ? emp.getDireccion() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Correo</label>
                <input type="email" name="correo" class="form-control" required value="<%= editar ? emp.getCorreo() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Fecha Ingreso</label>
                <input type="date" name="fecha_ingreso" class="form-control" required value="<%= editar ? emp.getFechaIngreso() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Cargo</label>
                <input type="text" name="cargo" class="form-control" required value="<%= editar ? emp.getCargo() : "" %>">
            </div>

            <button type="submit" class="btn btn-success"><%= editar ? "Actualizar" : "Registrar" %></button>
            <a href="EmpleadoServlet?accion=listar" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>
</body>
</html>
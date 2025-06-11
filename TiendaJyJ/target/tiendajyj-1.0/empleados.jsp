<%-- 
    Document   : empleados
    Created on : 19 may 2025, 10:09:46 p. m.
    Author     : juandiaz
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="tiendajyj.model.Empleado" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Empleados</title>
    <link rel="stylesheet"href="css/bootstrap.min.css">
    <link rel="stylesheet"href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-4">
        <h2 class="mb-4">Empleados Registrados</h2>
        <a href="registroEmpleado.jsp" class="btn btn-primary mb-3">Agregar Nuevo Empleado</a>
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>DUI</th>
                    <th>Teléfono</th>
                    <th>Dirección</th>
                    <th>Correo</th>
                    <th>Fecha Ingreso</th>
                    <th>Cargo</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Empleado> lista = (List<Empleado>) request.getAttribute("lista");
                    if (lista != null) {
                        for (Empleado emp : lista) {
                %>
                <tr>
                    <td><%= emp.getId() %></td>
                    <td><%= emp.getNombreEmpleado() %></td>
                    <td><%= emp.getApellidoEmpleado() %></td>
                    <td><%= emp.getDui() %></td>
                    <td><%= emp.getTelefono() %></td>
                    <td><%= emp.getDireccion() %></td>
                    <td><%= emp.getCorreo() %></td>
                    <td><%= emp.getFechaIngreso() %></td>
                    <td><%= emp.getCargo() %></td>
                    <td>
                        <a href="EmpleadoServlet?accion=editar&id=<%= emp.getId() %>" class="btn btn-warning btn-sm">Editar</a>
                        <a href="EmpleadoServlet?accion=eliminar&id=<%= emp.getId() %>" class="btn btn-danger btn-sm" onclick="return confirm('¿Deseas eliminar este empleado?')">Eliminar</a>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="10" class="text-center">No hay empleados registrados.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
<%-- 
    Document   : clientes
    Created on : Jun 9, 2025, 3:29:59 PM
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Cliente"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>
    <link rel="stylesheet"href="css/bootstrap.min.css">
    <link rel="stylesheet"href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-4">
        <h2 class="mb-4">Clientes Registrados</h2>
        <a href="registroCliente.jsp" class="btn btn-primary mb-3">Agregar Nuevo Cliente</a>
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
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
                    List<Cliente> lista = (List<Cliente>) request.getAttribute("lista");
                    if (lista != null && !lista.isEmpty()) {
                        for (Cliente cli : lista) {
                %>
                <tr>
                    <td><%= cli.getId_cliente() %></td>
                    <td><%= cli.getNombres_cliente() %></td>
                    <td><%= cli.getApellidos_cliente() %></td>
                    <td><%= cli.getDui() %></td>
                    <td><%= cli.getTelefono_cliente() %></td>
                    <td><%= cli.getCorreo_cliente() %></td>
                    <td><%= cli.getFecha_inserccion_cliente() %></td>
                    <td>
                        <a href="ClienteServlet?accion=editar&id=<%= cli.getId_cliente() %>" 
                           class="btn btn-warning btn-sm">Editar</a>
                        <a href="ClienteServlet?accion=eliminar&id=<%= cli.getId_cliente() %>" 
                           class="btn btn-danger btn-sm" 
                           onclick="return confirm('¿Deseas eliminar este cliente?')">Eliminar</a>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="8" class="text-center">No hay clientes registrados.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
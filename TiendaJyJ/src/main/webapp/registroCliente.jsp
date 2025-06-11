<%-- 
    Document   : resgistroClientes
    Created on : Jun 9, 2025, 3:16:44 PM
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Cliente cli = (Cliente) request.getAttribute("Cliente");
    boolean editar = (cli != null);
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

<title><%= editar ? "Editar" : "Registrar" %> Cliente</title>
    
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-5">
        <h2 class="mb-4"><%= editar ? "Editar" : "Registrar Nuevo" %> Cliente</h2>
        <form action="ClienteServlet" method="post">
            <% if (editar) { %>
                <input type="hidden" name="id_cliente" value="<%= cli.getId_cliente() %>">
                <input type="hidden" name="accion" value="actualizar">
            <% } else { %>
                <input type="hidden" name="accion" value="insertar">
            <% } %>

            <div class="mb-3">
                <label class="form-label">Nombres</label>
                <input type="text" name="nombres_cliente" class="form-control" required 
                       value="<%= editar ? cli.getNombres_cliente() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Apellidos</label>
                <input type="text" name="apellidos_cliente" class="form-control" required 
                       value="<%= editar ? cli.getApellidos_cliente() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">DUI</label>
                <input type="text" name="dui" class="form-control" required 
                       value="<%= editar ? cli.getDui() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono_cliente" class="form-control" 
                       value="<%= editar ? cli.getTelefono_cliente() : "" %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Correo Electrónico</label>
                <input type="email" name="correo_cliente" class="form-control" required 
                       value="<%= editar ? cli.getCorreo_cliente() : "" %>">
            </div>

            <button type="submit" class="btn btn-success"><%= editar ? "Actualizar" : "Registrar" %></button>
            <a href="ClienteServlet?accion=listar" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>
</body>
</html>

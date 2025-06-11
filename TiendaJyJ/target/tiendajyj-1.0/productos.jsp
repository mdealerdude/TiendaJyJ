<%-- 
    Document   : productos
    Created on : Jun 11, 2025
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Producto"%>
<%@page import="tiendajyj.dao.ProductoDao.Marca"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Productos</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-4">
        <h2 class="mb-4">Productos Registrados</h2>
        
        <%-- Mostrar mensajes si existen --%>
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");
            if (mensaje != null) {
                session.removeAttribute("mensaje");
                session.removeAttribute("tipoMensaje");
        %>
            <div class="alert alert-<%= "success".equals(tipoMensaje) ? "success" : "danger" %> alert-dismissible fade show" role="alert">
                <%= mensaje %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
            }
        %>
        
        <div class="row mb-3">
            <div class="col-md-6">
                <a href="ProductoServlet?accion=nuevo" class="btn btn-primary mb-3">
                    <i class="fas fa-plus"></i> Agregar Nuevo Producto
                </a>
            </div>
            <div class="col-md-6">
                <!-- Formulario de búsqueda -->
                <form method="get" action="ProductoServlet" class="d-flex">
                    <input type="hidden" name="accion" value="buscar">
                    <input type="text" name="termino" class="form-control me-2" 
                           placeholder="Buscar producto o marca..." 
                           value="<%= request.getAttribute("terminoBusqueda") != null ? request.getAttribute("terminoBusqueda") : "" %>">
                    <button type="submit" class="btn btn-outline-secondary">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
            </div>
        </div>
        
        <%-- Filtro por marca --%>
        <%
            List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
            if (marcas != null && !marcas.isEmpty()) {
        %>
        <div class="mb-3">
            <label class="form-label">Filtrar por marca:</label>
            <select class="form-select" style="width: auto; display: inline-block;" 
                    onchange="filtrarPorMarca(this.value)">
                <option value="">Todas las marcas</option>
                <%
                    Integer marcaSeleccionada = (Integer) request.getAttribute("marcaSeleccionada");
                    for (Marca marca : marcas) {
                %>
                <option value="<%= marca.getIdMarca() %>" 
                        <%= (marcaSeleccionada != null && marcaSeleccionada == marca.getIdMarca()) ? "selected" : "" %>>
                    <%= marca.getNombreMarca() %>
                </option>
                <%
                    }
                %>
            </select>
        </div>
        <%
            }
        %>
        
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre Producto</th>
                        <th>Marca</th>
                        <th>Stock</th>
                        <th>Precio</th>
                        <th>Fecha Registro</th>
                        <th>Última Actualización</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Producto> lista = (List<Producto>) request.getAttribute("lista");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "SV"));
                        
                        if (lista != null && !lista.isEmpty()) {
                            for (Producto prod : lista) {
                    %>
                    <tr>
                        <td><%= prod.getIdProducto() %></td>
                        <td><%= prod.getNombreProducto() %></td>
                        <td><%= prod.getNombreMarca() != null ? prod.getNombreMarca() : "N/A" %></td>
                        <td>
                            <span class="badge <%= prod.getStockProducto() <= 10 ? "bg-danger" : 
                                                   prod.getStockProducto() <= 50 ? "bg-warning" : "bg-success" %>">
                                <%= prod.getStockProducto() %>
                            </span>
                        </td>
                        <td><%= formatoMoneda.format(prod.getPrecioProducto()) %></td>
                        <td><%= prod.getFechaInserccionProducto() != null ? sdf.format(prod.getFechaInserccionProducto()) : "N/A" %></td>
                        <td><%= prod.getFechaActualizacionProducto() != null ? sdf.format(prod.getFechaActualizacionProducto()) : "N/A" %></td>
                        <td>
                            <a href="ProductoServlet?accion=editar&id=<%= prod.getIdProducto() %>" 
                               class="btn btn-warning btn-sm" title="Editar">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="ProductoServlet?accion=eliminar&id=<%= prod.getIdProducto() %>" 
                               class="btn btn-danger btn-sm" 
                               onclick="return confirm('¿Deseas eliminar este producto?')" 
                               title="Eliminar">
                                <i class="fas fa-trash"></i>
                            </a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="8" class="text-center">
                            <% if (request.getAttribute("terminoBusqueda") != null) { %>
                                No se encontraron productos con el término "<%= request.getAttribute("terminoBusqueda") %>"
                            <% } else { %>
                                No hay productos registrados.
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <%-- Mostrar total de productos --%>
        <%
            if (lista != null && !lista.isEmpty()) {
        %>
        <div class="mt-3">
            <small class="text-muted">Total de productos: <%= lista.size() %></small>
        </div>
        <%
            }
        %>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
    <script>
        function filtrarPorMarca(idMarca) {
            if (idMarca === '') {
                window.location.href = 'ProductoServlet?accion=listar';
            } else {
                window.location.href = 'ProductoServlet?accion=porMarca&idMarca=' + idMarca;
            }
        }
    </script>
</body>
</html>

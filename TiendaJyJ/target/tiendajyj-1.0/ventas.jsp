<%-- 
    Document   : ventas
    Created on : Jun 10, 2025
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Venta"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Ventas</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-4">
        <h2 class="mb-4"><i class="fas fa-shopping-cart me-2"></i>Ventas Registradas</h2>
        <a href="VentaServlet?accion=nuevo" class="btn btn-primary mb-3">
            <i class="fas fa-plus me-1"></i>Nueva Venta
        </a>
        
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="table-dark">
                    <tr>
                        <th>ID Venta</th>
                        <th>Cliente</th>
                        <th>Producto</th>
                        <th>Precio Unitario</th>
                        <th>Cantidad</th>
                        <th>Total</th>
                        <th>Vendedor</th>
                        <th>Fecha Venta</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Venta> lista = (List<Venta>) request.getAttribute("lista");
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "SV"));
                        
                        if (lista != null && !lista.isEmpty()) {
                            for (Venta venta : lista) {
                    %>
                    <tr>
                        <td><%= venta.getId_venta() %></td>
                        <td><%= venta.getNombre_cliente() %></td>
                        <td><%= venta.getNombre_producto() %></td>
                        <td><%= formatoMoneda.format(venta.getPrecio_unitario()) %></td>
                        <td><%= venta.getCantidad_producto() %></td>
                        <td class="fw-bold text-success"><%= formatoMoneda.format(venta.getTotal()) %></td>
                        <td><%= venta.getNombre_usuario() %></td>
                        <td><%= formatoFecha.format(venta.getFecha_inserccion_venta()) %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="VentaServlet?accion=editar&id=<%= venta.getId_venta() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                   <i class="fas fa-edit"></i>
                                </a>
                                <a href="VentaServlet?accion=eliminar&id=<%= venta.getId_venta() %>" 
                                   class="btn btn-danger btn-sm" 
                                   onclick="return confirm('¿Deseas eliminar esta venta?')" title="Eliminar">
                                   <i class="fas fa-trash"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="9" class="text-center">
                            <div class="alert alert-info mb-0">
                                <i class="fas fa-info-circle me-2"></i>No hay ventas registradas.
                            </div>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <%-- Resumen de ventas --%>
        <% if (lista != null && !lista.isEmpty()) { 
            double totalGeneral = 0;
            int totalVentas = lista.size();
            for (Venta v : lista) {
                totalGeneral += v.getTotal().doubleValue();
            }
        %>
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="card bg-primary text-white">
                    <div class="card-body">
                        <h5 class="card-title"><i class="fas fa-chart-line me-2"></i>Total de Ventas</h5>
                        <h3><%= totalVentas %></h3>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card bg-success text-white">
                    <div class="card-body">
                        <h5 class="card-title"><i class="fas fa-dollar-sign me-2"></i>Total General</h5>
                        <h3><%= formatoMoneda.format(totalGeneral) %></h3>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
    
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
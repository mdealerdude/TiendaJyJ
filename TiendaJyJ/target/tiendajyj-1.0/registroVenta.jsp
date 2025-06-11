<%-- 
    Document   : registroVenta
    Created on : Jun 10, 2025
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Venta"%>
<%@page import="tiendajyj.model.Cliente"%>
<%@page import="tiendajyj.model.Producto"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de Venta</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    
    <%
        Venta venta = (Venta) request.getAttribute("Venta");
        List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
        List<Producto> productos = (List<Producto>) request.getAttribute("productos");
        boolean esEdicion = (venta != null);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "SV"));
    %>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="fas fa-<%= esEdicion ? "edit" : "plus" %> me-2"></i>
                            <%= esEdicion ? "Editar Venta" : "Nueva Venta" %>
                        </h4>
                    </div>
                    <div class="card-body">
                        <form action="VentaServlet" method="post" id="formVenta">
                            <input type="hidden" name="accion" value="<%= esEdicion ? "actualizar" : "insertar" %>">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="id_venta" value="<%= venta.getId_venta() %>">
                            <% } %>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="id_cliente" class="form-label">
                                            <i class="fas fa-user me-1"></i>Cliente *
                                        </label>
                                        <select class="form-select" id="id_cliente" name="id_cliente" required>
                                            <option value="">Seleccione un cliente</option>
                                            <%
                                                if (clientes != null) {
                                                    for (Cliente cliente : clientes) {
                                                        boolean selected = esEdicion && venta.getId_cliente() == cliente.getId_cliente();
                                            %>
                                                <option value="<%= cliente.getId_cliente() %>" <%= selected ? "selected" : "" %>>
                                                    <%= cliente.getNombres_cliente() %>
                                                </option>
                                            <% 
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="id_producto" class="form-label">
                                            <i class="fas fa-box me-1"></i>Producto *
                                        </label>
                                        <select class="form-select" id="id_producto" name="id_producto" required onchange="actualizarPrecio()">
                                            <option value="">Seleccione un producto</option>
                                            <%
                                                if (productos != null) {
                                                    for (Producto producto : productos) {
                                                        boolean selected = esEdicion && venta.getId_producto() == producto.getId_producto();
                                            %>
                                                <option value="<%= producto.getId_producto() %>" 
                                                        data-precio="<%= producto.getPrecio_producto() %>"
                                                        data-stock="<%= producto.getStock_producto() %>"
                                                        <%= selected ? "selected" : "" %>>
                                                    <%= producto.getNombre_producto() %> - 
                                                    <%= producto.getNombre_marca() %> 
                                                    (Stock: <%= producto.getStock_producto() %>)
                                                </option>
                                            <% 
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="precio_unitario" class="form-label">
                                            <i class="fas fa-dollar-sign me-1"></i>Precio Unitario
                                        </label>
                                        <input type="text" class="form-control" id="precio_unitario" 
                                               value="<%= esEdicion ? formatoMoneda.format(venta.getPrecio_unitario()) : "" %>" 
                                               readonly>
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="cantidad_producto" class="form-label">
                                            <i class="fas fa-sort-numeric-up me-1"></i>Cantidad *
                                        </label>
                                        <input type="number" class="form-control" id="cantidad_producto" 
                                               name="cantidad_producto" min="1" required 
                                               value="<%= esEdicion ? venta.getCantidad_producto() : "" %>"
                                               onchange="calcularTotal()">
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="total" class="form-label">
                                            <i class="fas fa-calculator me-1"></i>Total
                                        </label>
                                        <input type="text" class="form-control fw-bold text-success" 
                                               id="total" 
                                               value="<%= esEdicion ? formatoMoneda.format(venta.getTotal()) : "" %>" 
                                               readonly>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-12">
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle me-2"></i>
                                        <strong>Información:</strong> El stock del producto será actualizado automáticamente al realizar la venta.
                                    </div>
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="VentaServlet?accion=listar" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-arrow-left me-1"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-1"></i>
                                    <%= esEdicion ? "Actualizar Venta" : "Registrar Venta" %>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="js/bootstrap.bundle.min.js"></script>
    <script>
        // Formatear números como moneda
        function formatearMoneda(valor) {
            return new Intl.NumberFormat('es-SV', {
                style: 'currency',
                currency: 'USD'
            }).format(valor);
        }
        
        // Actualizar precio cuando cambia el producto
        function actualizarPrecio() {
            const selectProducto = document.getElementById('id_producto');
            const inputPrecio = document.getElementById('precio_unitario');
            const inputCantidad = document.getElementById('cantidad_producto');
            
            if (selectProducto.value) {
                const opcionSeleccionada = selectProducto.options[selectProducto.selectedIndex];
                const precio = parseFloat(opcionSeleccionada.getAttribute('data-precio'));
                const stock = parseInt(opcionSeleccionada.getAttribute('data-stock'));
                
                inputPrecio.value = formatearMoneda(precio);
                inputCantidad.max = stock;
                
                // Si ya hay cantidad, calcular total
                if (inputCantidad.value) {
                    calcularTotal();
                }
            } else {
                inputPrecio.value = '';
                inputCantidad.max = '';
                document.getElementById('total').value = '';
            }
        }
        
        // Calcular total
        function calcularTotal() {
            const selectProducto = document.getElementById('id_producto');
            const inputCantidad = document.getElementById('cantidad_producto');
            const inputTotal = document.getElementById('total');
            
            if (selectProducto.value && inputCantidad.value) {
                const opcionSeleccionada = selectProducto.options[selectProducto.selectedIndex];
                const precio = parseFloat(opcionSeleccionada.getAttribute('data-precio'));
                const cantidad = parseInt(inputCantidad.value);
                const stock = parseInt(opcionSeleccionada.getAttribute('data-stock'));
                
                // Validar que no exceda el stock
                if (cantidad > stock) {
                    alert('La cantidad no puede exceder el stock disponible (' + stock + ')');
                    inputCantidad.value = stock;
                    return;
                }
                
                const total = precio * cantidad;
                inputTotal.value = formatearMoneda(total);
            } else {
                inputTotal.value = '';
            }
        }
        
        // Validaciones del formulario
        document.getElementById('formVenta').addEventListener('submit', function(e) {
            const selectProducto = document.getElementById('id_producto');
            const inputCantidad = document.getElementById('cantidad_producto');
            
            if (selectProducto.value && inputCantidad.value) {
                const opcionSeleccionada = selectProducto.options[selectProducto.selectedIndex];
                const stock = parseInt(opcionSeleccionada.getAttribute('data-stock'));
                const cantidad = parseInt(inputCantidad.value);
                
                if (cantidad > stock) {
                    e.preventDefault();
                    alert('La cantidad no puede exceder el stock disponible (' + stock + ')');
                    return false;
                }
                
                if (cantidad <= 0) {
                    e.preventDefault();
                    alert('La cantidad debe ser mayor a 0');
                    return false;
                }
            }
        });
        
        // Inicializar si es edición
        <% if (esEdicion) { %>
            actualizarPrecio();
        <% } %>
    </script>
</body>
</html>
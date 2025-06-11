<%-- 
    Document   : registroProducto
    Created on : Jun 11, 2025
    Author     : MINEDUCYT
--%>

<%@page import="tiendajyj.model.Producto"%>
<%@page import="tiendajyj.dao.ProductoDao.Marca"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Producto producto = (Producto) request.getAttribute("Producto");
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
    boolean editar = (producto != null);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title><%= editar ? "Editar" : "Registrar" %> Producto</title>
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h3 class="card-title mb-0">
                            <i class="fas fa-<%= editar ? "edit" : "plus" %>"></i>
                            <%= editar ? "Editar" : "Registrar Nuevo" %> Producto
                        </h3>
                    </div>
                    <div class="card-body">
                        <%-- Mostrar información del producto si está editando --%>
                        <% if (editar) { %>
                        <div class="alert alert-info">
                            <strong>Información del producto:</strong><br>
                            ID: <%= producto.getIdProducto() %><br>
                            Fecha de registro: <%= producto.getFechaInserccionProducto() != null ? sdf.format(producto.getFechaInserccionProducto()) : "N/A" %><br>
                            Última actualización: <%= producto.getFechaActualizacionProducto() != null ? sdf.format(producto.getFechaActualizacionProducto()) : "N/A" %>
                        </div>
                        <% } %>

                        <form action="ProductoServlet" method="post" id="formProducto">
                            <% if (editar) { %>
                                <input type="hidden" name="id_producto" value="<%= producto.getIdProducto() %>">
                                <input type="hidden" name="accion" value="actualizar">
                            <% } else { %>
                                <input type="hidden" name="accion" value="insertar">
                            <% } %>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label class="form-label">
                                            <i class="fas fa-box"></i> Nombre del Producto *
                                        </label>
                                        <input type="text" name="nombre_producto" class="form-control" 
                                               required maxlength="100"
                                               value="<%= editar ? producto.getNombreProducto() : "" %>"
                                               placeholder="Ingrese el nombre del producto">
                                        <div class="form-text">Máximo 100 caracteres</div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label class="form-label">
                                            <i class="fas fa-tag"></i> Marca *
                                        </label>
                                        <select name="id_marca" class="form-select" required>
                                            <option value="">Seleccione una marca</option>
                                            <%
                                                if (marcas != null && !marcas.isEmpty()) {
                                                    for (Marca marca : marcas) {
                                                        boolean seleccionado = editar && producto.getIdMarca() == marca.getIdMarca();
                                            %>
                                            <option value="<%= marca.getIdMarca() %>" <%= seleccionado ? "selected" : "" %>>
                                                <%= marca.getNombreMarca() %>
                                            </option>
                                            <%
                                                    }
                                                } else {
                                            %>
                                            <option value="" disabled>No hay marcas disponibles</option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label class="form-label">
                                            <i class="fas fa-warehouse"></i> Stock *
                                        </label>
                                        <input type="number" name="stock_producto" class="form-control" 
                                               required min="0" max="99999"
                                               value="<%= editar ? producto.getStockProducto() : "" %>"
                                               placeholder="0">
                                        <div class="form-text">Cantidad disponible en inventario</div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label class="form-label">
                                            <i class="fas fa-dollar-sign"></i> Precio *
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">$</span>
                                            <input type="number" name="precio_producto" class="form-control" 
                                                   required min="0" step="0.01"
                                                   value="<%= editar ? producto.getPrecioProducto() : "" %>"
                                                   placeholder="0.00">
                                        </div>
                                        <div class="form-text">Precio de venta del producto</div>
                                    </div>
                                </div>
                            </div>

                            <%-- Alertas de stock bajo --%>
                            <div class="mb-3">
                                <div class="alert alert-warning">
                                    <small>
                                        <i class="fas fa-info-circle"></i>
                                        <strong>Niveles de stock:</strong>
                                        Stock crítico (≤10), Stock bajo (≤50), Stock normal (>50)
                                    </small>
                                </div>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="ProductoServlet?accion=listar" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-<%= editar ? "warning" : "success" %>" id="btnSubmit">
                                    <i class="fas fa-<%= editar ? "save" : "plus" %>"></i>
                                    <%= editar ? "Actualizar" : "Registrar" %> Producto
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
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('formProducto');
            const btnSubmit = document.getElementById('btnSubmit');
            
            // Validación en tiempo real
            form.addEventListener('input', function(e) {
                validarFormulario();
            });
            
            // Validación antes del envío
            form.addEventListener('submit', function(e) {
                if (!validarFormulario()) {
                    e.preventDefault();
                    return false;
                }
                
                // Deshabilitar botón para evitar doble envío
                btnSubmit.disabled = true;
                btnSubmit.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
            });
            
            function validarFormulario() {
                let valido = true;
                
                // Validar nombre del producto
                const nombre = document.querySelector('input[name="nombre_producto"]');
                if (!nombre.value.trim()) {
                    nombre.classList.add('is-invalid');
                    valido = false;
                } else {
                    nombre.classList.remove('is-invalid');
                    nombre.classList.add('is-valid');
                }
                
                // Validar marca
                const marca = document.querySelector('select[name="id_marca"]');
                if (!marca.value) {
                    marca.classList.add('is-invalid');
                    valido = false;
                } else {
                    marca.classList.remove('is-invalid');
                    marca.classList.add('is-valid');
                }
                
                // Validar stock
                const stock = document.querySelector('input[name="stock_producto"]');
                if (!stock.value || parseInt(stock.value) < 0) {
                    stock.classList.add('is-invalid');
                    valido = false;
                } else {
                    stock.classList.remove('is-invalid');
                    stock.classList.add('is-valid');
                }
                
                // Validar precio
                const precio = document.querySelector('input[name="precio_producto"]');
                if (!precio.value || parseFloat(precio.value) <= 0) {
                    precio.classList.add('is-invalid');
                    valido = false;
                } else {
                    precio.classList.remove('is-invalid');
                    precio.classList.add('is-valid');
                }
                
                return valido;
            }
            
            // Formatear precio mientras se escribe
            const precioInput = document.querySelector('input[name="precio_producto"]');
            precioInput.addEventListener('blur', function() {
                if (this.value) {
                    this.value = parseFloat(this.value).toFixed(2);
                }
            });
        });

        // Función para mostrar alerta de stock bajo
        function mostrarAlertaStock() {
            const stockInput = document.querySelector('input[name="stock_producto"]');
            const stock = parseInt(stockInput.value);
            
            if (stock <= 10 && stock > 0) {
                alert('⚠️ Advertencia: El stock ingresado es crítico (≤10 unidades)');
            } else if (stock <= 50 && stock > 10) {
                alert('⚠️ Atención: El stock ingresado es bajo (≤50 unidades)');
            }
        }
        
        // Agregar evento al campo de stock
        document.querySelector('input[name="stock_producto"]').addEventListener('blur', mostrarAlertaStock);
    </script>
</body>
</html>
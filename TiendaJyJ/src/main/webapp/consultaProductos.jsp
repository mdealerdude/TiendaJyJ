<%-- 
    Document   : consultaProductos
    Created on : Jun 11, 2025
    Author     : MINEDUCYT
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row col-md-12 mt-4 mx-auto">
    <table class="table table-striped table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>Nombre Producto</th>
                <th>Marca</th>
                <th>Stock</th>
                <th>Precio</th>
                <th>Fecha de Registro</th>
                <th>Última Actualización</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
                Conexion conexionDB = null;
                Connection con = null;
                PreparedStatement st = null;
                ResultSet rs = null;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "SV"));

                try {
                    conexionDB = new Conexion();
                    con = conexionDB.getConnection();
                    
                    // Query con JOIN para obtener información de marca
                    String sql = "SELECT p.*, m.nombre_marca " +
                                "FROM productos p " +
                                "INNER JOIN marca m ON p.id_marca = m.id_marca " +
                                "ORDER BY p.fecha_inserccion_producto DESC";
                    
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();

                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("nombre_producto") %></td>
                <td><%= rs.getString("nombre_marca") %></td>
                <td>
                    <%
                        int stock = rs.getInt("stock_producto");
                        String badgeClass = stock <= 10 ? "bg-danger" : 
                                          stock <= 50 ? "bg-warning" : "bg-success";
                    %>
                    <span class="badge <%= badgeClass %>"><%= stock %></span>
                </td>
                <td><%= formatoMoneda.format(rs.getBigDecimal("precio_producto")) %></td>
                <td>
                    <%
                        java.sql.Timestamp fechaInsercion = rs.getTimestamp("fecha_inserccion_producto");
                        if (fechaInsercion != null) {
                    %>
                        <%= sdf.format(fechaInsercion) %>
                    <%
                        } else {
                    %>
                        N/A
                    <%
                        }
                    %>
                </td>
                <td>
                    <%
                        java.sql.Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion_producto");
                        if (fechaActualizacion != null) {
                    %>
                        <%= sdf.format(fechaActualizacion) %>
                    <%
                        } else {
                    %>
                        N/A
                    <%
                        }
                    %>
                </td>
                <td>
                    <a class="btn btn-warning btn-sm" 
                       href="ProductoServlet?accion=editar&id=<%= rs.getInt("id_producto") %>"
                       title="Editar producto">
                        <i class="fas fa-edit"></i> Editar
                    </a>
                    <a class="btn btn-danger btn-sm" 
                       href="ProductoServlet?accion=eliminar&id=<%= rs.getInt("id_producto") %>"
                       onclick="return confirm('¿Estás seguro de eliminar este producto?')"
                       title="Eliminar producto">
                        <i class="fas fa-trash"></i> Eliminar
                    </a>
                    <button class="btn btn-info btn-sm" 
                            onclick="verificarStock(<%= rs.getInt("id_producto") %>, '<%= rs.getString("nombre_producto") %>')"
                            title="Verificar stock">
                        <i class="fas fa-box"></i> Stock
                    </button>
                </td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='7' class='text-danger'>Error al cargar datos: " + e.getMessage() + "</td></tr>");
                    e.printStackTrace();
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                    if (st != null) try { st.close(); } catch (SQLException ignore) {}
                    if (conexionDB != null) try { conexionDB.cerrarConexion(); } catch (SQLException ignore) {}
                }
            %>
        </tbody>
    </table>
</div>

<!-- Modal para verificar stock -->
<div class="modal fade" id="stockModal" tabindex="-1" aria-labelledby="stockModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="stockModalLabel">Verificar Stock</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Producto: <span id="nombreProducto"></span></p>
                <div class="mb-3">
                    <label for="cantidadRequerida" class="form-label">Cantidad requerida:</label>
                    <input type="number" class="form-control" id="cantidadRequerida" min="1" value="1">
                </div>
                <div id="resultadoStock"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                <button type="button" class="btn btn-primary" onclick="consultarStock()">Verificar</button>
            </div>
        </div>
    </div>
</div>

<script>
let productoIdActual = 0;

function verificarStock(idProducto, nombreProducto) {
    productoIdActual = idProducto;
    document.getElementById('nombreProducto').textContent = nombreProducto;
    document.getElementById('cantidadRequerida').value = 1;
    document.getElementById('resultadoStock').innerHTML = '';
    
    var stockModal = new bootstrap.Modal(document.getElementById('stockModal'));
    stockModal.show();
}

function consultarStock() {
    const cantidad = document.getElementById('cantidadRequerida').value;
    
    if (!cantidad || cantidad <= 0) {
        document.getElementById('resultadoStock').innerHTML = 
            '<div class="alert alert-warning">Por favor ingrese una cantidad válida</div>';
        return;
    }
    
    fetch(`ProductoServlet?accion=verificarStock&idProducto=${productoIdActual}&cantidad=${cantidad}`)
        .then(response => response.json())
        .then(data => {
            const resultadoDiv = document.getElementById('resultadoStock');
            if (data.stockDisponible) {
                resultadoDiv.innerHTML = 
                    '<div class="alert alert-success"><i class="fas fa-check-circle"></i> Stock disponible</div>';
            } else {
                resultadoDiv.innerHTML = 
                    '<div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> Stock insuficiente</div>';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('resultadoStock').innerHTML = 
                '<div class="alert alert-danger">Error al verificar el stock</div>';
        });
}
</script>
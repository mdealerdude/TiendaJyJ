<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("USER") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    // Obtener la lista de usuarios del request
    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("lista");
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios - Tienda JyJ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .table th {
            background-color: #343a40;
            color: white;
            border: none;
            text-align: center;
            vertical-align: middle;
        }
        .table td {
            vertical-align: middle;
            text-align: center;
        }
        .btn-group .btn {
            margin: 0 2px;
        }
        .badge {
            font-size: 0.85em;
        }
        .table-hover tbody tr:hover {
            background-color: #f8f9fa;
        }
        .search-container {
            background: white;
            padding: 1.5rem;
            border-radius: 0.5rem;
            margin-bottom: 1.5rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .stats-cards {
            margin-bottom: 1.5rem;
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 0.5rem;
            padding: 1.5rem;
            text-align: center;
        }
        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }
    </style>
</head>
<body>
    <jsp:include page="menuppl.jsp" />
    
    <div class="container-fluid mt-4">
        <!-- Título y botón agregar -->
        <div class="row mb-4">
            <div class="col-md-8">
                <h2 class="text-primary">
                    <i class="fas fa-users"></i> Gestión de Usuarios
                </h2>
                <p class="text-muted">Administra los usuarios del sistema</p>
            </div>
            <div class="col-md-4 text-end">
                <a href="UsuarioServlet?accion=nuevo" class="btn btn-success btn-lg">
                    <i class="fas fa-user-plus"></i> Nuevo Usuario
                </a>
            </div>
        </div>

        <!-- Estadísticas -->
        <div class="row stats-cards">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-number">
                        <%= listaUsuarios != null ? listaUsuarios.size() : 0 %>
                    </div>
                    <div>Total Usuarios</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                    <div class="stat-number" id="adminCount">0</div>
                    <div>Administradores</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                    <div class="stat-number" id="userCount">0</div>
                    <div>Usuarios Regulares</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                    <div class="stat-number" id="recentCount">0</div>
                    <div>Registros Hoy</div>
                </div>
            </div>
        </div>

        <!-- Mensajes -->
        <% if (mensaje != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i>
                <%= mensaje %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        
        <% if (error != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i>
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <!-- Filtros y búsqueda -->
        <div class="search-container">
            <div class="row">
                <div class="col-md-4">
                    <label class="form-label">
                        <i class="fas fa-search"></i> Buscar Usuario
                    </label>
                    <input type="text" id="searchInput" class="form-control" 
                           placeholder="Buscar por nombre, username o correo...">
                </div>
                <div class="col-md-3">
                    <label class="form-label">
                        <i class="fas fa-filter"></i> Filtrar por Nivel
                    </label>
                    <select id="levelFilter" class="form-select">
                        <option value="">Todos los niveles</option>
                        <option value="1">Administrador</option>
                        <option value="2">Usuario</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">
                        <i class="fas fa-sort"></i> Ordenar por
                    </label>
                    <select id="sortBy" class="form-select">
                        <option value="fecha_desc">Más recientes</option>
                        <option value="fecha_asc">Más antiguos</option>
                        <option value="nombre_asc">Nombre A-Z</option>
                        <option value="nombre_desc">Nombre Z-A</option>
                        <option value="username_asc">Username A-Z</option>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="button" class="btn btn-outline-secondary w-100" onclick="clearFilters()">
                        <i class="fas fa-times"></i> Limpiar
                    </button>
                </div>
            </div>
        </div>

        <!-- Tabla de usuarios -->
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">
                    <i class="fas fa-list"></i> Lista de Usuarios
                    <span class="badge bg-light text-dark ms-2" id="resultCount">
                        <%= listaUsuarios != null ? listaUsuarios.size() : 0 %> usuarios
                    </span>
                </h5>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0" id="usuariosTable">
                        <thead class="table-dark">
                            <tr>
                                <th>#</th>
                                <th>Username</th>
                                <th>Nombres</th>
                                <th>Apellidos</th>
                                <th>Correo</th>
                                <th>Teléfono</th>
                                <th>Nivel Usuario</th>
                                <th>Fecha Registro</th>
                                <th>Última Actualización</th>
                                <th width="200">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                                    // Obtener información adicional de la base de datos para las fechas
                                    Conexion conexionDB = null;
                                    Connection con = null;
                                    PreparedStatement st = null;
                                    ResultSet rs = null;
                                    try {
                                        conexionDB = new Conexion();
                                        con = conexionDB.getConnection();
                                        
                                        for (int i = 0; i < listaUsuarios.size(); i++) {
                                            Usuario usuario = listaUsuarios.get(i);
                                            
                                            // Obtener fechas y nivel de usuario
                                            String queryFechas = "SELECT u.fecha_creacion_usuario, u.fecha_actualizacion_usuario, nu.nombre_nivel " +
                                                               "FROM usuarios u " +
                                                               "LEFT JOIN nivel_usuario nu ON u.id_nivel_usuario = nu.id_nivel_usuario " +
                                                               "WHERE u.id_usuario = ?";
                                            st = con.prepareStatement(queryFechas);
                                            st.setInt(1, usuario.getIdUsuario());
                                            rs = st.executeQuery();
                                            
                                            String fechaCreacion = "N/A";
                                            String fechaActualizacion = "Sin actualizar";
                                            String nombreNivel = "Sin nivel";
                                            
                                            if (rs.next()) {
                                                java.sql.Timestamp fc = rs.getTimestamp("fecha_creacion_usuario");
                                                java.sql.Timestamp fa = rs.getTimestamp("fecha_actualizacion_usuario");
                                                String nl = rs.getString("nombre_nivel");
                                                
                                                if (fc != null) fechaCreacion = fc.toString();
                                                if (fa != null) fechaActualizacion = fa.toString();
                                                if (nl != null) nombreNivel = nl;
                                            }
                                            
                                            rs.close();
                                            st.close();
                            %>
                            <tr data-username="<%= usuario.getUsername().toLowerCase() %>" 
                                data-nombres="<%= usuario.getNombresUsuario().toLowerCase() %>"
                                data-apellidos="<%= usuario.getApellidosUsuario().toLowerCase() %>"
                                data-correo="<%= usuario.getCorreoUsuario().toLowerCase() %>"
                                data-nivel="<%= usuario.getIdNivelUsuario() %>"
                                data-fecha="<%= fechaCreacion %>">
                                <td><%= i + 1 %></td>
                                <td>
                                    <strong><%= usuario.getUsername() %></strong>
                                </td>
                                <td><%= usuario.getNombresUsuario() %></td>
                                <td><%= usuario.getApellidosUsuario() %></td>
                                <td>
                                    <a href="mailto:<%= usuario.getCorreoUsuario() %>" class="text-decoration-none">
                                        <i class="fas fa-envelope"></i> <%= usuario.getCorreoUsuario() %>
                                    </a>
                                </td>
                                <td>
                                    <i class="fas fa-phone"></i> <%= usuario.getTelefonoUsuario() %>
                                </td>
                                <td>
                                    <% if (usuario.getIdNivelUsuario() == 1) { %>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-crown"></i> <%= nombreNivel %>
                                        </span>
                                    <% } else if (usuario.getIdNivelUsuario() == 2) { %>
                                        <span class="badge bg-primary">
                                            <i class="fas fa-user"></i> <%= nombreNivel %>
                                        </span>
                                    <% } else { %>
                                        <span class="badge bg-secondary">
                                            <i class="fas fa-question"></i> <%= nombreNivel %>
                                        </span>
                                    <% } %>
                                </td>
                                <td>
                                    <small class="text-muted">
                                        <i class="fas fa-calendar-plus"></i><br>
                                        <%= fechaCreacion %>
                                    </small>
                                </td>
                                <td>
                                    <small class="text-muted">
                                        <i class="fas fa-calendar-edit"></i><br>
                                        <%= fechaActualizacion %>
                                    </small>
                                </td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <button type="button" class="btn btn-info btn-sm" 
                                                data-bs-toggle="modal" 
                                                data-bs-target="#verUsuarioModal"
                                                onclick="verUsuario(<%= usuario.getIdUsuario() %>, '<%= usuario.getUsername() %>', '<%= usuario.getNombresUsuario() %>', '<%= usuario.getApellidosUsuario() %>', '<%= usuario.getCorreoUsuario() %>', '<%= usuario.getTelefonoUsuario() %>', '<%= nombreNivel %>', '<%= fechaCreacion %>', '<%= fechaActualizacion %>')"
                                                title="Ver detalles">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                        <a class="btn btn-warning btn-sm" 
                                           href="UsuarioServlet?accion=editar&id=<%= usuario.getIdUsuario() %>"
                                           title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <% 
                                        // No permitir eliminar al usuario actual
                                        Integer usuarioActualId = (Integer) session.getAttribute("id_usuario");
                                        if (usuarioActualId == null || usuarioActualId != usuario.getIdUsuario()) {
                                        %>
                                        <button type="button" class="btn btn-danger btn-sm" 
                                                onclick="eliminarUsuario(<%= usuario.getIdUsuario() %>, '<%= usuario.getUsername() %>')"
                                                title="Eliminar">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                        <% } else { %>
                                        <button type="button" class="btn btn-secondary btn-sm" 
                                                disabled title="No puedes eliminar tu propio usuario">
                                            <i class="fas fa-ban"></i>
                                        </button>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                            <%
                                        }
                                    } catch (Exception e) {
                                        out.println("<tr><td colspan='10' class='text-danger text-center'>");
                                        out.println("<i class='fas fa-exclamation-triangle'></i> ");
                                        out.println("Error al cargar datos adicionales: " + e.getMessage());
                                        out.println("</td></tr>");
                                    } finally {
                                        if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                                        if (st != null) try { st.close(); } catch (SQLException ignore) {}
                                        if (conexionDB != null) try { conexionDB.cerrarConexion(); } catch (SQLException ignore) {}
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="10" class="text-center text-muted py-4">
                                    <i class="fas fa-users fa-3x mb-3"></i><br>
                                    <h5>No hay usuarios registrados</h5>
                                    <p>Comienza agregando el primer usuario al sistema</p>
                                    <a href="UsuarioServlet?accion=nuevo" class="btn btn-primary">
                                        <i class="fas fa-user-plus"></i> Agregar Usuario
                                    </a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Ver Usuario -->
    <div class="modal fade" id="verUsuarioModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-info text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-user"></i> Detalles del Usuario
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="text-primary">Información de Acceso</h6>
                            <p><strong>ID:</strong> <span id="modalId"></span></p>
                            <p><strong>Username:</strong> <span id="modalUsername"></span></p>
                            <p><strong>Nivel:</strong> <span id="modalNivel"></span></p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="text-primary">Información Personal</h6>
                            <p><strong>Nombres:</strong> <span id="modalNombres"></span></p>
                            <p><strong>Apellidos:</strong> <span id="modalApellidos"></span></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="text-primary">Contacto</h6>
                            <p><strong>Correo:</strong> <span id="modalCorreo"></span></p>
                            <p><strong>Teléfono:</strong> <span id="modalTelefono"></span></p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="text-primary">Fechas</h6>
                            <p><strong>Registro:</strong> <span id="modalFechaCreacion"></span></p>
                            <p><strong>Última actualización:</strong> <span id="modalFechaActualizacion"></span></p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    <a id="modalEditarBtn" href="#" class="btn btn-warning">
                        <i class="fas fa-edit"></i> Editar
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
    
    <!-- Scripts personalizados -->
    <script>
        // Funciones de búsqueda y filtrado
        function filtrarTabla() {
            const searchInput = document.getElementById('searchInput').value.toLowerCase();
            const levelFilter = document.getElementById('levelFilter').value;
            const sortBy = document.getElementById('sortBy').value;
            
            const table = document.getElementById('usuariosTable');
            const rows = Array.from(table.querySelectorAll('tbody tr'));
            
            let visibleRows = 0;
            
            // Filtrar filas
            rows.forEach(row => {
                if (row.cells.length === 1) return; // Skip empty row
                
                const username = row.getAttribute('data-username') || '';
                const nombres = row.getAttribute('data-nombres') || '';
                const apellidos = row.getAttribute('data-apellidos') || '';
                const correo = row.getAttribute('data-correo') || '';
                const nivel = row.getAttribute('data-nivel') || '';
                
                const matchesSearch = username.includes(searchInput) || 
                                    nombres.includes(searchInput) || 
                                    apellidos.includes(searchInput) || 
                                    correo.includes(searchInput);
                
                const matchesLevel = !levelFilter || nivel === levelFilter;
                
                if (matchesSearch && matchesLevel) {
                    row.style.display = '';
                    visibleRows++;
                } else {
                    row.style.display = 'none';
                }
            });
            
            // Actualizar contador
            document.getElementById('resultCount').textContent = visibleRows + ' usuarios';
            
            // Ordenar si es necesario
            if (sortBy && visibleRows > 0) {
                ordenarTabla(sortBy);
            }
        }
        
        function ordenarTabla(criteria) {
            const table = document.getElementById('usuariosTable');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr')).filter(row => row.style.display !== 'none' && row.cells.length > 1);
            
            rows.sort((a, b) => {
                switch (criteria) {
                    case 'nombre_asc':
                        return a.getAttribute('data-nombres').localeCompare(b.getAttribute('data-nombres'));
                    case 'nombre_desc':
                        return b.getAttribute('data-nombres').localeCompare(a.getAttribute('data-nombres'));
                    case 'username_asc':
                        return a.getAttribute('data-username').localeCompare(b.getAttribute('data-username'));
                    case 'fecha_asc':
                        return new Date(a.getAttribute('data-fecha')) - new Date(b.getAttribute('data-fecha'));
                    case 'fecha_desc':
                    default:
                        return new Date(b.getAttribute('data-fecha')) - new Date(a.getAttribute('data-fecha'));
                }
            });
            
            // Reordenar en el DOM
            rows.forEach((row, index) => {
                row.cells[0].textContent = index + 1; // Actualizar numeración
                tbody.appendChild(row);
            });
        }
        
        function clearFilters() {
            document.getElementById('searchInput').value = '';
            document.getElementById('levelFilter').value = '';
            document.getElementById('sortBy').value = 'fecha_desc';
            filtrarTabla();
        }
        
        // Event listeners para filtros
        document.getElementById('searchInput').addEventListener('input', filtrarTabla);
        document.getElementById('levelFilter').addEventListener('change', filtrarTabla);
        document.getElementById('sortBy').addEventListener('change', filtrarTabla);
        
        // Función para mostrar detalles del usuario
        function verUsuario(id, username, nombres, apellidos, correo, telefono, nivel, fechaCreacion, fechaActualizacion) {
            document.getElementById('modalId').textContent = id;
            document.getElementById('modalUsername').textContent = username;
            document.getElementById('modalNombres').textContent = nombres;
            document.getElementById('modalApellidos').textContent = apellidos;
            document.getElementById('modalCorreo').textContent = correo;
            document.getElementById('modalTelefono').textContent = telefono;
            document.getElementById('modalNivel').textContent = nivel;
            document.getElementById('modalFechaCreacion').textContent = fechaCreacion;
            document.getElementById('modalFechaActualizacion').textContent = fechaActualizacion;
            document.getElementById('modalEditarBtn').href = 'UsuarioServlet?accion=editar&id=' + id;
        }
        
        // Función para eliminar usuario
        function eliminarUsuario(id, username) {
            if (confirm('¿Estás seguro de eliminar al usuario "' + username + '"?\n\nEsta acción no se puede deshacer.')) {
                window.location.href = 'UsuarioServlet?accion=eliminar&id=' + id;
            }
        }
        
        // Calcular estadísticas al cargar la página
        document.addEventListener('DOMContentLoaded', function() {
            const rows = document.querySelectorAll('#usuariosTable tbody tr[data-nivel]');
            let adminCount = 0;
            let userCount = 0;
            let recentCount = 0;
            
            const today = new Date().toDateString();
            
            rows.forEach(row => {
                const nivel = row.getAttribute('data-nivel');
                const fecha = row.getAttribute('data-fecha');
                
                if (nivel === '1') adminCount++;
                else if (nivel === '2') userCount++;
                
                if (fecha && new Date(fecha).toDateString() === today) {
                    recentCount++;
                }
            });
            
            document.getElementById('adminCount').textContent = adminCount;
            document.getElementById('userCount').textContent = userCount;
            document.getElementById('recentCount').textContent = recentCount;
        });
    </script>
</body>
</html>
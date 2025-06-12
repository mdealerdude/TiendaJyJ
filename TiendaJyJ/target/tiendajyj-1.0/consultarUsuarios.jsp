<%-- 
    Document   : consultarUsuario
    Created on : Jun 9, 2025, 3:34:46 PM
    Author     : MINEDUCYT
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="tiendajyj.servlet.Conexion"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Usuarios - Tienda JyJ</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
    <style>
        .table-container {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .table th {
            background-color: #343a40 !important;
            color: white !important;
            border: none !important;
            text-align: center;
            vertical-align: middle;
            font-weight: 600;
            padding: 15px 8px;
        }
        
        .table td {
            vertical-align: middle;
            text-align: center;
            padding: 12px 8px;
            border-bottom: 1px solid #dee2e6;
        }
        
        .table-hover tbody tr:hover {
            background-color: #f8f9fa;
        }
        
        .btn-group .btn {
            margin: 0 1px;
            padding: 6px 10px;
        }
        
        .badge {
            font-size: 0.75em;
            padding: 6px 10px;
        }
        
        .loading-spinner {
            display: none;
            text-align: center;
            padding: 40px;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #6c757d;
        }
        
        .search-highlight {
            background-color: #fff3cd;
            padding: 2px 4px;
            border-radius: 3px;
        }
        
        .card-header {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
        }
        
        .status-badge-admin {
            background-color: #dc3545;
            color: white;
        }
        
        .status-badge-user {
            background-color: #28a745;
            color: white;
        }
    </style>
</head>

<body class="bg-light">
    <div class="container-fluid py-4">
        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header">
                        <h3 class="mb-0">
                            <i class="fas fa-users me-2"></i>
                            Consultar Usuarios
                        </h3>
                    </div>
                    <div class="card-body">
                        
                        <!-- Controles de búsqueda y filtrado -->
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <div class="input-group">
                                                    <span class="input-group-text">
                                                        <i class="fas fa-search"></i>
                                                    </span>
                                                    <input type="text" class="form-control" id="searchInput" 
                                                           placeholder="Buscar por nombre, usuario o correo...">
                                                </div>
                                            </div>
                                            <div class="col-md-3">
                                                <select class="form-select" id="nivelFilter">
                                                    <option value="">Todos los niveles</option>
                                                    <option value="1">Administrador</option>
                                                    <option value="2">Usuario</option>
                                                </select>
                                            </div>
                                            <div class="col-md-3">
                                                <select class="form-select" id="sortBy">
                                                    <option value="fecha_desc">Más recientes</option>
                                                    <option value="fecha_asc">Más antiguos</option>
                                                    <option value="nombre_asc">Nombre A-Z</option>
                                                    <option value="nombre_desc">Nombre Z-A</option>
                                                </select>
                                            </div>
                                            <div class="col-md-2">
                                                <button class="btn btn-outline-secondary w-100" onclick="limpiarFiltros()">
                                                    <i class="fas fa-times"></i> Limpiar
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Contador de resultados -->
                        <div class="row mb-3">
                            <div class="col-12">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="text-muted">
                                        <i class="fas fa-users"></i>
                                        <span id="resultadosCount">Cargando usuarios...</span>
                                    </span>
                                    <div>
                                        <a href="UsuarioServlet?accion=nuevo" class="btn btn-primary">
                                            <i class="fas fa-plus"></i> Nuevo Usuario
                                        </a>
                                        <a href="UsuarioServlet?accion=listar" class="btn btn-outline-secondary">
                                            <i class="fas fa-list"></i> Gestionar Usuarios
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Spinner de carga -->
                        <div class="loading-spinner" id="loadingSpinner">
                            <div class="spinner-border text-primary" role="status">
                                <span class="visually-hidden">Cargando...</span>
                            </div>
                            <p class="mt-2">Cargando usuarios...</p>
                        </div>

                        <!-- Tabla de usuarios -->
                        <div class="table-container">
                            <table class="table table-hover mb-0" id="usuariosTable">
                                <thead>
                                    <tr>
                                        <th style="width: 5%;">ID</th>
                                        <th style="width: 12%;">Username</th>
                                        <th style="width: 15%;">Nombres</th>
                                        <th style="width: 15%;">Apellidos</th>
                                        <th style="width: 18%;">Correo</th>
                                        <th style="width: 12%;">Teléfono</th>
                                        <th style="width: 10%;">Nivel</th>
                                        <th style="width: 13%;">Fecha Registro</th>
                                    </tr>
                                </thead>
                                <tbody id="usuariosTableBody">
                                    <%
                                        Connection conn = null;
                                        PreparedStatement ps = null;
                                        ResultSet rs = null;
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        int totalUsuarios = 0;
                                        
                                        try {
                                            Conexion conexion = new Conexion();
                                            conn = conexion.getConnection();
                                            
                                            String sql = "SELECT u.*, " +
                                                        "CASE " +
                                                        "    WHEN u.id_nivel_usuario = 1 THEN 'Administrador' " +
                                                        "    WHEN u.id_nivel_usuario = 2 THEN 'Usuario' " +
                                                        "    ELSE 'Desconocido' " +
                                                        "END as nivel_nombre " +
                                                        "FROM usuarios u " +
                                                        "ORDER BY u.fecha_creacion_usuario DESC";
                                            
                                            ps = conn.prepareStatement(sql);
                                            rs = ps.executeQuery();
                                            
                                            while (rs.next()) {
                                                totalUsuarios++;
                                                String nivelClass = rs.getInt("id_nivel_usuario") == 1 ? "status-badge-admin" : "status-badge-user";
                                                String fechaCreacion = rs.getTimestamp("fecha_creacion_usuario") != null ? 
                                                                     sdf.format(rs.getTimestamp("fecha_creacion_usuario")) : "N/A";
                                    %>
                                    <tr>
                                        <td><span class="badge bg-secondary"><%= rs.getInt("id_usuario") %></span></td>
                                        <td><strong><%= rs.getString("username") %></strong></td>
                                        <td><%= rs.getString("nombres_usuario") %></td>
                                        <td><%= rs.getString("apellidos_usuario") %></td>
                                        <td>
                                            <a href="mailto:<%= rs.getString("correo_usuario") %>" class="text-decoration-none">
                                                <%= rs.getString("correo_usuario") %>
                                            </a>
                                        </td>
                                        <td>
                                            <a href="tel:<%= rs.getString("telefono_usuario") %>" class="text-decoration-none">
                                                <%= rs.getString("telefono_usuario") %>
                                            </a>
                                        </td>
                                        <td>
                                            <span class="badge <%= nivelClass %>">
                                                <%= rs.getString("nivel_nombre") %>
                                            </span>
                                        </td>
                                        <td>
                                            <small class="text-muted">
                                                <i class="fas fa-calendar-alt"></i>
                                                <%= fechaCreacion %>
                                            </small>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                            
                                            if (totalUsuarios == 0) {
                                    %>
                                    <tr>
                                        <td colspan="8" class="no-data">
                                            <div>
                                                <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                                <h5 class="text-muted">No hay usuarios registrados</h5>
                                                <p class="text-muted">Comienza agregando el primer usuario al sistema.</p>
                                                <a href="UsuarioServlet?accion=nuevo" class="btn btn-primary">
                                                    <i class="fas fa-plus"></i> Crear Usuario
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                            
                                        } catch (SQLException e) {
                                            out.println("<tr><td colspan='8' class='text-danger text-center'>");
                                            out.println("<i class='fas fa-exclamation-triangle'></i> ");
                                            out.println("Error al cargar los usuarios: " + e.getMessage());
                                            out.println("</td></tr>");
                                        } finally {
                                            try {
                                                if (rs != null) rs.close();
                                                if (ps != null) ps.close();
                                                if (conn != null) conn.close();
                                            } catch (SQLException e) {
                                                // Log error
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>

                        <!-- Información adicional -->
                        <div class="row mt-4">
                            <div class="col-md-8">
                                <div class="card bg-light">
                                    <div class="card-body">
                                        <h6 class="card-title">
                                            <i class="fas fa-info-circle text-info"></i>
                                            Información del Sistema
                                        </h6>
                                        <p class="card-text mb-1">
                                            <strong>Total de usuarios registrados:</strong> <%= totalUsuarios %>
                                        </p>
                                        <p class="card-text mb-1">
                                            <strong>Última actualización:</strong> <%= new java.util.Date() %>
                                        </p>
                                        <p class="card-text mb-0">
                                            <strong>Estado del sistema:</strong> 
                                            <span class="badge bg-success">Activo</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card bg-primary text-white">
                                    <div class="card-body">
                                        <h6 class="card-title">
                                            <i class="fas fa-chart-bar"></i>
                                            Estadísticas Rápidas
                                        </h6>
                                        <p class="card-text mb-1">
                                            <strong>Usuarios activos:</strong> <%= totalUsuarios %>
                                        </p>
                                        <p class="card-text mb-0">
                                            <strong>Registros hoy:</strong> 
                                            <span class="badge bg-light text-dark">0</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Variables globales
        let usuariosData = [];
        let filteredData = [];

        // Inicialización
        document.addEventListener('DOMContentLoaded', function() {
            cargarDatosUsuarios();
            configurarEventListeners();
            actualizarContador();
        });

        // Cargar datos de usuarios en memoria para filtrado
        function cargarDatosUsuarios() {
            const tabla = document.getElementById('usuariosTableBody');
            const filas = tabla.querySelectorAll('tr');
            
            usuariosData = [];
            filas.forEach(fila => {
                if (fila.cells.length > 1) { // Evitar la fila de "no hay datos"
                    const usuario = {
                        id: fila.cells[0].textContent.trim(),
                        username: fila.cells[1].textContent.trim(),
                        nombres: fila.cells[2].textContent.trim(),
                        apellidos: fila.cells[3].textContent.trim(),
                        correo: fila.cells[4].textContent.trim(),
                        telefono: fila.cells[5].textContent.trim(),
                        nivel: fila.cells[6].textContent.trim(),
                        fecha: fila.cells[7].textContent.trim(),
                        elemento: fila
                    };
                    usuariosData.push(usuario);
                }
            });
            
            filteredData = [...usuariosData];
        }

        // Configurar event listeners
        function configurarEventListeners() {
            const searchInput = document.getElementById('searchInput');
            const nivelFilter = document.getElementById('nivelFilter');
            const sortBy = document.getElementById('sortBy');

            searchInput.addEventListener('input', aplicarFiltros);
            nivelFilter.addEventListener('change', aplicarFiltros);
            sortBy.addEventListener('change', aplicarFiltros);
        }

        // Aplicar filtros
        function aplicarFiltros() {
            const searchTerm = document.getElementById('searchInput').value.toLowerCase();
            const nivelSelected = document.getElementById('nivelFilter').value;
            const sortOption = document.getElementById('sortBy').value;

            // Filtrar datos
            filteredData = usuariosData.filter(usuario => {
                const matchesSearch = !searchTerm || 
                                    usuario.username.toLowerCase().includes(searchTerm) ||
                                    usuario.nombres.toLowerCase().includes(searchTerm) ||
                                    usuario.apellidos.toLowerCase().includes(searchTerm) ||
                                    usuario.correo.toLowerCase().includes(searchTerm);
                
                const matchesNivel = !nivelSelected || 
                                   (nivelSelected === '1' && usuario.nivel.includes('Administrador')) ||
                                   (nivelSelected === '2' && usuario.nivel.includes('Usuario'));

                return matchesSearch && matchesNivel;
            });

            // Ordenar datos
            ordenarDatos(sortOption);
            
            // Actualizar tabla
            actualizarTabla();
            actualizarContador();
        }

        // Ordenar datos
        function ordenarDatos(sortOption) {
            filteredData.sort((a, b) => {
                switch(sortOption) {
                    case 'nombre_asc':
                        return a.nombres.localeCompare(b.nombres);
                    case 'nombre_desc':
                        return b.nombres.localeCompare(a.nombres);
                    case 'fecha_asc':
                        return a.fecha.localeCompare(b.fecha);
                    case 'fecha_desc':
                    default:
                        return b.fecha.localeCompare(a.fecha);
                }
            });
        }

        // Actualizar tabla
        function actualizarTabla() {
            const tbody = document.getElementById('usuariosTableBody');
            
            // Ocultar todas las filas
            usuariosData.forEach(usuario => {
                usuario.elemento.style.display = 'none';
            });

            // Mostrar filas filtradas
            filteredData.forEach(usuario => {
                usuario.elemento.style.display = '';
            });

            // Mostrar mensaje si no hay resultados
            const noResultsRow = document.querySelector('.no-results');
            if (noResultsRow) {
                noResultsRow.remove();
            }

            if (filteredData.length === 0 && usuariosData.length > 0) {
                const row = document.createElement('tr');
                row.className = 'no-results';
                row.innerHTML = `
                    <td colspan="8" class="no-data">
                        <div>
                            <i class="fas fa-search fa-2x text-muted mb-3"></i>
                            <h5 class="text-muted">No se encontraron resultados</h5>
                            <p class="text-muted">Intenta con otros términos de búsqueda.</p>
                        </div>
                    </td>
                `;
                tbody.appendChild(row);
            }
        }

        // Actualizar contador
        function actualizarContador() {
            const contador = document.getElementById('resultadosCount');
            const total = usuariosData.length;
            const mostrados = filteredData.length;
            
            if (total === 0) {
                contador.textContent = 'No hay usuarios registrados';
            } else if (mostrados === total) {
                contador.textContent = `Total: ${total} usuarios`;
            } else {
                contador.textContent = `Mostrando ${mostrados} de ${total} usuarios`;
            }
        }

        // Limpiar filtros
        function limpiarFiltros() {
            document.getElementById('searchInput').value = '';
            document.getElementById('nivelFilter').value = '';
            document.getElementById('sortBy').value = 'fecha_desc';
            aplicarFiltros();
        }

        // Función para resaltar texto buscado
        function resaltarTexto(texto, termino) {
            if (!termino) return texto;
            const regex = new RegExp(`(${termino})`, 'gi');
            return texto.replace(regex, '<span class="search-highlight">$1</span>');
        }
    </script>
</body>
</html>
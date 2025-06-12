<%@page import="tiendajyj.model.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios - Tienda JyJ</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
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
        
        .alert {
            border-radius: 0.5rem;
            margin-bottom: 1rem;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #6c757d;
        }
        
        .action-buttons {
            white-space: nowrap;
        }
        
        .search-highlight {
            background-color: #fff3cd;
            padding: 2px 4px;
            border-radius: 3px;
        }
    </style>
</head>

<body class="bg-light">
    <jsp:include page="menuppl.jsp" />
    <div class="container-fluid py-4">
        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header">
                        <div class="d-flex justify-content-between align-items-center">
                            <h3 class="mb-0">
                                <i class="fas fa-users me-2"></i>
                                Gestión de Usuarios
                            </h3>
                            <div>
                                <a href="UsuarioServlet?accion=nuevo" class="btn btn-light">
                                    <i class="fas fa-plus"></i> Nuevo Usuario
                                </a>
                                <a href="consultarUsuarios.jsp" class="btn btn-outline-light">
                                    <i class="fas fa-search"></i> Consultar
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        
                        <!-- Mensajes de éxito o error -->
                        <%
                            String mensaje = (String) request.getAttribute("mensaje");
                            String error = (String) request.getAttribute("error");
                            
                            if (mensaje != null && !mensaje.trim().isEmpty()) {
                        %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                <%= mensaje %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <%
                            }
                            
                            if (error != null && !error.trim().isEmpty()) {
                        %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <%= error %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        <%
                            }
                        %>

                        <!-- Barra de búsqueda -->
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-6">
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
                                                <button class="btn btn-outline-secondary w-100" onclick="limpiarFiltros()">
                                                    <i class="fas fa-times"></i> Limpiar Filtros
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
                                <span class="text-muted">
                                    <i class="fas fa-users"></i>
                                    <span id="resultadosCount">
                                        <%
                                            List<Usuario> lista = (List<Usuario>) request.getAttribute("lista");
                                            int totalUsuarios = (lista != null) ? lista.size() : 0;
                                        %>
                                        Total: <%= totalUsuarios %> usuarios
                                    </span>
                                </span>
                            </div>
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
                                        <th style="width: 13%;">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="usuariosTableBody">
                                    <%
                                        if (lista != null && !lista.isEmpty()) {
                                            for (Usuario usuario : lista) {
                                                String nivelTexto = "";
                                                String nivelClass = "";
                                                
                                                switch (usuario.getIdNivelUsuario()) {
                                                    case 1:
                                                        nivelTexto = "Administrador";
                                                        nivelClass = "status-badge-admin";
                                                        break;
                                                    case 2:
                                                        nivelTexto = "Usuario";
                                                        nivelClass = "status-badge-user";
                                                        break;
                                                    default:
                                                        nivelTexto = "Desconocido";
                                                        nivelClass = "bg-secondary";
                                                        break;
                                                }
                                    %>
                                    <tr>
                                        <td>
                                            <span class="badge bg-secondary"><%= usuario.getIdUsuario() %></span>
                                        </td>
                                        <td>
                                            <strong><%= usuario.getUsername() %></strong>
                                        </td>
                                        <td><%= usuario.getNombresUsuario() %></td>
                                        <td><%= usuario.getApellidosUsuario() %></td>
                                        <td>
                                            <a href="mailto:<%= usuario.getCorreoUsuario() %>" class="text-decoration-none">
                                                <%= usuario.getCorreoUsuario() %>
                                            </a>
                                        </td>
                                        <td>
                                            <a href="tel:<%= usuario.getTelefonoUsuario() %>" class="text-decoration-none">
                                                <%= usuario.getTelefonoUsuario() %>
                                            </a>
                                        </td>
                                        <td>
                                            <span class="badge <%= nivelClass %>">
                                                <%= nivelTexto %>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group action-buttons" role="group">
                                                <a href="UsuarioServlet?accion=editar&id=<%= usuario.getIdUsuario() %>" 
                                                   class="btn btn-sm btn-outline-primary" 
                                                   title="Editar usuario">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button type="button" 
                                                        class="btn btn-sm btn-outline-danger" 
                                                        title="Eliminar usuario"
                                                        onclick="confirmarEliminacion(<%= usuario.getIdUsuario() %>, '<%= usuario.getUsername() %>')">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        } else {
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
                                            <i class="fas fa-cogs"></i>
                                            Acciones Rápidas
                                        </h6>
                                        <div class="d-grid gap-2">
                                            <a href="UsuarioServlet?accion=nuevo" class="btn btn-light btn-sm">
                                                <i class="fas fa-plus"></i> Nuevo Usuario
                                            </a>
                                            <a href="consultarUsuarios.jsp" class="btn btn-outline-light btn-sm">
                                                <i class="fas fa-search"></i> Vista de Consulta
                                            </a>
                                        </div>
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

            if (searchInput) {
                searchInput.addEventListener('input', aplicarFiltros);
            }
            if (nivelFilter) {
                nivelFilter.addEventListener('change', aplicarFiltros);
            }
        }

        // Aplicar filtros
        function aplicarFiltros() {
            const searchInput = document.getElementById('searchInput');
            const nivelFilter = document.getElementById('nivelFilter');
            
            if (!searchInput || !nivelFilter) return;
            
            const searchTerm = searchInput.value.toLowerCase();
            const nivelSelected = nivelFilter.value;

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
            
            // Actualizar tabla
            actualizarTabla();
            actualizarContador();
        }

        // Actualizar tabla
        function actualizarTabla() {
            // Ocultar todas las filas
            usuariosData.forEach(usuario => {
                usuario.elemento.style.display = 'none';
            });

            // Mostrar filas filtradas
            filteredData.forEach(usuario => {
                usuario.elemento.style.display = '';
            });

            // Mostrar mensaje si no hay resultados
            const tbody = document.getElementById('usuariosTableBody');
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
            if (!contador) return;
            
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
            const searchInput = document.getElementById('searchInput');
            const nivelFilter = document.getElementById('nivelFilter');
            
            if (searchInput) searchInput.value = '';
            if (nivelFilter) nivelFilter.value = '';
            
            aplicarFiltros();
        }

        // Confirmar eliminación
        function confirmarEliminacion(id, username) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `¿Deseas eliminar al usuario "${username}"? Esta acción no se puede deshacer.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = `UsuarioServlet?accion=eliminar&id=${id}`;
                }
            });
        }

        // Mostrar mensaje de confirmación si hay mensaje en URL
        const urlParams = new URLSearchParams(window.location.search);
        const mensaje = urlParams.get('mensaje');
        const error = urlParams.get('error');

        if (mensaje) {
            Swal.fire({
                title: '¡Éxito!',
                text: mensaje,
                icon: 'success',
                timer: 3000,
                showConfirmButton: false
            });
        }

        if (error) {
            Swal.fire({
                title: 'Error',
                text: error,
                icon: 'error',
                confirmButtonText: 'Entendido'
            });
        }
    </script>
</body>
</html>
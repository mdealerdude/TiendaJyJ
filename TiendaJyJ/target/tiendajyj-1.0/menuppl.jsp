<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="#">
                <i class="fas fa-prescription-bottle-alt me-2"></i>Tienda J&ampJ
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarOpciones" aria-controls="navbarOpciones" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarOpciones">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 text-uppercase fw-semibold">
                    <li class="nav-item">
                        <a class="nav-link active" href="principal.jsp"><i class="fas fa-home me-1"></i>Inicio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="UsuarioServlet?accion=listar"><i class="fas fa-users me-1"></i>Usuarios</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="ClienteServlet?accion=listar"><i class="fas fa-user-friends me-1"></i>Clientes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="ProductoServlet?accion=listar"><i class="fas fa-user-friends me-1"></i>Productos</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="VentaServlet?accion=listar"><i class="fas fa-shopping-cart me-1"></i>Ventas</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-warning" href="CerrarSesion"><i class="fas fa-sign-out-alt me-1"></i>Salir</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
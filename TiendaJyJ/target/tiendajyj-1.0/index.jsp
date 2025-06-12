
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Sistema de Tienda J&amp;J - Login</title>
    <!-- Bootstrap 5 CDN -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background: url('images/loginTJYJ.jpg') no-repeat center center fixed; background-size: cover;">
    <div class="bg-dark bg-opacity-50 min-vh-100 d-flex justify-content-center align-items-center">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="card shadow-lg border-0 rounded-4">
                        <div class="card-body p-4">
                            <h3 class="card-title text-center mb-4">Ingreso al Sistema de Tienda</h3>

                            <%-- Mostrar mensaje de error si existe --%>
                            <%
                                String error = request.getParameter("error");
                                if ("1".equals(error)) {
                            %>
                                <div class="alert alert-danger text-center" role="alert">
                                    Usuario o contraseña incorrectos
                                </div>
                            <%
                                }
                            %>

                            <form action="LoginServlet" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Usuario</label>
                                    <input type="text" class="form-control" id="usuario" name="username" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Ingresar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <p class="text-center text-light mt-3">&copy; 2025 Sistema de Tienda JyJ</p>
                </div>
            </div>
        </div>
    </div>

<!-- Bootstrap JS (opcional, para interactividad) -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>
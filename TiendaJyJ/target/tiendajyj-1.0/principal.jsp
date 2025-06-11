

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("USER") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    String usuario = (String) session.getAttribute("USER");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Principal - Tienda JyJ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap 5 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome (opcional para íconos) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            background-color: #f8f9fa;
        }

        .navbar {
            font-size: 1.2rem;
            padding: 1rem 1.5rem;
            transition: box-shadow 0.3s ease;
        }

        .navbar.scrolled {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .nav-link {
            transition: color 0.2s ease;
        }

        .nav-link:hover {
            color: #ffc107 !important;
        }

        .welcome-box {
            background: white;
            padding: 2rem;
            border-radius: 0.5rem;
            margin-top: 3rem;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<jsp:include page="menuppl.jsp" />
    <!-- Bienvenida -->
    <main class="container">
        <div class="welcome-box">
            <h2>Bienvenido, <%= usuario %> </h2>
            <p>Selecciona una opción del menú para comenzar.</p>
            
        </div>
    </main>

    <!-- Bootstrap JS -->
    <script src="js/bootstrap.bundle.min.js"></script>

    <!-- Efecto scroll sombra -->
    <script>
        window.addEventListener('scroll', function () {
            const navbar = document.querySelector('.navbar');
            navbar.classList.toggle('scrolled', window.scrollY > 10);
        });
    </script>
</body>
</html>
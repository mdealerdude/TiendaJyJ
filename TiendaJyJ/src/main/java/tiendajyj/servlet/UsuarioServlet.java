package tiendajyj.servlet;

import tiendajyj.dao.UsuarioDAO;
import tiendajyj.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        try {
            usuarioDAO = new UsuarioDAO();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // doPost para insertar y actualizar
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            if ("insertar".equals(accion)) {
                insertarUsuario(request, response);
            } else if ("actualizar".equals(accion)) {
                actualizarUsuario(request, response);
            } else {
                response.sendRedirect("UsuarioServlet?accion=listar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error en el servlet: " + e.getMessage(), e);
        }
    }

    // doGet para listar, mostrar formulario nuevo, eliminar y editar
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            if (accion == null) accion = "listar";
            switch (accion) {
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "listar":
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error en el servlet: " + e.getMessage(), e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
        request.setAttribute("lista", listaUsuarios);
        
        // Pasar mensajes de éxito o error si existen
        String mensaje = request.getParameter("mensaje");
        String error = request.getParameter("error");
        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
        }
        if (error != null) {
            request.setAttribute("error", error);
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("usuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        // Obtener parámetros del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombres = request.getParameter("nombres_usuario");
        String apellidos = request.getParameter("apellidos_usuario");
        String correo = request.getParameter("correo_usuario");
        String telefono = request.getParameter("telefono_usuario");
        String nivelStr = request.getParameter("id_nivel_usuario");
        
        // Validaciones básicas del lado del servidor
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "El username es requerido");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "La contraseña es requerida");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (nombres == null || nombres.trim().isEmpty()) {
            request.setAttribute("error", "Los nombres son requeridos");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (apellidos == null || apellidos.trim().isEmpty()) {
            request.setAttribute("error", "Los apellidos son requeridos");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "El correo electrónico es requerido");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (telefono == null || telefono.trim().isEmpty()) {
            request.setAttribute("error", "El teléfono es requerido");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar formato de username
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            request.setAttribute("error", "El username solo puede contener letras, números y guiones bajos");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar formato de correo
        if (!correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("error", "Formato de correo electrónico inválido");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar nivel de usuario
        int idNivel;
        try {
            idNivel = Integer.parseInt(nivelStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Debe seleccionar un nivel de usuario válido");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Crear el objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username.trim());
        usuario.setPassword(password);
        usuario.setNombresUsuario(nombres.trim());
        usuario.setApellidosUsuario(apellidos.trim());
        usuario.setCorreoUsuario(correo.trim());
        usuario.setTelefonoUsuario(telefono.trim());
        usuario.setIdNivelUsuario(idNivel);
        
        // Intentar insertar el usuario
        boolean exito = usuarioDAO.insertarUsuario(usuario);
        
        if (exito) {
            response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario registrado exitosamente");
        } else {
            request.setAttribute("error", "Error al registrar el usuario. El username ya existe o hay un problema en la base de datos.");
            request.setAttribute("formData", request.getParameterMap());
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = usuarioDAO.eliminarUsuario(id);
            
            if (exito) {
                response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario eliminado exitosamente");
            } else {
                response.sendRedirect("UsuarioServlet?accion=listar&error=Error al eliminar el usuario");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("UsuarioServlet?accion=listar&error=Usuario no encontrado");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        String idStr = request.getParameter("id_usuario");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            return;
        }
        
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            return;
        }
        
        // Obtener el usuario actual para comparar
        Usuario usuarioActual = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        if (usuarioActual == null) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=Usuario no encontrado");
            return;
        }
        
        // Obtener parámetros del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombres = request.getParameter("nombres_usuario");
        String apellidos = request.getParameter("apellidos_usuario");
        String correo = request.getParameter("correo_usuario");
        String telefono = request.getParameter("telefono_usuario");
        String nivelStr = request.getParameter("id_nivel_usuario");
        
        // Validaciones básicas
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "El username es requerido");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar formato de username
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            request.setAttribute("error", "El username solo puede contener letras, números y guiones bajos");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar que el username no esté duplicado (excluyendo el usuario actual)
        if (usuarioDAO.existeUsernameExcluyendoId(username.trim(), idUsuario)) {
            request.setAttribute("error", "El username ya existe");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar formato de correo
        if (correo != null && !correo.trim().isEmpty() && !correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("error", "Formato de correo electrónico inválido");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar nivel de usuario
        int idNivel;
        try {
            idNivel = Integer.parseInt(nivelStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Debe seleccionar un nivel de usuario válido");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Actualizar los datos del usuario
        usuarioActual.setUsername(username.trim());
        usuarioActual.setNombresUsuario(nombres != null ? nombres.trim() : usuarioActual.getNombresUsuario());
        usuarioActual.setApellidosUsuario(apellidos != null ? apellidos.trim() : usuarioActual.getApellidosUsuario());
        usuarioActual.setCorreoUsuario(correo != null ? correo.trim() : usuarioActual.getCorreoUsuario());
        usuarioActual.setTelefonoUsuario(telefono != null ? telefono.trim() : usuarioActual.getTelefonoUsuario());
        usuarioActual.setIdNivelUsuario(idNivel);
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (password != null && !password.trim().isEmpty()) {
            usuarioActual.setPassword(password);
        }
        
        // Intentar actualizar el usuario
        boolean exito = usuarioDAO.actualizarUsuario(usuarioActual);
        
        if (exito) {
            response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario actualizado exitosamente");
        } else {
            request.setAttribute("error", "Error al actualizar el usuario");
            request.setAttribute("usuario", usuarioActual);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }
}
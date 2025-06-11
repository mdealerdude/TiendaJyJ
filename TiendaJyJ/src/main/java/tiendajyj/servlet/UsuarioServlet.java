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
        
        // Validaciones del lado del servidor
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmarPassword = request.getParameter("confirmar_password");
        
        // Validar que las contraseñas coincidan
        if (password != null && confirmarPassword != null && !password.equals(confirmarPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar que el username no esté vacío y tenga formato válido
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "El username es requerido");
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar formato de username (solo letras, números y guiones bajos)
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            request.setAttribute("error", "El username solo puede contener letras, números y guiones bajos");
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        Usuario usuario = obtenerUsuarioDesdeFormulario(request);
        boolean exito = usuarioDAO.insertarUsuario(usuario);
        
        if (exito) {
            response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario registrado exitosamente");
        } else {
            request.setAttribute("error", "Error al registrar el usuario. Posiblemente el username ya existe.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = usuarioDAO.eliminarUsuario(id);
        
        if (exito) {
            response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario eliminado exitosamente");
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar&error=Error al eliminar el usuario");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
        request.setAttribute("usuario", usuario);
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        String username = request.getParameter("username");
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
        
        // Validar que el username no esté vacío
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "El username es requerido");
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Validar que el username no esté duplicado (excluyendo el usuario actual)
        if (usuarioDAO.existeUsernameExcluyendoId(username, idUsuario)) {
            request.setAttribute("error", "El username ya existe");
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        Usuario usuario = obtenerUsuarioDesdeFormulario(request);
        usuario.setIdUsuario(idUsuario);
        boolean exito = usuarioDAO.actualizarUsuario(usuario);
        
        if (exito) {
            response.sendRedirect("UsuarioServlet?accion=listar&mensaje=Usuario actualizado exitosamente");
        } else {
            request.setAttribute("error", "Error al actualizar el usuario");
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }

    private Usuario obtenerUsuarioDesdeFormulario(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        
        // Validar y obtener parámetros
        String idNivelStr = request.getParameter("id_nivel_usuario");
        if (idNivelStr != null && !idNivelStr.trim().isEmpty()) {
            usuario.setIdNivelUsuario(Integer.parseInt(idNivelStr));
        }
        
        usuario.setUsername(request.getParameter("username"));
        usuario.setNombresUsuario(request.getParameter("nombres_usuario"));
        usuario.setApellidosUsuario(request.getParameter("apellidos_usuario"));
        usuario.setCorreoUsuario(request.getParameter("correo_usuario"));
        usuario.setTelefonoUsuario(request.getParameter("telefono_usuario"));
        
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            // Aquí puedes agregar encriptación si lo deseas
            usuario.setPassword(password);
        }
        
        return usuario;
    }
}
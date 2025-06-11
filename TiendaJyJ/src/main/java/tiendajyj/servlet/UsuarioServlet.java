package tiendajyj.servlet;


import tiendajyj.dao.UsuarioDao;
import tiendajyj.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})

public class UsuarioServlet extends HttpServlet {


    private UsuarioDao usuarioDAO;

    @Override
    public void init() {
        try {
            usuarioDAO = new UsuarioDao();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

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
                case "insertar":
                    insertarUsuario(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "actualizar":
                    actualizarUsuario(request, response);
                    break;
                case "cambiarPassword":
                    mostrarFormularioCambiarPassword(request, response);
                    break;
                case "actualizarPassword":
                    actualizarPassword(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        request.setAttribute("lista", usuarioDAO.listarUsuarios());
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
        String username = request.getParameter("username");
        
        // Verificar si el username ya existe
        if (usuarioDAO.existeUsername(username)) {
            request.setAttribute("error", "El nombre de usuario ya existe. Por favor, elija otro.");
            request.setAttribute("usuario", obtenerUsuarioDesdeFormulario(request));
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        Usuario usr = obtenerUsuarioDesdeFormulario(request);
        boolean resultado = usuarioDAO.insertarUsuario(usr);
        
        if (resultado) {
            response.sendRedirect("UsuarioServlet?accion=listar");
        } else {
            request.setAttribute("error", "Error al insertar el usuario.");
            request.setAttribute("usuario", usr);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                usuarioDAO.eliminarUsuario(id);
            } catch (NumberFormatException e) {
                // Manejar error de conversión
                request.setAttribute("error", "ID de usuario inválido.");
            }
        }
        response.sendRedirect("UsuarioServlet?accion=listar");
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                if (usr != null) {
                    request.setAttribute("Usuario", usr);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("UsuarioServlet?accion=listar");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            }
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar");
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            String username = request.getParameter("username");
            
            // Verificar si el username ya existe para otro usuario
            if (usuarioDAO.existeUsernameParaActualizar(username, id)) {
                request.setAttribute("error", "El nombre de usuario ya existe. Por favor, elija otro.");
                Usuario usr = obtenerUsuarioDesdeFormulario(request);
                usr.setId_usuario(id);
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            Usuario usr = obtenerUsuarioDesdeFormulario(request);
            usr.setId_usuario(id);
            boolean resultado = usuarioDAO.actualizarUsuario(usr);
            
            if (resultado) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el usuario.");
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar");
        }
    }

    private void mostrarFormularioCambiarPassword(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                if (usr != null) {
                    request.setAttribute("Usuario", usr);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("cambiarPassword.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("UsuarioServlet?accion=listar");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            }
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar");
        }
    }

    private void actualizarPassword(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            // Verificar que las contraseñas coincidan
            if (!nuevaPassword.equals(confirmarPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden.");
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("cambiarPassword.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            boolean resultado = usuarioDAO.actualizarPassword(id, nuevaPassword);
            
            if (resultado) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al actualizar la contraseña.");
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("cambiarPassword.jsp");
                dispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar");
        }
    }

    private Usuario obtenerUsuarioDesdeFormulario(HttpServletRequest request) {
        Usuario usr = new Usuario();
        
        // Validar y convertir id_nivel_usuario
        String idNivelStr = request.getParameter("id_nivel_usuario");
        if (idNivelStr != null && !idNivelStr.trim().isEmpty()) {
            try {
                usr.setId_nivel_usuario(Integer.parseInt(idNivelStr.trim()));
            } catch (NumberFormatException e) {
                usr.setId_nivel_usuario(0); // Valor por defecto
            }
        } else {
            usr.setId_nivel_usuario(0); // Valor por defecto si está vacío
        }
        
        usr.setUsername(request.getParameter("username"));
        usr.setNombres_usuario(request.getParameter("nombres_usuario"));
        usr.setApellidos_usuario(request.getParameter("apellidos_usuario"));
        usr.setCorreo_usuario(request.getParameter("correo_usuario"));
        usr.setTelefono_usuario(request.getParameter("telefono_usuario"));
        
        // Solo establecer password si viene del formulario de inserción
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            usr.setPassword(password);
        }

        return usr;
    }

    @Override
    public void destroy() {
        if (usuarioDAO != null) {
            usuarioDAO.cerrarConexion();
        }
    }
}
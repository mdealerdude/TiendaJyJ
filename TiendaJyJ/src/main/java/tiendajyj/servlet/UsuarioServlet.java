package tiendajyj.servlet;

import tiendajyj.dao.UsuarioDao;
import tiendajyj.model.Usuario;
import tiendajyj.model.NivelAcceso;
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

    private static final Logger logger = Logger.getLogger(UsuarioServlet.class.getName());
    private UsuarioDao usuarioDAO;

    @Override
    public void init() {
        try {
            usuarioDAO = new UsuarioDao();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al inicializar UsuarioDAO", ex);
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
        
        // Configurar encoding para caracteres especiales
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");

        try {
            if (accion == null || accion.trim().isEmpty()) {
                accion = "listar";
            }
            
            switch (accion.toLowerCase()) {
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
                case "cambiarpassword":
                    mostrarFormularioCambiarPassword(request, response);
                    break;
                case "actualizarpassword":
                    actualizarPassword(request, response);
                    break;
                case "ver":
                    verUsuario(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en UsuarioServlet", e);
            request.setAttribute("error", "Error interno del servidor: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Usuario> lista = usuarioDAO.listarUsuarios();
            request.setAttribute("lista", lista);
            RequestDispatcher dispatcher = request.getRequestDispatcher("usuarios.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al listar usuarios", e);
            request.setAttribute("error", "Error al cargar la lista de usuarios");
            RequestDispatcher dispatcher = request.getRequestDispatcher("usuarios.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Cargar niveles de acceso para el select
            List<NivelAcceso> niveles = usuarioDAO.listarNivelesAcceso();
            request.setAttribute("nivelesAcceso", niveles);
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al mostrar formulario nuevo", e);
            response.sendRedirect("UsuarioServlet?accion=listar");
        }
    }

    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmarPassword = request.getParameter("confirmar_password");
            
            // Validaciones básicas
            if (username == null || username.trim().isEmpty()) {
                mostrarError(request, response, "El nombre de usuario es requerido", null);
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                mostrarError(request, response, "La contraseña es requerida", null);
                return;
            }
            
            if (confirmarPassword == null || !password.equals(confirmarPassword)) {
                mostrarError(request, response, "Las contraseñas no coinciden", null);
                return;
            }
            
            if (password.length() < 6) {
                mostrarError(request, response, "La contraseña debe tener al menos 6 caracteres", null);
                return;
            }
            
            // Verificar si el username ya existe
            if (usuarioDAO.existeUsername(username.trim())) {
                mostrarError(request, response, "El nombre de usuario ya existe. Por favor, elija otro.", null);
                return;
            }
            
            Usuario usr = obtenerUsuarioDesdeFormulario(request);
            if (usr == null) {
                mostrarError(request, response, "Error al procesar los datos del formulario", null);
                return;
            }
            
            boolean resultado = usuarioDAO.insertarUsuario(usr);
            
            if (resultado) {
                response.sendRedirect("UsuarioServlet?accion=listar&success=Usuario registrado exitosamente");
            } else {
                mostrarError(request, response, "Error al registrar el usuario", usr);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al insertar usuario", e);
            mostrarError(request, response, "Error interno al registrar el usuario", null);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        String idStr = request.getParameter("id");
        String mensaje = "";
        
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                
                // Verificar que no sea el usuario actual (si tienes sesión)
                
            } catch (NumberFormatException e) {
                mensaje = "error=ID de usuario inválido";
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al eliminar usuario", e);
                mensaje = "error=Error al eliminar el usuario";
            }
        } else {
            mensaje = "error=ID de usuario no proporcionado";
        }
        
        response.sendRedirect("UsuarioServlet?accion=listar&" + mensaje);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                if (usr != null) {
                    // Cargar niveles de acceso para el select
                    List<NivelAcceso> niveles = usuarioDAO.listarNivelesAcceso();
                    request.setAttribute("nivelesAcceso", niveles);
                    request.setAttribute("Usuario", usr);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("UsuarioServlet?accion=listar&error=Usuario no encontrado");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            }
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario no proporcionado");
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        String idStr = request.getParameter("id_usuario");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario no proporcionado");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            String username = request.getParameter("username");
            
            // Verificar si el username ya existe para otro usuario
            if (usuarioDAO.existeUsernameParaActualizar(username, id)) {
                Usuario usr = obtenerUsuarioDesdeFormulario(request);
                usr.setId_usuario(id);
                List<NivelAcceso> niveles = usuarioDAO.listarNivelesAcceso();
                request.setAttribute("nivelesAcceso", niveles);
                request.setAttribute("error", "El nombre de usuario ya existe. Por favor, elija otro.");
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            Usuario usr = obtenerUsuarioDesdeFormulario(request);
            usr.setId_usuario(id);
            boolean resultado = usuarioDAO.actualizarUsuario(usr);
            
            if (resultado) {
                response.sendRedirect("UsuarioServlet?accion=listar&success=Usuario actualizado exitosamente");
            } else {
                List<NivelAcceso> niveles = usuarioDAO.listarNivelesAcceso();
                request.setAttribute("nivelesAcceso", niveles);
                request.setAttribute("error", "Error al actualizar el usuario");
                request.setAttribute("Usuario", usr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar usuario", e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=Error interno al actualizar");
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
                    response.sendRedirect("UsuarioServlet?accion=listar&error=Usuario no encontrado");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            }
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario no proporcionado");
        }
    }

    private void actualizarPassword(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario no proporcionado");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            // Validaciones
            if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
                mostrarErrorPassword(request, response, id, "La nueva contraseña es requerida");
                return;
            }
            
            if (nuevaPassword.length() < 6) {
                mostrarErrorPassword(request, response, id, "La contraseña debe tener al menos 6 caracteres");
                return;
            }
            
            if (!nuevaPassword.equals(confirmarPassword)) {
                mostrarErrorPassword(request, response, id, "Las contraseñas no coinciden");
                return;
            }
            
            boolean resultado = usuarioDAO.actualizarPassword(id, nuevaPassword);
            
            if (resultado) {
                response.sendRedirect("UsuarioServlet?accion=listar&success=Contraseña actualizada exitosamente");
            } else {
                mostrarErrorPassword(request, response, id, "Error al actualizar la contraseña");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar password", e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=Error interno al actualizar contraseña");
        }
    }

    private void verUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
                if (usr != null) {
                    request.setAttribute("Usuario", usr);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("verUsuario.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("UsuarioServlet?accion=listar&error=Usuario no encontrado");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario inválido");
            }
        } else {
            response.sendRedirect("UsuarioServlet?accion=listar&error=ID de usuario no proporcionado");
        }
    }

    private Usuario obtenerUsuarioDesdeFormulario(HttpServletRequest request) {
        try {
            Usuario usr = new Usuario();
            
            // Validar y convertir id_nivel_usuario
            String idNivelStr = request.getParameter("id_nivel_usuario");
            if (idNivelStr != null && !idNivelStr.trim().isEmpty()) {
                try {
                    usr.setId_nivel_usuario(Integer.parseInt(idNivelStr.trim()));
                } catch (NumberFormatException e) {
                    logger.warning("Error al convertir id_nivel_usuario: " + idNivelStr);
                    return null;
                }
            } else {
                logger.warning("id_nivel_usuario no proporcionado");
                return null;
            }
            
            // Validar campos requeridos
            String username = request.getParameter("username");
            String nombres = request.getParameter("nombres_usuario");
            String apellidos = request.getParameter("apellidos_usuario");
            String correo = request.getParameter("correo_usuario");
            String telefono = request.getParameter("telefono_usuario");
            
            if (username == null || username.trim().isEmpty() ||
                nombres == null || nombres.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty()) {
                return null;
            }
            
            usr.setUsername(username.trim());
            usr.setNombres_usuario(nombres.trim());
            usr.setApellidos_usuario(apellidos.trim());
            usr.setCorreo_usuario(correo.trim());
            usr.setTelefono_usuario(telefono.trim());
            
            // Solo establecer password si viene del formulario de inserción
            String password = request.getParameter("password");
            if (password != null && !password.trim().isEmpty()) {
                usr.setPassword(password);
            }

            return usr;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener usuario desde formulario", e);
            return null;
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, 
                             String mensaje, Usuario usuario) throws ServletException, IOException {
        try {
            List<NivelAcceso> niveles = usuarioDAO.listarNivelesAcceso();
            request.setAttribute("nivelesAcceso", niveles);
            request.setAttribute("error", mensaje);
            if (usuario != null) {
                request.setAttribute("Usuario", usuario);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al mostrar error", e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=" + mensaje);
        }
    }

    private void mostrarErrorPassword(HttpServletRequest request, HttpServletResponse response, 
                                    int id, String mensaje) throws ServletException, IOException {
        try {
            Usuario usr = usuarioDAO.obtenerUsuarioPorId(id);
            request.setAttribute("error", mensaje);
            request.setAttribute("Usuario", usr);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cambiarPassword.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al mostrar error de password", e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=" + mensaje);
        }
    }

    @Override
    public void destroy() {
        if (usuarioDAO != null) {
            usuarioDAO.cerrarConexion();
        }
    }
}
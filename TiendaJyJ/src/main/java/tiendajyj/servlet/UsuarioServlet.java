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
    private static final Logger logger = Logger.getLogger(UsuarioServlet.class.getName());

    // doPost para insertar y actualizar
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar codificación UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        logger.info("Acción POST recibida: " + accion);
        
        UsuarioDAO usuarioDAO = null;
        try {
            usuarioDAO = new UsuarioDAO();
            
            if ("insertar".equals(accion)) {
                insertarUsuario(request, response, usuarioDAO);
            } else if ("actualizar".equals(accion)) {
                actualizarUsuario(request, response, usuarioDAO);
            } else {
                logger.warning("Acción POST no reconocida: " + accion);
                response.sendRedirect("UsuarioServlet?accion=listar");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error de SQL en doPost", e);
            throw new ServletException("Error de base de datos", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error general en doPost", e);
            throw new ServletException("Error procesando solicitud", e);
        } finally {
            if (usuarioDAO != null) {
                try {
                    usuarioDAO.cerrarConexion();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Error cerrando conexión", e);
                }
            }
        }
    }

    // doGet para listar, mostrar formulario nuevo, eliminar y editar
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar codificación UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";
        
        logger.info("Acción GET recibida: " + accion);
        
        UsuarioDAO usuarioDAO = null;
        try {
            usuarioDAO = new UsuarioDAO();
            
            switch (accion) {
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response, usuarioDAO);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response, usuarioDAO);
                    break;
                case "listar":
                default:
                    listarUsuarios(request, response, usuarioDAO);
                    break;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error de SQL en doGet", e);
            throw new ServletException("Error de base de datos", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error general en doGet", e);
            throw new ServletException("Error procesando solicitud", e);
        } finally {
            if (usuarioDAO != null) {
                try {
                    usuarioDAO.cerrarConexion();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Error cerrando conexión", e);
                }
            }
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws ServletException, IOException, SQLException {
        
        logger.info("Listando usuarios...");
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
        logger.info("Usuarios encontrados: " + (listaUsuarios != null ? listaUsuarios.size() : 0));
        
        request.setAttribute("lista", listaUsuarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("usuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("Mostrando formulario nuevo usuario");
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws SQLException, IOException {
        
        logger.info("Insertando nuevo usuario...");
        
        try {
            Usuario usuario = obtenerUsuarioDesdeFormulario(request);
            logger.log(Level.INFO, "Usuario a insertar: {0}", usuario.toString());
            
            boolean exito = usuarioDAO.insertarUsuario(usuario);
            
            if (exito) {
                logger.info("Usuario insertado exitosamente");
                response.sendRedirect("UsuarioServlet?accion=listar&success=insert");
            } else {
                logger.warning("Fallo al insertar usuario");
                response.sendRedirect("UsuarioServlet?accion=nuevo&error=insert");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al insertar usuario", e);
            response.sendRedirect("UsuarioServlet?accion=nuevo&error=insert");
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws SQLException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            logger.warning("ID de usuario no proporcionado para eliminar");
            response.sendRedirect("UsuarioServlet?accion=listar&error=no_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            logger.info("Eliminando usuario con ID: " + id);
            
            boolean exito = usuarioDAO.eliminarUsuario(id);
            
            if (exito) {
                logger.info("Usuario eliminado exitosamente");
                response.sendRedirect("UsuarioServlet?accion=listar&success=delete");
            } else {
                logger.warning("Fallo al eliminar usuario");
                response.sendRedirect("UsuarioServlet?accion=listar&error=delete");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID de usuario inválido: " + idParam, e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=invalid_id");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws SQLException, ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            logger.warning("ID de usuario no proporcionado para editar");
            response.sendRedirect("UsuarioServlet?accion=listar&error=no_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            logger.info("Cargando usuario para editar con ID: " + id);
            
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
            
            if (usuario != null) {
                logger.info("Usuario encontrado para editar: " + usuario.getUsername());
                request.setAttribute("usuario", usuario);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuario.jsp");
                dispatcher.forward(request, response);
            } else {
                logger.warning("Usuario no encontrado con ID: " + id);
                response.sendRedirect("UsuarioServlet?accion=listar&error=not_found");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID de usuario inválido: " + idParam, e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=invalid_id");
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws SQLException, IOException {
        
        String idParam = request.getParameter("id_usuario");
        if (idParam == null || idParam.trim().isEmpty()) {
            logger.warning("ID de usuario no proporcionado para actualizar");
            response.sendRedirect("UsuarioServlet?accion=listar&error=no_id");
            return;
        }
        
        try {
            Usuario usuario = obtenerUsuarioDesdeFormulario(request);
            int id = Integer.parseInt(idParam);
            usuario.setIdUsuario(id);
            
            logger.info("Actualizando usuario con ID: " + id);
            
            boolean exito = usuarioDAO.actualizarUsuario(usuario);
            
            if (exito) {
                logger.info("Usuario actualizado exitosamente");
                response.sendRedirect("UsuarioServlet?accion=listar&success=update");
            } else {
                logger.warning("Fallo al actualizar usuario");
                response.sendRedirect("UsuarioServlet?accion=editar&id=" + id + "&error=update");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID de usuario inválido: " + idParam, e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=invalid_id");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar usuario", e);
            response.sendRedirect("UsuarioServlet?accion=listar&error=update");
        }
    }

    private Usuario obtenerUsuarioDesdeFormulario(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        
        try {
            // Validar y obtener parámetros
            String nivelUsuarioStr = request.getParameter("id_nivel_usuario");
            if (nivelUsuarioStr != null && !nivelUsuarioStr.trim().isEmpty()) {
                usuario.setIdNivelUsuario(Integer.parseInt(nivelUsuarioStr));
            }
            
            usuario.setUsername(obtenerParametroSeguro(request, "username"));
            usuario.setNombresUsuario(obtenerParametroSeguro(request, "nombres_usuario"));
            usuario.setApellidosUsuario(obtenerParametroSeguro(request, "apellidos_usuario"));
            usuario.setCorreoUsuario(obtenerParametroSeguro(request, "correo_usuario"));
            usuario.setTelefonoUsuario(obtenerParametroSeguro(request, "telefono_usuario"));
            usuario.setPassword(obtenerParametroSeguro(request, "password"));
            
            logger.info("Usuario creado desde formulario: " + usuario.toString());
            
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Error al parsear nivel de usuario", e);
            throw new IllegalArgumentException("Nivel de usuario inválido", e);
        }
        
        return usuario;
    }
    
    private String obtenerParametroSeguro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor != null ? valor.trim() : "";
    }
}
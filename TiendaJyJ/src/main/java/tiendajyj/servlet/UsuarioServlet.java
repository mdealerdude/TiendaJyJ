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
                throw new ServletException(e);
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
                throw new ServletException(e);
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
                throws SQLException, IOException {
            Usuario usuario = obtenerUsuarioDesdeFormulario(request);
            boolean exito = usuarioDAO.insertarUsuario(usuario);
            if (exito) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            } else {
                response.sendRedirect("UsuarioServlet?accion=nuevo&error=insert");
            }
        }

        private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
                throws SQLException, IOException {
            int id = Integer.parseInt(request.getParameter("id"));
            usuarioDAO.eliminarUsuario(id);
            response.sendRedirect("UsuarioServlet?accion=listar");
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
                throws SQLException, IOException {
            Usuario usuario = obtenerUsuarioDesdeFormulario(request);
            usuario.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
            boolean exito = usuarioDAO.actualizarUsuario(usuario);
            if (exito) {
                response.sendRedirect("UsuarioServlet?accion=listar");
            } else {
                response.sendRedirect("UsuarioServlet?accion=editar&id=" + usuario.getIdUsuario() + "&error=update");
            }
        }

        private Usuario obtenerUsuarioDesdeFormulario(HttpServletRequest request) {
            Usuario usuario = new Usuario();
            usuario.setIdNivelUsuario(Integer.parseInt(request.getParameter("id_nivel_usuario")));
            usuario.setUsername(request.getParameter("username"));
            usuario.setNombresUsuario(request.getParameter("nombres_usuario"));
            usuario.setApellidosUsuario(request.getParameter("apellidos_usuario"));
            usuario.setCorreoUsuario(request.getParameter("correo_usuario"));
            usuario.setTelefonoUsuario(request.getParameter("telefono_usuario"));
            usuario.setPassword(request.getParameter("password")); // Aquí puedes encriptar si quieres
            return usuario;
        }
    } 

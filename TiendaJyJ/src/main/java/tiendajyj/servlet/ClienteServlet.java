/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package tiendajyj.servlet;

import tiendajyj.dao.ClienteDao;
import tiendajyj.model.Cliente;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private ClienteDao ClienteDao;

    @Override
    public void init() {
        try {
            ClienteDao = new ClienteDao();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                    insertarCliente(request, response);
                    break;
                case "eliminar":
                    eliminarCliente(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "actualizar":
                    actualizarCliente(request, response);
                    break;
                default:
                    listarClientes(request, response);
                    break;
            }
        } catch (ServletException | IOException | SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Cliente> listaClientes = ClienteDao.listarClientes();
        request.setAttribute("lista", listaClientes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("clientes.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroCliente.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Cliente cli = obtenerClienteDesdeFormulario(request);
        ClienteDao.insertarCliente(cli);
        response.sendRedirect("ClienteServlet?accion=listar");
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
       ClienteDao.eliminarCliente(id);
        response.sendRedirect("ClienteServlet?accion=listar");
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cliente cli = ClienteDao.obtenerClientePorId(id);
        request.setAttribute("Cliente", cli);
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroCliente.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Cliente cli = obtenerClienteDesdeFormulario(request);
        cli.setId_cliente(Integer.parseInt(request.getParameter("id_cliente")));
        ClienteDao.actualizarCliente(cli);
        response.sendRedirect("ClienteServlet?accion=listar");
    }

    private Cliente obtenerClienteDesdeFormulario(HttpServletRequest request) {
        Cliente cli = new Cliente();
        cli.setNombres_cliente(request.getParameter("nombres_cliente"));
        cli.setApellidos_cliente(request.getParameter("apellidos_cliente"));
        cli.setDui(request.getParameter("dui"));
        cli.setTelefono_cliente(request.getParameter("telefono_cliente"));
        cli.setCorreo_cliente(request.getParameter("correo_cliente"));
        
        // Manejo de fechas automático
        cli.setFecha_inserccion_cliente(new Date());
        cli.setFecha_actualizacion_cliente(new Date());
        
        // Si tienes usuarios en sesión, deberías obtener el ID así:
        // HttpSession session = request.getSession();
        // cli.setId_usuario((Integer) session.getAttribute("id_usuario"));
        
        // Valor temporal mientras implementas usuarios:
        cli.setId_usuario(1); // Reemplazar con lógica real de usuario
        
        return cli;
    }
}
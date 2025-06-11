package tiendajyj.servlet;

import tiendajyj.dao.EmpleadoDAO;
import tiendajyj.model.Empleado;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet("/EmpleadoServlet")

public class EmpleadoServlet extends HttpServlet {

    private EmpleadoDAO empleadoDAO;

    @Override
    public void init() {
        try {
            empleadoDAO = new EmpleadoDAO();
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                    insertarEmpleado(request, response);
                    break;
                case "eliminar":
                    eliminarEmpleado(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "actualizar":
                    actualizarEmpleado(request, response);
                    break;
                default:
                    listarEmpleados(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        request.setAttribute("lista", empleadoDAO.listarEmpleados());
        RequestDispatcher dispatcher = request.getRequestDispatcher("empleados.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroEmpleado.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Empleado emp = obtenerEmpleadoDesdeFormulario(request);
        empleadoDAO.insertarEmpleado(emp);
        response.sendRedirect("EmpleadoServlet?accion=listar");
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        empleadoDAO.eliminarEmpleado(id);
        response.sendRedirect("EmpleadoServlet?accion=listar");
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Empleado emp = empleadoDAO.obtenerEmpleadoPorId(id);
        request.setAttribute("Empleado", emp);
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroEmpleado.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Empleado emp = obtenerEmpleadoDesdeFormulario(request);
        emp.setId(Integer.parseInt(request.getParameter("id")));
        empleadoDAO.actualizarEmpleado(emp);
        response.sendRedirect("EmpleadoServlet?accion=listar");
    }

    private Empleado obtenerEmpleadoDesdeFormulario(HttpServletRequest request) throws ParseException {
        Empleado emp = new Empleado();
        emp.setNombreEmpleado(request.getParameter("nombreEmpleado"));
        emp.setApellidoEmpleado(request.getParameter("apellidoEmpleado"));
        emp.setDui(request.getParameter("dui"));
        emp.setTelefono(request.getParameter("telefono"));
        emp.setDireccion(request.getParameter("direccion"));
        emp.setCorreo(request.getParameter("correo"));
        emp.setCargo(request.getParameter("cargo"));
        
        String fechaStr = request.getParameter("fecha_ingreso");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = sdf.parse(fechaStr);
        emp.setFechaIngreso(new java.sql.Date(fecha.getTime()));

        return emp;
    }
}

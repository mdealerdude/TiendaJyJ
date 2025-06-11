/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package tiendajyj.servlet;

import tiendajyj.dao.VentaDao;
import tiendajyj.model.Venta;
import tiendajyj.model.Cliente;
import tiendajyj.model.Producto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

@WebServlet("/VentaServlet")
public class VentaServlet extends HttpServlet {

    private VentaDao ventaDao;

    @Override
    public void init() {
        try {
            ventaDao = new VentaDao();
        } catch (SQLException ex) {
            Logger.getLogger(VentaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                    insertarVenta(request, response);
                    break;
                case "eliminar":
                    eliminarVenta(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "actualizar":
                    actualizarVenta(request, response);
                    break;
                case "obtenerPrecio":
                    obtenerPrecioProducto(request, response);
                    break;
                default:
                    listarVentas(request, response);
                    break;
            }
        } catch (ServletException | IOException | SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarVentas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Venta> listaVentas = ventaDao.listarVentas();
        request.setAttribute("lista", listaVentas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("ventas.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar listas para los selects
        List<Cliente> clientes = ventaDao.listarClientes();
        List<Producto> productos = ventaDao.listarProductos();
        
        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroVenta.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarVenta(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        Venta venta = obtenerVentaDesdeFormulario(request);
        
        // Calcular el total
        BigDecimal precio = ventaDao.obtenerPrecioProducto(venta.getId_producto());
        BigDecimal cantidad = new BigDecimal(venta.getCantidad_producto());
        BigDecimal total = precio.multiply(cantidad);
        venta.setTotal(total);
        
        // Insertar venta
        boolean ventaInsertada = ventaDao.insertarVenta(venta);
        
        // Si la venta se insertó correctamente, actualizar el stock
        if (ventaInsertada) {
            ventaDao.actualizarStockProducto(venta.getId_producto(), venta.getCantidad_producto());
        }
        
        response.sendRedirect("VentaServlet?accion=listar");
    }

    private void eliminarVenta(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ventaDao.eliminarVenta(id);
        response.sendRedirect("VentaServlet?accion=listar");
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Venta venta = ventaDao.obtenerVentaPorId(id);
        
        // Cargar listas para los selects
        List<Cliente> clientes = ventaDao.listarClientes();
        List<Producto> productos = ventaDao.listarProductos();
        
        request.setAttribute("Venta", venta);
        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroVenta.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarVenta(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Venta venta = obtenerVentaDesdeFormulario(request);
        venta.setId_venta(Integer.parseInt(request.getParameter("id_venta")));
        
        // Calcular el total
        BigDecimal precio = ventaDao.obtenerPrecioProducto(venta.getId_producto());
        BigDecimal cantidad = new BigDecimal(venta.getCantidad_producto());
        BigDecimal total = precio.multiply(cantidad);
        venta.setTotal(total);
        
        ventaDao.actualizarVenta(venta);
        response.sendRedirect("VentaServlet?accion=listar");
    }

    private void obtenerPrecioProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        BigDecimal precio = ventaDao.obtenerPrecioProducto(idProducto);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"precio\": " + precio + "}");
    }

    private Venta obtenerVentaDesdeFormulario(HttpServletRequest request) {
        Venta venta = new Venta();
        venta.setId_cliente(Integer.parseInt(request.getParameter("id_cliente")));
        venta.setId_producto(Integer.parseInt(request.getParameter("id_producto")));
        venta.setCantidad_producto(Integer.parseInt(request.getParameter("cantidad_producto")));
        
        // Manejo de fechas automático
        venta.setFecha_inserccion_venta(new Date());
        venta.setFecha_actualizacion_venta(new Date());
        
        // Si tienes usuarios en sesión, deberías obtener el ID así:
        HttpSession session = request.getSession();
        venta.setId_usuario((Integer) session.getAttribute("id_usuario"));
        
        // Valor temporal mientras se implementa usuarios:
        //venta.setId_usuario(1); // Reemplazar con lógica real de usuario
        
        return venta;
    }
}
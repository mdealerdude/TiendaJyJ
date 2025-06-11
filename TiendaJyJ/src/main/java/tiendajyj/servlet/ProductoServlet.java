package tiendajyj.servlet;

import tiendajyj.dao.ProductoDao;
import tiendajyj.dao.ProductoDao.Marca;
import tiendajyj.model.Producto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() {
        try {
            productoDao = new ProductoDao();
        } catch (SQLException ex) {
            Logger.getLogger(ProductoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                    insertarProducto(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "actualizar":
                    actualizarProducto(request, response);
                    break;
                case "buscar":
                    buscarProductos(request, response);
                    break;
                case "porMarca":
                    listarProductosPorMarca(request, response);
                    break;
                case "verificarStock":
                    verificarStock(request, response);
                    break;
                default:
                    listarProductos(request, response);
                    break;
            }
        } catch (ServletException | IOException | SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Producto> listaProductos = productoDao.listarProductos();
        request.setAttribute("lista", listaProductos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("productos.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar lista de marcas para el select
        List<Marca> marcas = productoDao.listarMarcas();
        request.setAttribute("marcas", marcas);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroProducto.jsp");
        dispatcher.forward(request, response);
    }

    private void insertarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        Producto producto = obtenerProductoDesdeFormulario(request);
        
        // Establecer fechas
        Date ahora = new Date();
        producto.setFechaInserccionProducto(new Timestamp(ahora.getTime()));
        producto.setFechaActualizacionProducto(new Timestamp(ahora.getTime()));
        
        // Insertar producto
        boolean productoInsertado = productoDao.insertarProducto(producto);
        
        if (productoInsertado) {
            request.getSession().setAttribute("mensaje", "Producto registrado exitosamente");
            request.getSession().setAttribute("tipoMensaje", "success");
        } else {
            request.getSession().setAttribute("mensaje", "Error al registrar el producto");
            request.getSession().setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ProductoServlet?accion=listar");
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        boolean eliminado = productoDao.eliminarProducto(id);
        
        if (eliminado) {
            request.getSession().setAttribute("mensaje", "Producto eliminado exitosamente");
            request.getSession().setAttribute("tipoMensaje", "success");
        } else {
            request.getSession().setAttribute("mensaje", "Error al eliminar el producto");
            request.getSession().setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ProductoServlet?accion=listar");
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Producto producto = productoDao.obtenerProductoPorId(id);
        
        // Cargar lista de marcas para el select
        List<Marca> marcas = productoDao.listarMarcas();
        
        request.setAttribute("Producto", producto);
        request.setAttribute("marcas", marcas);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroProducto.jsp");
        dispatcher.forward(request, response);
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Producto producto = obtenerProductoDesdeFormulario(request);
        producto.setIdProducto(Integer.parseInt(request.getParameter("id_producto")));
        
        // Establecer fecha de actualización
        producto.setFechaActualizacionProducto(new Timestamp(new Date().getTime()));
        
        // Obtener la fecha de inserción original
        Producto productoOriginal = productoDao.obtenerProductoPorId(producto.getIdProducto());
        if (productoOriginal != null) {
            producto.setFechaInserccionProducto(productoOriginal.getFechaInserccionProducto());
        }
        
        boolean actualizado = productoDao.actualizarProducto(producto);
        
        if (actualizado) {
            request.getSession().setAttribute("mensaje", "Producto actualizado exitosamente");
            request.getSession().setAttribute("tipoMensaje", "success");
        } else {
            request.getSession().setAttribute("mensaje", "Error al actualizar el producto");
            request.getSession().setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("ProductoServlet?accion=listar");
    }

    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String termino = request.getParameter("termino");
        
        if (termino != null && !termino.trim().isEmpty()) {
            List<Producto> listaProductos = productoDao.buscarProductos(termino);
            request.setAttribute("lista", listaProductos);
            request.setAttribute("terminoBusqueda", termino);
        } else {
            List<Producto> listaProductos = productoDao.listarProductos();
            request.setAttribute("lista", listaProductos);
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("productos.jsp");
        dispatcher.forward(request, response);
    }

    private void listarProductosPorMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idMarca = Integer.parseInt(request.getParameter("idMarca"));
        
        List<Producto> listaProductos = productoDao.listarProductosPorMarca(idMarca);
        List<Marca> marcas = productoDao.listarMarcas();
        
        request.setAttribute("lista", listaProductos);
        request.setAttribute("marcas", marcas);
        request.setAttribute("marcaSeleccionada", idMarca);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("productos.jsp");
        dispatcher.forward(request, response);
    }

    private void verificarStock(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        
        boolean stockDisponible = productoDao.verificarStockDisponible(idProducto, cantidad);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"stockDisponible\": " + stockDisponible + "}");
    }

    private Producto obtenerProductoDesdeFormulario(HttpServletRequest request) {
    Producto producto = new Producto();
    
    // Obtener parámetros del formulario
    String nombreProducto = request.getParameter("nombre_producto");
    String idMarcaStr = request.getParameter("id_marca");
    String stockStr = request.getParameter("stock_producto");
    String precioStr = request.getParameter("precio_producto");
    
    // Validar y establecer valores
    if (nombreProducto != null && !nombreProducto.trim().isEmpty()) {
        producto.setNombreProducto(nombreProducto.trim());
    }
    
    if (idMarcaStr != null && !idMarcaStr.trim().isEmpty()) {
        try {
            producto.setIdMarca(Integer.parseInt(idMarcaStr));
        } catch (NumberFormatException e) {
            producto.setIdMarca(0);
        }
    }
    
    if (stockStr != null && !stockStr.trim().isEmpty()) {
        try {
            producto.setStockProducto(Integer.parseInt(stockStr));
        } catch (NumberFormatException e) {
            producto.setStockProducto(0);
        }
    }
    
    if (precioStr != null && !precioStr.trim().isEmpty()) {
        try {
            producto.setPrecioProducto(new BigDecimal(precioStr));
        } catch (NumberFormatException e) {
            producto.setPrecioProducto(BigDecimal.ZERO);
        }
    }
    
    // Obtener usuario de la sesión con validación
    HttpSession session = request.getSession();
    Integer idUsuario = (Integer) session.getAttribute("id_usuario");
    
    if (idUsuario != null) {
        producto.setIdUsuario(idUsuario);
    } else {
        // Si no hay usuario en sesión, usar un valor por defecto o lanzar excepción
        // Opción 1: Usar valor por defecto (temporal)
        producto.setIdUsuario(1);
        
        // Opción 2: Lanzar excepción (recomendado para producción)
        // throw new IllegalStateException("Usuario no autenticado. Debe iniciar sesión.");
    }
    
    return producto;
}

    @Override
    public void destroy() {
        // Limpiar recursos si es necesario
        super.destroy();
    }
}
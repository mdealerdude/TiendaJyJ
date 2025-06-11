package tiendajyj.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet { 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Conexion con = new Conexion();
            Connection conn = con.getConnection();

            // Modificar la consulta para obtener también el id_usuario
            String sql = "SELECT id_usuario, username FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession(true);
                // Guardar tanto el username como el id_usuario en la sesión
                session.setAttribute("USER", username);
                session.setAttribute("id_usuario", rs.getInt("id_usuario"));
                response.sendRedirect("principal.jsp");
            } else {
                response.sendRedirect("index.jsp?error=1");
            }

            rs.close();
            ps.close();
            con.cerrarConexion();

        } catch (SQLException e) {
            throw new ServletException("Error al conectar con la base de datos", e);
        }
    }
}
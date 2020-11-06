import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = "/Login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                DBManager db = new DBManager();
                db.getConnection();
                if(db.validateLogin(req.getParameter("username"), req.getParameter("password"))){
                    Cookie loginCookie = new Cookie("user", req.getParameter("username"));
                    loginCookie.setMaxAge(5);
                    resp.addCookie(loginCookie);
                    resp.sendRedirect("home.jsp");
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
                    PrintWriter out = resp.getWriter();
                    out.println("<font color=red>Wrong username or password.</font>");
                    rd.include(req, resp);
                }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

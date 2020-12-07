import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBManager db = new DBManager();
        db.getConnection();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (db.validateLogin(username,password)){
            HttpSession session = req.getSession();
            session.setAttribute("loggedInUser", true);
            session.setAttribute("username", req.getParameter("username"));
            session.setAttribute("visibility", db.getUserVisibility(username));
            session.setAttribute("allVisibilities", db.getAllVisibilities(db.getUserVisibility(username)));
            resp.sendRedirect("/app/home");

        } else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
            if (req.getParameter("username") != null || req.getParameter("password") != null) {
                PrintWriter out = resp.getWriter();
                out.println("<div class='alert alert-danger' role='alert'> Wrong username or password " +
                        "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
                        " <span aria-hidden='true'>&times;</span></button></div> ");
            }
            rd.include(req, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

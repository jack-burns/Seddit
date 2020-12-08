import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserManagerFactory factory = UserManagerFactory.getInstance();
        userManagerInterface userManager = factory.createUserManager();
        if (userManager.authenticateUser(username,password)){
            HttpSession session = req.getSession();
            session.setAttribute("loggedInUser", true);
            session.setAttribute("username", userManager.getUsername());
            session.setAttribute("visibility", userManager.getUserGroup());
            session.setAttribute("allVisibilities", userManager.getAllGroups());
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

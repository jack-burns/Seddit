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
        DBManager db = new DBManager();
        db.getConnection();
        if (db.validateLogin(req.getParameter("username"), req.getParameter("password"))) {
            HttpSession session = req.getSession();
            session.setAttribute("loggedInUser", true);

//USER AUTH WITH SESSION
//                    HttpSession session = req.getSession();
//                    session.setAttribute("username", req.getParameter("username"));
//                    req.setAttribute("username", req.getParameter("username"));
//                    Populate with UserPosts
//                    req.setAttribute("UserPosts", db.getUserPosts());
//                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
//                    rd.forward(req,resp);


            Cookie loginCookie = new Cookie("user", req.getParameter("username"));
            loginCookie.setMaxAge(30 * 60);
            loginCookie.setPath("/");
            resp.addCookie(loginCookie);
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

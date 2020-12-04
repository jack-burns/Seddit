import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        session.invalidate();
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
        PrintWriter out = resp.getWriter();
        out.println("<div class='alert alert-success' role='alert' style='margin: 0;'>Successfully Logged Out</div> ");
        resp.setHeader("refresh", "3;url="+req.getContextPath()+"/login");
        rd.include(req, resp);
    }

}

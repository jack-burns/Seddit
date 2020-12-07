import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeletionManager", urlPatterns = "/app/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/app/home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // check that user is actually an admin (only admins can delete posts)
        HttpSession session = req.getSession();
        if(session.getAttribute("visibility").equals("admin")) {
            int postID = Integer.parseInt(req.getParameter("postID"));
            DBManager db = new DBManager();
            if(!db.deletePost(postID)) {
                PrintWriter writer = resp.getWriter();
                writer.write("<H1>Deletion failed!</H1>");
                writer.flush();
            }
        }
        resp.sendRedirect("/app/home");
    }
}

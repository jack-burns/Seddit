import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeletionManager", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postID = Integer.parseInt(req.getParameter("postID"));

        DBManager db = new DBManager();
        if(db.deletePost(postID)){
            resp.sendRedirect("home");
        }
        else{
            PrintWriter writer = resp.getWriter();
            writer.write("<H1>Deletion failed!</H1>");
            writer.flush();
        }
    }
}

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="ModifyPost", urlPatterns = "/modifyPost")
public class ModifyPostServlet extends ModifyServlet{

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DBManager db = new DBManager();
        int postID = (Integer) req.getSession().getAttribute("postID");

        if(req.getParameter("modifyPost")!=null) {

            String newContent = req.getParameter("content");
            String newTitle = req.getParameter("title");

            if(!db.modifyPost(postID, newTitle, newContent)){
                PrintWriter writer = resp.getWriter();
                writer.write("<H1>Update failed!</H1>");
                writer.flush();
            }
        }

        if(req.getParameter("deletePost")!=null){
            if(!db.deletePost(postID)){
                PrintWriter writer = resp.getWriter();
                writer.write("<H1>Deletion failed!</H1>");
                writer.flush();
            }
        }

        resp.sendRedirect("home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}

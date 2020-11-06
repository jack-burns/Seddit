import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="PostManager", urlPatterns = "/home")
public class PostManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DBManager db = new DBManager();
        db.getConnection();

        //Populate with UserPosts
        req.setAttribute("UserPosts", db.getUserPosts());
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
        rd.forward(req,resp);
    }
}

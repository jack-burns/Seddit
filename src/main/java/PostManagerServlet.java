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
        int viewCount = 10;

        if(req.getParameter("viewposts")!=null){
            try {
                viewCount = Integer.parseInt(req.getParameter("viewposts"));
            }catch (NumberFormatException e){
                viewCount = -1;
            }
        }

        //Populate with UserPosts
        req.setAttribute("UserPosts", db.getUserPosts(viewCount));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
        rd.forward(req,resp);
    }
}

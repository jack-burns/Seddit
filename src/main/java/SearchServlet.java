import dao.UserPost;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name="RedirectToSearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");//can only search one username
        String hashtag = req.getParameter("hashtag");//can search multiple hashtags with OR logical relation
        String fromDate = req.getParameter("from");//either from or to can be missing
        String toDate = req.getParameter("to");

        DBManager db = new DBManager();
        ArrayList<UserPost> searchResult = db.searchPost(username, hashtag, fromDate, toDate);

        req.setAttribute("UserPosts", searchResult);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");
        rd.forward(req,resp);

    }
}

import dao.UserPost;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RedirectToSearchServlet", urlPatterns = "/app/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/app/search.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");//can only search one username
        String hashtag = req.getParameter("hashtag");//can search multiple hashtags with OR logical relation
        String fromDate = req.getParameter("from");//either from or to can be missing
        String toDate = req.getParameter("to");
        String group = req.getParameter("group");
        System.out.println("from date is: " + fromDate + "\n" + "to date is: " + toDate);

        DBManager db = new DBManager();
        ArrayList<UserPost> searchResult = db.searchPost(username, hashtag, fromDate, toDate, group);

        req.setAttribute("UserPosts", searchResult);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/app/search.jsp");
        rd.forward(req, resp);

    }
}

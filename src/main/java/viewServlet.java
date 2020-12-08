import dao.UserPost;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "viewServlet", urlPatterns = "/app/templateView")
public class viewServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBManager db = new DBManager();
        db.getConnection();

        int userPostID = Integer.parseInt(req.getParameter("postID"));
        UserPost userPost = db.getUserPost(userPostID);
        int fileID = userPost.getFileAttachment().getId();

        //get UserPost
        req.getSession().setAttribute("UserPost", userPost);
        req.getSession().setAttribute("postID", userPostID);
        req.getSession().setAttribute("fileID", fileID);

        //send client to post modification page


        //get FileAttachment
//        req.setAttribute("FileAttachment", db.get);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/app/templateView.jsp");
        rd.forward(req, resp);
    }
}

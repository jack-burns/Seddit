import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name="PostManager", urlPatterns = "/app/home")
@javax.servlet.annotation.MultipartConfig
public class PostManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBManager db = new DBManager();
        db.getConnection();

        //getting username from cookie
        String username = "";
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {//can probably clean up using stream
                Cookie cookie = cookies[i];
                if ((cookie.getName()).equals("user")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        Part filePart = req.getPart("file");


        //sending message to DB with title, content, posterID, and current system time
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        db.postMessage(title, content, username, filePart);
        resp.sendRedirect("/app/home");


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
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/app/home.jsp");
        rd.forward(req,resp);
    }
}

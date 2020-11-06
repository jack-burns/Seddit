import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="PostManager", urlPatterns = "/home")
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

        //getting userID from DB using username
        int userID = db.getUserID(username);

        PrintWriter out= resp.getWriter();
        if(userID != -1){
            //sending message to DB with title, content, posterID, and current system time
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            if(db.postMessage(title, content, username)){
                out.println("<h1>Message posting successful!</h1>");
            }
        }
        else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/home");
            out.println("<h1>Something Went Wrong</h1>");
            rd.include(req, resp);
        }
        resp.sendRedirect("home");


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

import dao.UserPost;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ModifyServlet", urlPatterns = "/app/modify")
public class ModifyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

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

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/app/modify.jsp");
        rd.forward(req, resp);
    }

    /*
    private String formatDate(String date){//this ended up not being needed but can be useful if we ever need to parse the post timestamp received from the jsp page
        String parameterFormat = "EEE MMM dd hh:mm:ss Z yyyy";//I don't know where Dominik specified this but this is what I'm getting from JSP request
        String dbFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(parameterFormat);
        try{
            Date tempDate = formatter.parse(date);
            formatter = new SimpleDateFormat(dbFormat);
            return formatter.format(tempDate);
        }
        catch (ParseException e){
            e.printStackTrace();
            return "";
        }
    }
     */
}
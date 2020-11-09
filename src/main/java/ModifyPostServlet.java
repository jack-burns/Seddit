import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@WebServlet(name="ModificationManager", urlPatterns = "/modify")
public class ModifyPostServlet extends  HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postID = Integer.parseInt(req.getParameter("postID"));
        String newContent = req.getParameter("newContent");
        String newTitle = req.getParameter("newTitle");

        DBManager db = new DBManager();
        boolean modStatus = db.modifyPost(postID, newTitle, newContent);

        if(modStatus){
            //it seems like writing to httpresponse interferes with redirection, for some reason
            resp.sendRedirect("home");
        }
        else{
            PrintWriter writer = resp.getWriter();
            writer.write("<H1>Update failed!</H1>");
            writer.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //send client to post modification page
        RequestDispatcher dispatchToModifyPage = getServletContext().getRequestDispatcher("/modify_comment.jsp");
        dispatchToModifyPage.forward(req, resp);
    }

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
}
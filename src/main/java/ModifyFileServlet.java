import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="ModifyFile", urlPatterns = "/modifyFile")
@javax.servlet.annotation.MultipartConfig
public class ModifyFileServlet extends ModifyServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DBManager db = new DBManager();
        int fileID = (Integer) req.getSession().getAttribute("fileID");

        Part filePart = req.getPart("file");



        if(filePart!=null){
            if(fileID!=0){

                if(!db.modifyFile(filePart, fileID)){
                    PrintWriter writer = resp.getWriter();
                    writer.write("<H1>Update file failed!</H1>");
                    writer.flush();
                }
            }else{

                if(!db.postFile(filePart, (Integer) req.getSession().getAttribute("postID"))){
                    PrintWriter writer = resp.getWriter();
                    writer.write("<H1>Post new file failed!</H1>");
                    writer.flush();
                }

            }
        }

        if(req.getParameter("deleteFile")!=null){
            if(!db.deleteFile(fileID)){
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

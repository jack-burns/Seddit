import dao.FileAttachment;
import dao.UserPost;
import org.apache.taglibs.standard.tag.common.core.ImportSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@WebServlet(name = "XMLDownloadServlet", urlPatterns = "/app/download2")
public class XMLDownloadServlet extends HttpServlet {
    private final int ARBITARY_SIZE = 1048;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        DBManager db = new DBManager();

        int userPostID = Integer.parseInt(req.getParameter("downloadXML"));
        UserPost userPost = db.getUserPost(userPostID);
        int fileID = userPost.getFileAttachment().getId();

        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment; filename=\"XMLoutput.txt\"");
        try {
            OutputStream outputStream = resp.getOutputStream();
            String outputResult = "<post>\n" +
                    "\t<id> " + userPost.getPostID() + " </id>\n" +
                    "\t<author> " + userPost.getUsername() + " </author>\n" +
                    "\t<created> " + userPost.getCreate_timestamp() + " </created>\n";
            if (userPost.getModified_timestamp()!= null){
                outputResult += "\t<updated> " + userPost.getModified_timestamp() + " </updated>\n";
            }

            outputResult += "\t<Group> " + userPost.getGroup() +" </group>\n" +
                            "\t<body> " + userPost.getContent() + " </body>\n" +
                    "</post>";
            outputStream.write(outputResult.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



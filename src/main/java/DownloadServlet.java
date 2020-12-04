import dao.FileAttachment;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;

@WebServlet(name = "DownloadServlet", urlPatterns = "/app/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {


        DBManager db = new DBManager();

        FileAttachment fileAttachment = db.getFileAttachment(Integer.parseInt(req.getParameter("fileID")));

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {

            String fileName = fileAttachment.getName();

            String resContentType = "application/octet-stream";
            resp.setContentType(resContentType);

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            resp.setHeader(headerKey, headerValue);


            InputStream in = fileAttachment.getData().getBinaryStream();

            if (in == null) {
                System.out.println("file not found according to id");
                return;
            }
            for (int i = in.read(); i != -1; i = in.read()) {
                out.write(i);
            }

            in.close();
            out.close();

        } catch (Exception e) {
            System.out.println("problem occurs");
        }
















    }
}

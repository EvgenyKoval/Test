package server;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ragvalod on 12.06.16.
 */
public class Servlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String partNumber = request.getParameter("number");
        String hash = request.getParameter("hash");
        String data = request.getParameter("data");

        try {
            Boolean isLast = Boolean.valueOf(request.getParameter("isLast"));

            data = Base64.base64Decode(data);

            if (hash.equals(DigestUtils.md5Hex(data))) {
                Saver saver = new Saver(partNumber, data, isLast);
                saver.savePart();

            } else {
                response.sendError(400, "REPEAT");
            }
        } catch (NullPointerException e) {
            response.sendError(400, "REPEAT");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        printWriter.write("<h1>use POST method</h1>");
    }
}

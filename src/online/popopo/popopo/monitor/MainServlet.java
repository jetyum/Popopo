package online.popopo.popopo.monitor;

import org.eclipse.jetty.servlet.DefaultServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends DefaultServlet {
    @Override
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res) throws IOException, ServletException {
        String path = req.getServletPath();

        if (getResource(path).exists()) {
            super.doGet(req, res);
        } else {
            res.sendRedirect("/404.html");
        }
    }
}

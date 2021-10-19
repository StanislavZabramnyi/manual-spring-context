import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/name")
public class NameServlet extends HttpServlet {
    private static final String SPRING_APP_CONTEXT = "SPRING_APP_CONTEXT";

    @Override
    public void init(ServletConfig servletConfig) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(NameProvider.class);
        servletConfig.getServletContext().setAttribute(SPRING_APP_CONTEXT, applicationContext);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var springContext = req.getServletContext().getAttribute(SPRING_APP_CONTEXT);

        var nameProviderBean = Optional.ofNullable(springContext)
                .filter(ApplicationContext.class::isInstance).map(ApplicationContext.class::cast)
                .orElseThrow(() -> new ApplicationContextException("Failed to initialize Spring context"))
                .getBean(NameProvider.class);

        resp.getWriter().println(nameProviderBean.getName());
    }
}

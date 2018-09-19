package alex.ru.filters;

import alex.ru.exceptions.UtilException;
import alex.ru.utils.rendering_adapter.ViewRender;
import alex.ru.utils.jmx.JmxContainer;
import alex.ru.utils.jmx.stopserver.StopServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StopServerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(StopServerFilter.class.getSimpleName());

    private JmxContainer jmxContainer;

    private ViewRender viewRender;

    private String errorMassage;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        final StopServer stopServerMBean = (StopServer) jmxContainer.get(StopServer.class);

        if(stopServerMBean.isStopped()) {
            try {

                final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
                final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                final ModelAndView modelAndView = new ModelAndView("opt_error.html");

                modelAndView .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                modelAndView .addObject("opt_error", errorMassage);

                viewRender.reflectiveRenderHtml(modelAndView , httpServletRequest, httpServletResponse);

            } catch (NullPointerException | UtilException e) {

                if(e.equals(NullPointerException.class)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    logger.error("MxBean error.");
                }
                else {
                    filterChain.doFilter(servletRequest, servletResponse);
                    logger.error("ViewRenderer error");
                }
                e.printStackTrace();
            }
        }
        else
            filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    public void setViewRender(ViewRender viewRender) {
        this.viewRender = viewRender;
    }

    public void setJmxContainer(final JmxContainer jmxContainer) {
        this.jmxContainer = jmxContainer;
    }

    public void setErrorMassage(final String errorMassage) {
        this.errorMassage = errorMassage;
    }

}

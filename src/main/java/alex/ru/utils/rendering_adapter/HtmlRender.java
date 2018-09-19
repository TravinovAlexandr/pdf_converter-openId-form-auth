package alex.ru.utils.rendering_adapter;

import alex.ru.exceptions.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class HtmlRender implements ViewRender {

    private DispatcherServlet dispatcherServlet;

    @Override
    public void reflectiveRenderHtml(@NonNull final ModelAndView mv,
                                     @NonNull final HttpServletRequest request,
                                     @NonNull final HttpServletResponse response) {
        try {
            final Method renderMethod = dispatcherServlet.getClass()
                    .getDeclaredMethod("render", ModelAndView.class, HttpServletRequest.class, HttpServletResponse.class);
            renderMethod.setAccessible(true);
            renderMethod.invoke(dispatcherServlet, mv, request, response);
            renderMethod.setAccessible(false);

        } catch (NullPointerException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {

            throw new UtilException(e.getClass().getSimpleName());
        }
    }

    @Autowired
    public void setDispatcherServlet(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }
}

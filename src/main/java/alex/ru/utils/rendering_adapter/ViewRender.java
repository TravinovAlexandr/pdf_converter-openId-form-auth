package alex.ru.utils.rendering_adapter;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface ViewRender {

    void reflectiveRenderHtml(final ModelAndView mv, final HttpServletRequest request, final HttpServletResponse response);
}

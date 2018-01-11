package ywd.cache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import ywd.util.PrintUtils;

/**
 * Cache Builder
 * Initialize properties.
 * Read properties from property.json
 * 
 * @author dreamingodd
 *
 */
@SuppressWarnings("serial")
public class CacheInitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            // Property initialization
            String webinfPath = getServletContext().getRealPath("/") + "/WEB-INF";
            Property.init(webinfPath);

            // Expenditure and Category Cache initialization
            CategoryCache.getCache();
            MonthlyExpenditureCache.getCache();
            PrintUtils.print(this.getClass(), "Category cache & expend data cache are ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

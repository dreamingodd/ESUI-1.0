package ywd.action;

import java.util.List;
import java.util.Map;

import ywd.cache.CategoryCache;
import ywd.cache.MonthlyExpenditureCache;
import ywd.entity.ExpendCategory;
import ywd.entity.MonthlyExpenditure;
import ywd.exception.YwdException;
import ywd.service.ExpendChartService;
import ywd.util.PrintUtils;

/**
 * 
 * @author dreamingodd Created @2015年1月25日
 */
public class ExpendChartAction extends Action {

    private ExpendChartService service = new ExpendChartService();

    private String output;

    public String exe() {
        try {
            PrintUtils.print(this.getClass(), "Begin");
            List<MonthlyExpenditure> monthlyExpends = MonthlyExpenditureCache
                    .getCache();
            Map<String, ExpendCategory> cates = CategoryCache.getCache();
            output = service.allExpendJson(cates, monthlyExpends);
            PrintUtils.print(this.getClass(), "End");
        } catch (Exception e) {
            PrintUtils.error(this.getClass(), new YwdException(e));
            return ERROR;
        }
        return SUCCESS;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}

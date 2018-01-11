package ywd.action;

import java.util.List;
import java.util.Map;

import ywd.cache.MonthlyExpenditureCache;
import ywd.entity.ExpendCategory;
import ywd.entity.Expenditure;
import ywd.entity.MonthlyExpenditure;
import ywd.util.CalendarUtils;
import ywd.util.PrintUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExpendToJsonAction extends Action {

    private Map<String, ExpendCategory> cates;

    private List<MonthlyExpenditure> monthlyExpends;

    private JSONArray monthlyExpendsJson;

    /**
     * Convert monthlyExpends to a big JSON array.
     */
    public String ajaj() {
        try {
            monthlyExpends = MonthlyExpenditureCache.getCache();
            monthlyExpendsJson = new JSONArray();
            for (MonthlyExpenditure monthlyExpend : monthlyExpends) {
                JSONObject monthlyExpendJson = new JSONObject();
                JSONArray cateExpendsJson = new JSONArray();
                JSONArray statExpendsJson = new JSONArray();
                monthlyExpendJson.put("total", monthlyExpend.total());
                monthlyExpendJson.put(
                        "date",
                        CalendarUtils.toString(CalendarUtils.YM,
                                monthlyExpend.getDate()));
                monthlyExpendJson.put("expenditures", cateExpendsJson);
                monthlyExpendJson.put("statisticalExpenditures",
                        statExpendsJson);
                monthlyExpendsJson.add(monthlyExpendJson);
                // expenditures - category : list[expend]
                for (String cateName : monthlyExpend.getExpenditures().keySet()) {
                    JSONArray expendsJson = new JSONArray();
                    for (Expenditure expend : monthlyExpend.getExpenditures()
                            .get(cateName)) {
                        JSONObject expendJson = new JSONObject();
                        expendJson.put("category", expend.getCategory()
                                .getName());
                        expendJson.put("date", CalendarUtils.toString(
                                CalendarUtils.YMD, expend.getDate()));
                        expendJson.put("name", expend.getName());
                        expendJson.put("howMuch", expend.getHowMuch());
                        expendsJson.add(expendJson);
                    }
                    cateExpendsJson.add(expendsJson);
                }
                // statisticalExpenditures - category : statistical expend
                for (String cateName : monthlyExpend
                        .getStatisticalExpenditures().keySet()) {
                    Expenditure statExpend = monthlyExpend
                            .getStatisticalExpenditures().get(cateName);
                    JSONObject statExpendJson = new JSONObject();
                    statExpendJson.put("category", statExpend.getCategory()
                            .getName());
                    statExpendJson.put("date", CalendarUtils.toString(
                            CalendarUtils.YMD, statExpend.getDate()));
                    statExpendJson.put("howMuch", statExpend.getHowMuch());
                    statExpendJson.put("average", statExpend.getAverage());
                    statExpendsJson.add(statExpendJson);
                }
            }
        } catch (Exception e) {
            PrintUtils.error(this.getClass(), e);
            return ERROR;
        }
        return SUCCESS;
    }

    public Map<String, ExpendCategory> getCates() {
        return cates;
    }

    public void setCates(Map<String, ExpendCategory> cates) {
        this.cates = cates;
    }

    public List<MonthlyExpenditure> getMonthlyExpends() {
        return monthlyExpends;
    }

    public void setMonthlyExpends(List<MonthlyExpenditure> monthlyExpends) {
        this.monthlyExpends = monthlyExpends;
    }

    public JSONArray getMonthlyExpendsJson() {
        return monthlyExpendsJson;
    }

    public void setMonthlyExpendsJson(JSONArray monthlyExpendsJson) {
        this.monthlyExpendsJson = monthlyExpendsJson;
    }
}

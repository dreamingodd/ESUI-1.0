package ywd.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author dreamingodd
 * 
 */
public class MonthlyExpenditure {

    private Date date;
    /** Normal expenditure */
    private Map<String, List<Expenditure>> expenditures;
    /** Simply last 2 rows of the sheet */
    private Map<String, Expenditure> statisticalExpenditures;

    public MonthlyExpenditure() {
    }

    public MonthlyExpenditure(Date date) {
        this.date = date;
        this.expenditures = new HashMap<String, List<Expenditure>>();
        this.statisticalExpenditures = new HashMap<String, Expenditure>();
    }

    public void addExpend(String cateName, Expenditure expend) {
        if (this.expenditures.containsKey(cateName)) {
            this.expenditures.get(cateName).add(expend);
        } else {
            List<Expenditure> list = new ArrayList<Expenditure>();
            this.expenditures.put(cateName, list);
            list.add(expend);
        }
    }

    public void addMonthExpend(String cateName, Expenditure expend) {
        this.statisticalExpenditures.put(cateName, expend);
    }

    public double total() {
        double total = 0;
        for (Expenditure expend : this.statisticalExpenditures.values()) {
            total += expend.getHowMuch();
        }
        return total;
    }

    /**
     * Sometimes I add expenditures in the future such as rent, parking fee. So
     * I use eating expend and income as standards of being empty.
     */
    public boolean isEmpty() {
        ExpendCategory cate1 = new ExpendCategory("必要食品");
        return statisticalExpenditures.get(cate1.getName()).getHowMuch() == 0;
    }

    public double average() {
        return total() / 12;
    }

    public void minusSpecialFromStat() {
        for (String cateName : this.statisticalExpenditures.keySet()) {
            Expenditure specialExpend = this.statisticalExpenditures
                    .get(cateName);
            // Is special.
            if (specialExpend.getCategory().getLevel() == 2) {
                Expenditure parentExpend = this.statisticalExpenditures
                        .get(specialExpend.getCategory().getParent().getName());
                parentExpend.setHowMuch(parentExpend.getHowMuch()
                        - specialExpend.getHowMuch());
            }
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, List<Expenditure>> getExpenditures() {
        return expenditures;
    }

    public Map<String, Expenditure> getStatisticalExpenditures() {
        return statisticalExpenditures;
    }

}

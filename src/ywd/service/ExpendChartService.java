package ywd.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ywd.entity.ExpendCategory;
import ywd.entity.Expenditure;
import ywd.entity.MonthlyExpenditure;
import ywd.entity.SimpleExpend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExpendChartService {

    public String allExpendJson(Map<String, ExpendCategory> cates,
            List<MonthlyExpenditure> monthlyExpends) throws Exception {
        // It will be converted to gatherObject.
        Map<String, Map<String, SimpleExpend>> cateSimpleExpendMap = new HashMap<String, Map<String, SimpleExpend>>();
        // Put cates into cateSimpleExpendMap
        for (ExpendCategory cate : cates.values()) {
            // Exclude parent categories
            if (cate.getLevel() >= 1 && cate.isExpend()) {
                cateSimpleExpendMap.put(cate.getName(),
                        new HashMap<String, SimpleExpend>());
            }
        }
        // Put cate-expends/date-expends into cateSimpleExpendMap
        for (MonthlyExpenditure monthlyExpend : monthlyExpends) {
            Map<String, Expenditure> cateExpends = monthlyExpend
                    .getStatisticalExpenditures();
            for (String cateName : cateExpends.keySet()) {
                Expenditure statExpend = cateExpends.get(cateName);
                SimpleExpend simple = statExpend.convertToSimpleExpend();
                if (cateSimpleExpendMap.containsKey(cateName)) {
                    cateSimpleExpendMap.get(cateName).put(simple.getDateStr(),
                            simple);
                }
            }
        }
        // Sort category name in Chinese
        List<String> cateNameList = new ArrayList<String>();
        for (String cateName : cateSimpleExpendMap.keySet()) {
            cateNameList.add(cateName);
        }
        // Convert map-map to gatherObject
        JSONObject catesStatsObject = new JSONObject();
        Collections.sort(cateNameList,
                Collator.getInstance(java.util.Locale.CHINA));
        for (String cateName : cateNameList) {
            JSONObject dateExpendsObject = new JSONObject();
            catesStatsObject.put(cateName, dateExpendsObject);
            for (String dateStr : cateSimpleExpendMap.get(cateName).keySet()) {
                String simpleStatStr = JSON.toJSONString(cateSimpleExpendMap
                        .get(cateName).get(dateStr));
                dateExpendsObject.put(dateStr, simpleStatStr);
            }
        }
        return catesStatsObject.toJSONString();
    }

}

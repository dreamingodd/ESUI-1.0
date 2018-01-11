package ywd.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ywd.entity.MonthlyExpenditure;
import ywd.service.ExpenditureExcelReader;
import ywd.util.CollectionUtils;

/**
 * Expenditure Cache [Singleton] Store all the expenditure data from given
 * folder
 * 
 * @author dreamingodd
 * 
 */
public class MonthlyExpenditureCache {

    private static List<MonthlyExpenditure> cache;

    private MonthlyExpenditureCache() {
    }

    public static List<MonthlyExpenditure> getCache() throws Exception {
        if (CollectionUtils.isEmpty(cache)) {
            synchronized (MonthlyExpenditureCache.class) {
                if (CollectionUtils.isEmpty(cache)) {
                    init();
                }
            }
        }
        return cache;
    }

    private static void init() throws Exception {
        cache = new ArrayList<MonthlyExpenditure>();
        List<File> srcFiles = new ArrayList<File>();
        File srcFolder = new File(Property.getSRC_FOLDER());
        for (File file : srcFolder.listFiles()) {
            if (StringUtils.contains(file.getName(),
                    Property.getSRC_FILE_NAME_FORMAT())) {
                srcFiles.add(file);
            }
        }
        for (File file : srcFiles) {
            List<MonthlyExpenditure> yearExpends = ExpenditureExcelReader
                    .parseExcel(file, Property.getDATE_FORMAT());
            cache.addAll(yearExpends);
        }
    }

    public static void destroy() {
        cache.clear();
    }
}

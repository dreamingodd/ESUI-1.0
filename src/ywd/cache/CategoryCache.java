package ywd.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ywd.entity.ExpendCategory;
import ywd.exception.YwdSrcFileExistException;
import ywd.exception.YwdSrcFolderExistException;
import ywd.service.ExpenditureExcelReader;
import ywd.util.CollectionUtils;

/**
 * Contains all the categories, get from an excel [Singleton]
 * 
 * @author dreamingodd
 * 
 */
public class CategoryCache {
    private volatile static Map<String, ExpendCategory> cache;

    private CategoryCache() {
    }

    public static Map<String, ExpendCategory> getCache() throws Exception {
        if (CollectionUtils.isEmpty(cache)) {
            synchronized (CategoryCache.class) {
                if (CollectionUtils.isEmpty(cache)) {
                    init();
                }
            }
        }
        return cache;
    }

    public static void destory() {
        cache.clear();
    }

    /**
     * Initialize categories' cache Premises: Execute immediately after
     * Property's initialization.
     * 
     * @throws Exception
     */
    private static void init() throws Exception {
        File srcFolder = new File(Property.getSRC_FOLDER());
        if (!srcFolder.exists()) {
            throw new YwdSrcFolderExistException();
        }
        List<File> srcFiles = new ArrayList<File>();
        for (File file : srcFolder.listFiles()) {
            if (StringUtils.contains(file.getName(),
                    Property.getSRC_FILE_NAME_FORMAT())) {
                srcFiles.add(file);
            }
        }
        if (srcFiles.size() == 0) {
            throw new YwdSrcFileExistException();
        }
        cache = ExpenditureExcelReader.parseCategoriesFromFirstSheet(
                srcFiles.get(0), Property.getDATE_FORMAT());
        List<ExpendCategory> list = new ArrayList<ExpendCategory>();
        for (String cateName : Property.SPECIAL_CATEGORIES.keySet()) {
            ExpendCategory cate = Property.SPECIAL_CATEGORIES.get(cateName)
                    .convertToCategory(cache);
            list.add(cate);
        }
        for (ExpendCategory cate : list) {
            cache.put(cate.getName(), cate);
        }
    }

}

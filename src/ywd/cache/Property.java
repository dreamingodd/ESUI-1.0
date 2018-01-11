package ywd.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ywd.entity.SpecialCategory;
import ywd.exception.YwdSystemNotRecognizeException;
import ywd.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Property Initializer [Singleton]
 * 
 * 
 * @author dreamingodd
 * 
 */
@SuppressWarnings("serial")
public class Property extends HashMap<String, String> {

    private static Property property;
    public static Map<String, SpecialCategory> SPECIAL_CATEGORIES = new HashMap<String, SpecialCategory>();

    private static String SRC_FOLDER_WIN;
    private static String SRC_FOLDER_MAC;
    private static String SRC_FILE_NAME_FORMAT;
    private static String DATE_FORMAT;
    private static int YEAR_IN_FILE;

    private Property() {
    }

    // static is for new is already abandoned
    @SuppressWarnings("unchecked")
    public static Property init(String webinfPath) throws Exception {
        if (CollectionUtils.isEmpty(property)) {
            synchronized (Property.class) {
                if (CollectionUtils.isEmpty(property)) {
                    if (StringUtils.isEmpty(webinfPath)) {
                        throw new Exception(
                                "Property has not been initialized!");
                    }
                    property = new Property();
                    String propertyStr = loadProperty(webinfPath);
                    Map<String, Object> map = (Map<String, Object>) JSONObject
                            .parse(propertyStr);
                    for (String key : map.keySet()) {
                        if (key.endsWith("ARRAY")) {
                            List<JSONObject> jos = (List<JSONObject>) map
                                    .get(key);
                            for (JSONObject jo : jos) {
                                SpecialCategory cate = new SpecialCategory(jo);
                                SPECIAL_CATEGORIES.put(cate.getName(), cate);
                            }
                        } else {
                            property.put(key, (String) map.get(key));
                        }
                    }
                    SRC_FOLDER_WIN = property.get("SRC_FOLDER_WIN");
                    SRC_FOLDER_MAC = property.get("SRC_FOLDER_MAC");
                    SRC_FILE_NAME_FORMAT = property.get("SRC_FILE_NAME_FORMAT");
                    DATE_FORMAT = property.get("DATE_FORMAT");
                    YEAR_IN_FILE = Integer.parseInt(property
                            .get("YEAR_IN_FILE"));
                }
            }
        }
        return property;
    }

    public static Property getProperty() throws Exception {
        if (property == null) {
            throw new Exception("Property has not been initialized!");
        }
        return property;
    }

    public static void destroy() {
        property.clear();
        ;
    }

    private static String loadProperty(String webinfPath) throws Exception {

        BufferedReader propertyReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(webinfPath
                        + "/property.json")), "UTF-8"));
        StringBuilder propertySb = new StringBuilder();
        String line = "";
        while (line != null) {
            propertySb.append(line);
            line = propertyReader.readLine();
        }
        propertyReader.close();
        return propertySb.toString();
    }

    public static String getSRC_FOLDER() throws YwdSystemNotRecognizeException {

        String osName = System.getProperty("os.name");
        if (osName.startsWith("win") || osName.startsWith("Win")) {
            return SRC_FOLDER_WIN;
        } else if (osName.startsWith("mac") || osName.startsWith("Mac")) {
            return SRC_FOLDER_MAC;
        } else {
            throw new YwdSystemNotRecognizeException();
        }
    }

    public static String getSRC_FOLDER_WIN() {
        return SRC_FOLDER_WIN;
    }

    public static String getSRC_FOLDER_MAC() {
        return SRC_FOLDER_MAC;
    }

    public static String getSRC_FILE_NAME_FORMAT() {
        return SRC_FILE_NAME_FORMAT;
    }

    public static String getDATE_FORMAT() {
        return DATE_FORMAT;
    }

    public static int getYEAR_IN_FILE() {
        return YEAR_IN_FILE;
    }
}

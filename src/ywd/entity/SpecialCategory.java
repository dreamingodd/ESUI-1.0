package ywd.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.format.BoldStyle;
import jxl.format.Colour;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SpecialCategory {

    public static Map<String, Colour> JXL_COLOUR_MAP = new HashMap<String, Colour>();
    public static Map<String, BoldStyle> JXL_BOLD_MAP = new HashMap<String, BoldStyle>();
    static {
        JXL_COLOUR_MAP.put("red", Colour.RED);
        JXL_COLOUR_MAP.put("blue", Colour.BLUE);
        JXL_COLOUR_MAP.put("green", Colour.GREEN);
        JXL_COLOUR_MAP.put("black", Colour.BLACK);
        JXL_BOLD_MAP.put("bold", BoldStyle.BOLD);
        JXL_BOLD_MAP.put("normal", BoldStyle.NORMAL);
    }

    private String name;
    private String parentName;
    private Colour color;
    private BoldStyle bold;
    private List<String> words;

    public SpecialCategory(JSONObject jo) {
        this.name = jo.getString("name");
        this.parentName = jo.getString("parent");
        if (StringUtils.isNotEmpty(jo.getString("color"))) {
            this.color = JXL_COLOUR_MAP.get(jo.getString("color"));
        }
        if (StringUtils.isNotEmpty(jo.getString("bold"))) {
            this.bold = JXL_BOLD_MAP.get(jo.getString("bold"));
        }
        JSONArray wordArray = (JSONArray) jo.get("words");
        this.words = new ArrayList<String>();
        for (int i = 0; i < wordArray.size(); i++) {
            this.words.add((String) wordArray.get(i));
        }
        if (this.words.size() == 0) {
            this.words = null;
        }
    }

    public ExpendCategory convertToCategory(Map<String, ExpendCategory> cateMap) {
        ExpendCategory cate = new ExpendCategory();
        cate.setName(this.name);
        cate.setParent(cateMap.get(parentName));
        if (cate.getParent() == null) {
            cate.setParent(cateMap.get("收入"));
        }
        cate.setLevel(cate.getParent().getLevel() + 1);
        return cate;
    }

    public boolean checkCell(ExpendCategory cate, Cell cell) {
        if (StringUtils.isEmpty(cell.getContents())) {
            return false;
        }
        if (this.parentName != null && !cate.getName().equals(this.parentName)) {
            return false;
        } else if (this.color != null
                && !cell.getCellFormat().getFont().getColour().getDescription()
                        .equals(this.color.getDescription())) {
            return false;
        } else if (this.bold != null
                && cell.getCellFormat().getFont().getBoldWeight() != this.bold
                        .getValue()) {
            return false;
        }
        if (this.words != null && this.words.size() != 0) {
            boolean contain = false;
            for (String word : words) {
                if (cell.getContents().contains(word)) {
                    contain = true;
                }
            }
            if (!contain) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Colour getColor() {
        return color;
    }

    public void setColor(Colour color) {
        this.color = color;
    }

    public BoldStyle getBold() {
        return bold;
    }

    public void setBold(BoldStyle bold) {
        this.bold = bold;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}

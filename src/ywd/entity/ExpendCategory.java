package ywd.entity;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ywd.util.PrintUtils;

public class ExpendCategory {

    private String name;
    private ExpendCategory parent;
    private int level;
    private int rowNum;
    private int colNum;

    public ExpendCategory() { }

    public ExpendCategory(String name, ExpendCategory parent, int level,
            int rowNum, int colNum) {
        super();
        this.name = name;
        this.parent = parent;
        this.level = level;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public ExpendCategory(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        String parentName = parent == null ? "NONE" : parent.getName();
        return "Category [name=" + name + ", parent=" + parentName + ", level=" + level + "]";
    }

    public boolean isExpend() {
        if ("支出".equals(this.getName())) {
            return true;
        }
        if (this.getParent() == null) {
            return false;
        } else {
            return this.getParent().isExpend();
        }
    }

    public boolean isIncome() {
        if ( "收入".equals(this.getName())) {
            return true;
        }
        if (this.getParent() == null) {
            return false;
        } else {
            return this.getParent().isExpend();
        }
    }

    /**
     * Find parent category from a category map
     * @param categories
     */
    public void findAndSetParent(Map<String, ExpendCategory> categories) {
        if (this.getLevel() == 0) {
            PrintUtils.error(this.getClass(), this.getName() + ": This is a root category!!!");
        }
        ExpendCategory parent = new ExpendCategory();
        for (ExpendCategory cate : categories.values()) {
            if (cate.getLevel() == this.getLevel() - 1) {
                if (cate.getColNum() <= this.getColNum() && cate.getColNum() > parent.getColNum()) {
                    parent = cate;
                }
            }
        }
        if (StringUtils.isEmpty(parent.getName())) {
            PrintUtils.error(this.getClass(), this.getName() + ": Cannot find parent!!!");
        }
        this.setParent(parent);
    }

    public String getName() {
        return name;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpendCategory other = (ExpendCategory) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ExpendCategory getParent() {
        return parent;
    }
    public void setParent(ExpendCategory parent) {
        this.parent = parent;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

}

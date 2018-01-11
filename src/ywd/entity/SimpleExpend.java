package ywd.entity;

public class SimpleExpend {
    private String cateName;
    private String dateStr;
    private Double howMuch;
    @Override
    public String toString() {
        return "SimpleExpend [cateName=" + cateName + ", dateStr=" + dateStr
                + ", howMuch=" + howMuch + "]";
    }
    public String getCateName() {
        return cateName;
    }
    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
    public String getDateStr() {
        return dateStr;
    }
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
    public Double getHowMuch() {
        return howMuch;
    }
    public void setHowMuch(Double howMuch) {
        this.howMuch = howMuch;
    }
}

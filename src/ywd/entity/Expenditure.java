package ywd.entity;

import java.util.Date;
import java.util.List;

import ywd.util.CalendarUtils;

public class Expenditure {

    /** Mandatory fields */
    private Date date;
    private ExpendCategory category;
    private String name;
    private double howMuch;
    private double average;
    /** Optional fields */
    private List<Person> people;
    private String description;
    private Type type;

    public SimpleExpend convertToSimpleExpend() {
        SimpleExpend se = new SimpleExpend();
        se.setCateName(category.getName());
        se.setDateStr(CalendarUtils.toString(CalendarUtils.YM, date));
        // 保留一位小数
        howMuch = Math.round(howMuch * 10) / 10.;
        se.setHowMuch(howMuch);
        return se;
    }

    public Expenditure() {
        super();
    }

    public Expenditure(Date date, String name, ExpendCategory category, double howMuch) {
        super();
        this.date = date;
        this.name = name;
        this.category = category;
        this.howMuch = howMuch;
    }

    public Expenditure(Date date, ExpendCategory category, double howMuch, double average) {
        super();
        this.date = date;
        this.average = average;
        this.category = category;
        this.howMuch = howMuch;
    }

    public Expenditure(Date date, ExpendCategory category, String name,
            double howMuch, List<Person> people, String description, Type type) {
        super();
        this.date = date;
        this.category = category;
        this.name = name;
        this.howMuch = howMuch;
        this.people = people;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Expenditure [" + "date=" + date + ", category=" + category + ", name=" + name
                + ", howMuch=" + howMuch + "]";
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public ExpendCategory getCategory() {
        return category;
    }
    public void setCategory(ExpendCategory category) {
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getHowMuch() {
        return howMuch;
    }
    public void setHowMuch(double howMuch) {
        this.howMuch = howMuch;
    }
    public List<Person> getPeople() {
        return people;
    }
    public void setPeople(List<Person> people) {
        this.people = people;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public double getAverage() {
        return average;
    }
    public void setAverage(double average) {
        this.average = average;
    }

}

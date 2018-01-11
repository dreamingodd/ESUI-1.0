package ywd.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang3.StringUtils;

import ywd.cache.CategoryCache;
import ywd.cache.Property;
import ywd.entity.ExpendCategory;
import ywd.entity.Expenditure;
import ywd.entity.MonthlyExpenditure;
import ywd.entity.SpecialCategory;
import ywd.util.CalendarUtils;
import ywd.util.PrintUtils;

/**
 * @author dreamingodd
 * 
 */
public class ExpenditureExcelReader {

    public static final int TOTAL_ROW = 66;
    public static final int AVERAGE_ROW = 68;

    private ExpenditureExcelReader() {
    }

    /**
     * Parse the given spreadsheet, get all the data(categories and items) from
     * it. Premises: 1 sheet for 1 month.
     * 
     * @param excel
     * @return
     * @throws Exception
     */
    public static List<MonthlyExpenditure> parseExcel(File excel,
            String dateFormat) throws Exception {
        List<MonthlyExpenditure> yearExpend = new ArrayList<MonthlyExpenditure>();
        // get year date
        int yearBegin = Property.getYEAR_IN_FILE();
        String yearStr = excel.getName().substring(yearBegin, 4);

        // If just for reading, it doesn't matter whether the spreadsheet is in
        // use or not.
        Workbook wb = Workbook.getWorkbook(excel);
        // get category cache, put them into the expend instances
        Map<String, ExpendCategory> cateCache = CategoryCache.getCache();
        for (Sheet sheet : wb.getSheets()) {
            MonthlyExpenditure monthExpend = parseSheet(sheet, cateCache,
                    dateFormat, yearStr);
            if (!monthExpend.isEmpty()) {
                // Minus ones of category level 2 from those from level 1
                monthExpend.minusSpecialFromStat();
                yearExpend.add(monthExpend);
            }
        }
        /**
         * Date date = CalendarUtils.getDate(2014, 9); for (MonthlyExpenditure
         * monthExpend : yearExpend) { if (monthExpend.getDate().getTime() ==
         * date.getTime()) { // Expend for (ExpendCategory cate :
         * monthExpend.getExpenditures().keySet()) { for (Expenditure expend :
         * monthExpend.getExpenditures().get(cate)) {
         * PrintUtils.print(ExpenditureExcelReader.class,
         * expend.getCategory().getName() + ": " + expend.getName() + "- " +
         * expend.getHowMuch()); } } // Month Statistics for (Expenditure expend
         * : monthExpend.getStatisticalExpenditures().values()) {
         * PrintUtils.print(ExpenditureExcelReader.class,
         * expend.getCategory().getName() + ": " + expend.getHowMuch()); } } }
         */
        wb.close();
        return yearExpend;
    }

    /**
     * Parse the first sheet of the given excel file, get all the category
     * hierarchy. Premises: all sheets' categories are the same.
     * 
     * @param excel
     * @param dateFormat
     * @return
     * @throws Exception
     */
    public static Map<String, ExpendCategory> parseCategoriesFromFirstSheet(
            File excel, String dateFormat) throws Exception {
        Workbook wb = Workbook.getWorkbook(excel);
        Map<String, ExpendCategory> categories = parseCategoriesFromSheet(
                wb.getSheet(0), dateFormat);
        wb.close();
        return categories;
    }

    /**
     * Parse the given sheet, get all the categories. I make it private because
     * I don't want scenarios like invoker forgetting close the workbook
     * happens, the same reason for the following methods.
     * 
     * @param sheet
     * @return all the categories from the sheet
     */
    private static Map<String, ExpendCategory> parseCategoriesFromSheet(
            Sheet sheet, String dateFormat) throws Exception {
        // jxl : index begin with 0, column comes first, secondary is row.
        // VBA : begin with 1, row comes first.
        Map<String, ExpendCategory> categories = new HashMap<String, ExpendCategory>();
        int rowCount = sheet.getRows();
        int colCount = sheet.getColumns();
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            if (Pattern.matches(dateFormat, sheet.getCell(0, rowNum)
                    .getContents()))
                break;
            for (int colNum = 0; colNum < colCount; colNum++) {
                String categoryName = sheet.getCell(colNum, rowNum)
                        .getContents();
                if (StringUtils.isNotEmpty(categoryName)) {
                    ExpendCategory category = new ExpendCategory(categoryName,
                            null, rowNum, rowNum, colNum);
                    // sub category has parent category
                    if (category.getLevel() != 0) {
                        category.findAndSetParent(categories);
                    }
                    categories.put(categoryName, category);
                }
            }
        }
        return categories;
    }

    /**
     * Parse the given sheet, get all the data.
     * 
     * @param sheet
     * @return expenditures of a month
     */
    private static MonthlyExpenditure parseSheet(Sheet sheet,
            Map<String, ExpendCategory> cateCache, String dateFormat,
            String yearStr) throws Exception {
        Date monthDate = CalendarUtils
                .parseExcelMonth(sheet.getName(), yearStr);
        MonthlyExpenditure monthExpend = new MonthlyExpenditure(monthDate);
        // 1.Gather the standard categories, column number as its key
        Map<Integer, ExpendCategory> standardCates = new HashMap<Integer, ExpendCategory>();
        for (ExpendCategory cate : cateCache.values()) {
            if (cate.getLevel() == 1)
                standardCates.put(cate.getColNum(), cate);
        }

        // 2.Gather the special categories.
        Map<String, Double> specialCateStatMap = new HashMap<String, Double>();
        for (String cateName : Property.SPECIAL_CATEGORIES.keySet()) {
            specialCateStatMap.put(cateName, 0.);
        }

        // 3.Collect expenditure data
        // // Get begin row
        int rowCount = sheet.getRows();
        int colCount = sheet.getColumns();
        int beginRow = 0;
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            if (Pattern.matches(dateFormat, sheet.getCell(0, rowNum)
                    .getContents())) {
                beginRow = rowNum;
                break;
            }
        }

        // // Iterate all over the sheet
        for (int colNum = 1; colNum < 15; colNum++) {
            ExpendCategory cate = standardCates.get(colNum);
            // Extract single expend
            for (int rowNum = beginRow; rowNum < rowCount; rowNum += 2) {
                // The date cell is empty
                if (StringUtils.isEmpty(sheet.getCell(0, rowNum).getContents()))
                    break;
                // Either name or howMuch is not empty
                String howMuchStr = sheet.getCell(colNum, rowNum).getContents()
                        .trim();
                String desc = sheet.getCell(colNum, rowNum + 1).getContents()
                        .trim();
                String dateStr = sheet.getCell(0, rowNum).getContents().trim();
                // If howMuch is not a Number
                if (StringUtils.isNotEmpty(howMuchStr)
                        && !sheet.getCell(colNum, rowNum).getType()
                                .equals(CellType.NUMBER)) {
                    PrintUtils.error(ExpenditureExcelReader.class, yearStr
                            + "年" + sheet.getName() + ": " + (rowNum + 1) + "-"
                            + (colNum + 1) + " is not a Number !");
                    continue;
                }
                if (StringUtils.isNotEmpty(desc)
                        || StringUtils.isNotEmpty(howMuchStr)) {
                    Expenditure expend = new Expenditure();
                    Date date = CalendarUtils.parseExcelDate(dateStr, yearStr);
                    Cell descCell = sheet.getCell(colNum, rowNum + 1);
                    boolean isSpecial = false;
                    for (SpecialCategory spCate : Property.SPECIAL_CATEGORIES
                            .values()) {
                        if (spCate.checkCell(cate, descCell)) {
                            isSpecial = true;
                            ExpendCategory specialCate = CategoryCache
                                    .getCache().get(spCate.getName());
                            if (StringUtils.isNotEmpty(howMuchStr)) {
                                double howMuch = Double.parseDouble(howMuchStr);
                                expend = new Expenditure(date, desc,
                                        specialCate, howMuch);
                                double stat = specialCateStatMap.get(spCate
                                        .getName()) + howMuch;
                                specialCateStatMap.put(spCate.getName(), stat);
                            } else {
                                expend = new Expenditure(date, desc,
                                        specialCate, 0);
                            }
                        }
                    }
                    if (!isSpecial) {
                        if (StringUtils.isNotEmpty(howMuchStr)) {
                            expend = new Expenditure(date, desc, cate,
                                    Double.parseDouble(howMuchStr));
                        } else {
                            expend = new Expenditure(date, desc, cate, 0);
                        }
                    }
                    monthExpend.addExpend(expend.getCategory().getName(),
                            expend);
                }
            }
            // Extract month/category statistics
            try {
                Double howMuch = Double.parseDouble(sheet
                        .getCell(colNum, TOTAL_ROW).getContents());
                Double average = Double.parseDouble(sheet.getCell(colNum,
                        AVERAGE_ROW).getContents());
                Expenditure monthStat = new Expenditure(monthDate, cate, howMuch,
                        average);
                monthExpend.addMonthExpend(monthStat.getCategory().getName(),
                        monthStat);
            } catch (Exception e) {
                PrintUtils.error(ExpenditureExcelReader.class, yearStr
                        + "年" + sheet.getName() + ": " + (66 + 1) + "-"
                        + (colNum + 1) + " is not parsable !");
                throw e;
            }
            for (String cateName : specialCateStatMap.keySet()) {
                Expenditure specialMonthStat = new Expenditure(monthDate,
                        CategoryCache.getCache().get(cateName),
                        specialCateStatMap.get(cateName), 0);
                monthExpend.addMonthExpend(cateName, specialMonthStat);
            }
        }

        return monthExpend;
    }
}

package ywd.service;

import java.io.File;

import org.junit.Test;

import ywd.cache.Property;

public class ExpenditureExcelReaderTest {

    @Test
    public void testParseExcel() throws Exception {
        // Property initialization
        String webinfPath = "V:\\workspace_eclipse\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\ExpenditureStatisticsUserInterface_1.0\\WEB-INF";
        Property.init(webinfPath);
        String excelPath = "D:\\Detail\\2013-YWD.xls";
        ExpenditureExcelReader.parseExcel(new File(excelPath), "\\d{1,2}月\\d{1,2}日");
    }
}

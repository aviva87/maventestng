package com.test.dataprovider;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import testCase.ReadFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ReadExcelUtil {

    /**
     * 读取excel操作
     *
     * @param filePath
     * @return:读取excel，返回map对象集合
     */
    public static Map<String, String> getExcuteList(String filePath) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String, String>> list = null;
        String cellData = null;
//        String columns[] = {"name", "method", "value","备注"};
        wb = readExcel(filePath);
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<Map<String, String>>();

            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();

            for (int i = 1; i < rownum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {

                        String key = String.valueOf(row.getCell(j)) ;
                        cellData = (String) getCellFormatValue(row.getCell(j+1));
                        map.put(key,cellData);
                        j++;
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        return map;
    }


    /**
     * 判断excel文件的类型
     *
     * @param filePath
     * @return
     */
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }



    public static void main(String[] args) throws Exception {
//        Object[][] objects = ExcelUtils.getData("C:\\Users\\chun\\Desktop\\测试用例.xls", "Sheet1");
        Map<String, String> map =  ReadExcelUtil.getExcuteList(ReadFile.class.getClassLoader().getResource("testCase.xls").getPath());
        System.out.println(JsonUtils.toJSONString(map));



    }
}



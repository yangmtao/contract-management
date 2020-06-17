package com.bj.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author: lmf
 * @Date: 2019/3/29 10:54
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    String[] titles = new String[]{"一级分类", "二级分类", "商品名称", "商品编号", "规格", "区域", "市场价", "成本价"};

    public static void importExcelData(InputStream is, String excelFileName, Map<Integer, String> cellField, List<Object> objList, String className) throws Exception {
        //List<KfsProductPriceVo> KfsProPriceVoList = new ArrayList<KfsProductPriceVo>();
        //解析excel文件内容
        Class<?> clazz = Class.forName("com.bj.business.model." + className);
        Workbook workbook = createWorkbook(is, excelFileName);
        Sheet sheet = getSheet(workbook, 0);
        //获取sheet中数据的行数
        int rows = sheet.getPhysicalNumberOfRows();
        boolean islastrow = false;
        int i = 1;
        //迭代记录
        while (i <= rows && !islastrow) {
            Row row = sheet.getRow(i);
            if (row != null && i <= (rows - 1)) {
                Object tempBean = clazz.newInstance();
                Map<String, String> tempMap = new HashMap<String, String>();
                for (Map.Entry<Integer, String> entry : cellField.entrySet()) {
                    //取得excel读取模版信息
                    Cell cellRent = row.getCell(entry.getKey());
                    if (cellRent != null && !"".equals(cellRent.toString())) {
                        //rowRentType = cellRent.toString();
                        tempMap.put(entry.getValue(), cellRent.toString());
                    }
                }
                setFieldValue(tempBean, tempMap);
                objList.add(tempBean);
            } else {
                //判断row是否为空如果为空那么停止解析，代表已尽读取完有效数据
                islastrow = true;
            }
            i++;
        }

    }


    /**
     * set属性的值到Bean
     *
     * @param bean
     * @param valMap
     */
    public static Object setFieldValue(Object bean, Map<String, String> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field
                        .getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        if (value.indexOf(".") != -1) {
                            value = value.substring(0, value.indexOf("."));
                        }
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
                        BigDecimal temp = new BigDecimal(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return bean;

    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods
     * @param fieldSetMet
     * @return boolean
     */
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {

                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 生成workbook
     *
     * @param is
     * @param excelFileName
     * @return
     * @throws IOException
     */
    public static Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * 获取sheet
     *
     * @param workbook
     * @param sheetIndex
     * @return
     */
    public static Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }


}


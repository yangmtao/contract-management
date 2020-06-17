package com.bj.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * desc:表单验证
 *
 * @author zhph
 * @date 2019/8/24  14:51
 */
public class FormValid {
    /**
     * 带区号的电话号码验证的表达式
     */
    private static Pattern PATTERN_MOBILE_CODE = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
    /**
     * 不带区号的电话号码验证的表达式
     */
    private static Pattern PATTERN_MOBILE = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
    /**
     * 手机号码验证的表达式
     */
    private static Pattern PATTERN_PHONE = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$");
    /**
     * 邮箱验证的表达式
     */
    private static Pattern PATTERN_EMAIL = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    /**
     * 判断数字型字符串的小数点后是否最多有2位数字
     */
    private static Pattern PATTERN_TWO_DECIMAL = Pattern.compile("^\\d*([.]\\d{0,2})?$");

    private static Pattern PATTERN_TWO_DECIMAL_OTHER = Pattern.compile("^([+-])?\\d*([.]\\d{0,2})?$");

    /**
     * 判断数字型字符串是否是大于等于0的整数的正则表达式
     */
    private static Pattern PATTERN_INTEGER = Pattern.compile("^\\+?[0-9][0-9]*$");

    /**
     * 判断字符串是否只有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    /**
     * 判断字符串是否是数值
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        //Pattern pattern = Pattern.compile("^-?[0-9]+"); //这个也行
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");//这个也行
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     * @author ：shijing
     * 2016年12月5日下午4:34:46
     */
    public static boolean isMobile(final String str) {
        Matcher m = null;
        boolean b = false;
        // 验证手机号
        m = PATTERN_PHONE.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     * @author ：shijing
     * 2016年12月5日下午4:34:21
     */

    public static boolean isPhone(final String str) {
        Matcher m = null;
        boolean b = false;
        if (str.length() > 9) {
            m = PATTERN_MOBILE_CODE.matcher(str);
            b = m.matches();
        } else {
            m = PATTERN_MOBILE.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Matcher m = PATTERN_EMAIL.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断小数点后是否是2位
     *
     * @param decimalStr
     * @return
     */
    public static boolean isTwoDecimal(String decimalStr) {
        Matcher match = PATTERN_TWO_DECIMAL.matcher(decimalStr);
        boolean bo = match.matches();
        return bo;
    }

    /**
     * 判断小数点后是否是2位
     *
     * @param decimalStr
     * @return
     */
    public static boolean isTwoDecimalOther(String decimalStr) {
        Matcher match = PATTERN_TWO_DECIMAL_OTHER.matcher(decimalStr);
        boolean bo = match.matches();
        return bo;
    }

    /**
     * 判断数字字符串是否是大于等于0的整数
     *
     * @param integerStr
     * @return
     */
    public static boolean isInteger(String integerStr) {
        Matcher match = PATTERN_INTEGER.matcher(integerStr);
        boolean bo = match.matches();
        return bo;
    }

    /**
     * 判断BigDecimal类型的值是否发生变化
     *
     * @param decimalBefore
     * @param decimalAfter
     * @return
     */
    public static boolean checkBigDecimalChange(BigDecimal decimalBefore, BigDecimal decimalAfter) {
        if (decimalBefore == null && decimalAfter != null) {
            return true;
        }
        if (decimalBefore != null && decimalAfter == null) {
            return true;
        }
        if (decimalBefore != null && decimalAfter != null
                && decimalBefore.doubleValue() != decimalAfter.doubleValue()) {
            return true;
        }
        return false;
    }

    /**
     * 判断Integer类型的值是否发生变化
     *
     * @param integerBefore
     * @param integerAfter
     * @return
     */
    public static boolean checkIntegerChange(Integer integerBefore, Integer integerAfter) {
        if (integerBefore == null && integerAfter != null) {
            return true;
        }
        if (integerBefore != null && integerAfter == null) {
            return true;
        }
        if (integerBefore != null && integerAfter != null
                && integerBefore.intValue() != integerAfter.intValue()) {
            return true;
        }
        return false;
    }

    /**
     * 判断String类型的值是否发生变化
     *
     * @param stringBefore
     * @param stringAfter
     * @return
     */
    public static boolean checkStringChange(String stringBefore, String stringAfter) {
        if (StringUtils.isBlank(stringBefore) && StringUtils.isNotBlank(stringAfter)) {
            return true;
        }
        if (StringUtils.isNotBlank(stringBefore) && StringUtils.isBlank(stringAfter)) {
            return true;
        }
        if (StringUtils.isNotBlank(stringBefore) && StringUtils.isNotBlank(stringAfter)
                && !stringBefore.equals(stringAfter)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> uuidList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        for(int i=0;i<100000000;i++){
            uuidList.add(UUID.randomUUID().toString().replace("-",""));
        }
        for(int i=0;i<100000000;i++){
            map.put(uuidList.get(i),uuidList.get(i));
            map2.put(uuidList.get(i).substring(15,31),uuidList.get(i).substring(15,31));
        }
        System.out.println(map.size()+"===="+map2.size());
        System.out.println(map2.entrySet().iterator().next().getKey());
    }
}

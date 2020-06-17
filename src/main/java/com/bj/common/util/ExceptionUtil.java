package com.bj.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * desc:
 *
 * @author zhph
 * @date 2020/3/23  16:54
 */
public class ExceptionUtil {
    /**
     * 获取异常的详细信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionAllInformation(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}

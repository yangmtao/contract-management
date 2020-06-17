
package com.bj.common.validator;

import com.bj.common.exception.RRException;
import org.apache.commons.lang3.StringUtils;

//数据校验
public abstract class Assert {

    public static void isBlank(String str, String message) throws Exception {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) throws Exception {
        if (object == null) {
            throw new RRException(message);
        }
    }
}

package com.bj.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by you on 2017/12/15.
 */
public class ConvertUtil {
    public static Map<String, Object> objectToMap(Object obj) {
        if(obj == null) {
            return null;
        } else {
            HashMap map = new HashMap();

            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                PropertyDescriptor[] var4 = propertyDescriptors;
                int var5 = propertyDescriptors.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    PropertyDescriptor property = var4[var6];
                    String key = property.getName();
                    if(key.compareToIgnoreCase("class") != 0) {
                        key = key.replaceAll("([A-Z])","_$1").toLowerCase();
                        Method getter = property.getReadMethod();
                        Object value = getter != null?getter.invoke(obj, new Object[0]):null;
                        if(value != null) {
                            map.put(key, value.toString());
                        }
                    }
                }
            } catch (Exception var11) {
                var11.fillInStackTrace();
            }

            return map;
        }
    }
}

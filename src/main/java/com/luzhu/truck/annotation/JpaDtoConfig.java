package com.luzhu.truck.annotation;

import com.luzhu.truck.exception.JpaExceptionEnum;
import com.luzhu.truck.exception.JpaRuntimeException;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Configuration
public class JpaDtoConfig {

    private static final Logger log = LoggerFactory.getLogger(JpaDtoConfig.class);
    @Autowired
    private ApplicationContext applicationContext;

    public JpaDtoConfig() {
    }

    @PostConstruct
    public void init() {
        Map<String, Object> map = this.applicationContext.getBeansWithAnnotation(JpaDto.class);
        Iterator var2 = map.values().iterator();

        while(var2.hasNext()) {
            Object o = var2.next();
            Class c = o.getClass();
            log.info("Jpa添加Converter,class={}", c.getName());
            GenericConversionService genericConversionService = (GenericConversionService) DefaultConversionService.getSharedInstance();
            genericConversionService.addConverter(Map.class, c, (m) -> {
                try {
                    Object obj = c.newInstance();
                    return this.copyMapToObj(m, obj);
                } catch (Exception var4) {
                    throw new FatalBeanException("Jpa结果转换出错,class=" + c.getName(), var4);
                }
            });
        }

    }

    private Object copyMapToObj(Map<String, Object> map, Object target) {
        if (map != null && target != null && !map.isEmpty()) {
            Class<?> actualEditable = target.getClass();
            PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
            PropertyDescriptor[] var5 = targetPds;
            int var6 = targetPds.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                PropertyDescriptor targetPd = var5[var7];
                if (targetPd.getWriteMethod() != null) {
                    String newKey = "";

                    Object value;
                    try {
                        String key = targetPd.getName();
                        if (!map.containsKey(key)) {
                            int startIndex = 0;

                            for(int i = 0; i < key.length(); ++i) {
                                if (Character.isUpperCase(key.charAt(i))) {
                                    newKey = newKey + key.substring(startIndex, i) + "_";
                                    startIndex = i;
                                }
                            }

                            newKey = newKey + key.substring(startIndex);
                            value = map.get(newKey.toLowerCase());
                        } else {
                            value = map.get(key);
                        }

                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }

                            Object newValue = this.parseValue(value, targetPd.getPropertyType());
                            writeMethod.invoke(target, newValue);
                        }
                    } catch (IllegalArgumentException var14) {
                        value = map.get(newKey.toLowerCase());
                        log.error("[JpaDto]Dto[{}]欄位[{}]JDBC[{}]轉換DTO[{}]失敗", new Object[]{target.getClass(), newKey, value.getClass(), targetPd.getPropertyType()});
                        throw new JpaRuntimeException(JpaExceptionEnum.JDBC_CLASS_CAST_ERROR);
                    } catch (Exception var15) {
                        var15.printStackTrace();
                        throw new FatalBeanException("Could not copy properties from source to target", var15);
                    }
                }
            }

            return target;
        } else {
            return target;
        }
    }

    private Object parseValue(Object value, Class<?> clazz) {
        if (value instanceof Timestamp && clazz == String.class) {
            return DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss");
        } else if (value instanceof Time && clazz == String.class) {
            return DateFormatUtils.format((Date)value, "HH:mm:ss");
        } else if (value instanceof java.sql.Date && clazz == String.class) {
            return DateFormatUtils.format((Date)value, "yyyy-MM-dd");
        } else if (value instanceof BigDecimal && clazz == Boolean.class) {
            return new Boolean(((BigDecimal)value).intValue() != 0);
        } else {
            if (value instanceof BigInteger) {
                if (clazz == String.class) {
                    return value.toString();
                }

                if (clazz == Integer.class) {
                    return ((BigInteger)value).intValue();
                }

                if (clazz == Long.class) {
                    return ((BigInteger)value).longValue();
                }

                if (clazz == BigDecimal.class) {
                    return new BigDecimal((BigInteger)value);
                }

                if (clazz == Boolean.class) {
                    return ((BigInteger)value).longValue() == 0L ? Boolean.FALSE : Boolean.TRUE;
                }
            }

            if (value instanceof Integer) {
                if (clazz == String.class) {
                    return String.valueOf(value);
                }

                if (clazz == Long.class) {
                    return ((Integer)value).longValue();
                }

                if (clazz == BigDecimal.class) {
                    return BigDecimal.valueOf((long)(Integer)value);
                }

                if (clazz == BigInteger.class) {
                    return BigInteger.valueOf((long)(Integer)value);
                }

                if (clazz == Boolean.class) {
                    return ((Integer)value).longValue() == 0L ? Boolean.FALSE : Boolean.TRUE;
                }
            }

            if (value instanceof Long) {
                if (clazz == String.class) {
                    return String.valueOf(value);
                }

                if (clazz == BigDecimal.class) {
                    return BigDecimal.valueOf((Long)value);
                }

                if (clazz == BigInteger.class) {
                    return BigInteger.valueOf((Long)value);
                }

                if (clazz == Boolean.class) {
                    return (Long)value == 0L ? Boolean.FALSE : Boolean.TRUE;
                }
            }

            return value;
        }
    }
}

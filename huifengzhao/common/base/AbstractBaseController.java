package com.huifengzhao.common.base;

import com.huifengzhao.utils.RequestUtil;
import com.huifengzhao.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author huifengzhao
 * @ClassName AbstractBaseController
 * @date 2018/8/28
 */
public class AbstractBaseController extends CallbackController {

    public static final transient Logger log = LoggerFactory.getLogger(AbstractBaseController.class);

    public AbstractBaseController() {
    }

    @Override
    public void resetInfo() {
        super.resetInfo();
    }

    /**
     * 获取request对象
     *
     * @return request对象
     */
    protected HttpServletRequest getRequest() {
        HttpServletRequest request = getRequestAttributes().getRequest();
        Objects.requireNonNull(request, "request null");
        return request;
    }

    /**
     * 获取response对象
     *
     * @return response对象
     */
    protected HttpServletResponse getResponse() {
        HttpServletResponse response = getRequestAttributes().getResponse();
        Objects.requireNonNull(response, "response null");
        return response;
    }

    /**
     * 获取session对象
     *
     * @return session对象
     */
    protected HttpSession getSession() {
        HttpSession session = getRequestAttributes().getRequest().getSession();
        if (session == null) {
            session = BaseInterceptor.getSession();
        }

        Objects.requireNonNull(session, "session null");
        return session;
    }

    /**
     * 获取ServletRequestAttributes对象
     *
     * @return ServletRequestAttributes对象
     */
    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = null;
        try {
            attributes = RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {
            log.error("获取RequestAttributes对象失败", e);
        }

        Objects.requireNonNull(attributes);
        if (attributes instanceof ServletRequestAttributes) {
            return (ServletRequestAttributes) attributes;
        } else {
            throw new IllegalArgumentException("error");
        }
    }

    /**
     * 获取request域中的属性
     *
     * @param name 属性的name
     * @return 属性的value
     */
    protected Object getAttribute(String name) {
        return getRequestAttributes().getAttribute(name, 0);
    }

    /**
     * 设置response域中的属性
     *
     * @param name   属性的name
     * @param object 属性的value
     */
    protected void setAttribute(String name, Object object) {
        getRequestAttributes().setAttribute(name, object, 0);
    }

    /**
     * 获取请求头
     *
     * @param name 请求头的name
     * @return 请求头
     */
    protected String getHeader(String name) {
        return this.getRequest().getHeader(name);
    }

    /**
     * 在session中,根据属性的name获取指定属性的value
     *
     * @param name 属性的name
     * @return 属性的value
     */
    protected String getSessionAttribute(String name) {
        return StringUtil.convertNULL(this.getSessionAttributeObj(name));
    }

    /**
     * 获取session中指定的object对象
     *
     * @param name 对象的name
     * @return 对象的value
     */
    protected Object getSessionAttributeObj(String name) {
        return getRequestAttributes().getAttribute(name, 1);
    }

    /**
     * 根据指定的name移除指定的session
     *
     * @param name 指定的session name
     */
    protected void removeSessionAttribute(String name) {
        getRequestAttributes().removeAttribute(name, 1);
    }

    /**
     * 向session域中设置属性
     *
     * @param name   属性的name
     * @param object object类型的属性
     */
    protected void setSessionAttribute(String name, Object object) {
        getRequestAttributes().setAttribute(name, object, 1);
    }

    /**
     * 获取前台请求的参数
     *
     * @param name 参数的name
     * @return string类型的value
     */
    protected String getParameter(String name) {
        return this.getParameter(name, null);
    }

    /**
     * 获取前台请求的参数
     *
     * @param name 参数的name
     * @return string数组类型的value
     */
    protected String[] getParameters(String name) {
        return this.getRequest().getParameterValues(name);
    }

    /**
     * 获取前台请求的参数,并设置默认值,参数为空返回默认值
     *
     * @param name 参数的name
     * @param def  默认的value
     * @return 参数的value
     */
    protected String getParameter(String name, String def) {
        String value = this.getRequest().getParameter(name);
        return value == null ? def : value;
    }

    /**
     * 获取前台请求的参数,并转换为int类型,转换失败返回0
     *
     * @param name 参数的name
     * @return int类型的参数
     */
    protected int getParameterInt(String name) {
        return this.getParameterInt(name, 0);
    }

    /**
     * 获取前台请求的参数,并转为int类型,并设置默认值
     *
     * @param name 参数的name
     * @param def  默认的值
     * @return int类型的value
     */
    protected int getParameterInt(String name, int def) {
        return StringUtil.parseInt(this.getParameter(name), def);
    }

    /**
     * 获取前台请求的参数,并转为long类型,转换失败返回0
     *
     * @param name 参数的name
     * @return long类型的value
     */
    protected long getParameterLong(String name) {
        return this.getParameterLong(name, 0L);
    }

    /**
     * 获取前台请求的参数,并转为long类型,并设置默认值
     *
     * @param name 参数的name
     * @param def  默认值
     * @return long类型的参数
     */
    protected long getParameterLong(String name, long def) {
        String value = this.getParameter(name);
        if (value == null) {
            return def;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException var6) {
                return def;
            }
        }
    }



    protected Map<String, String> getRefererParameter() throws UnsupportedEncodingException {
        String referer = this.getHeader("Referer");
        return RequestUtil.convertUrlMap(referer);
    }

    protected <T> Object getObject(Class<T> tClass) throws Exception {
        Object obj = tClass.newInstance();
        this.doParameterMap(this.getRequest().getParameterMap(), obj);
        return obj;
    }

    private void doParameterMap(Map<String, String[]> parameter, Object obj) {
        Iterator<Map.Entry<String, String[]>> entries = parameter.entrySet().iterator();
        Class tClass = obj.getClass();

        while (true) {
            String key;
            Object value;
            do {
                if (!entries.hasNext()) {
                    return;
                }

                Map.Entry entry = entries.next();
                key = (String) entry.getKey();
                value = entry.getValue();
            } while (value == null);

            String[] temp = (String[])(value);
            StringBuilder stringBuffer = new StringBuilder();

            for (int i = 0; i < temp.length; ++i) {
                if (i != 0) {
                    stringBuffer.append(",");
                }

                stringBuffer.append(temp[i]);
            }

            this.setValue(tClass, obj, key, stringBuffer.toString());
        }
    }

    private void setValue(Class tClass, Object obj, String name, String value) {
        Field[] fields = tClass.getDeclaredFields();
        Class type = null;
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                type = field.getType();
                break;
            }
        }

        if (type == null) {
            Class superClass = tClass.getSuperclass();
            if (superClass != Object.class) {
                this.setValue(superClass, obj, name, value);
            }

        } else {
            try {
                Method method = getMethod(tClass, name, type);
                if (type != Integer.TYPE && type != Integer.class) {
                    if (type != Long.TYPE && type != Long.class) {
                        if (type == String.class) {
                            method.invoke(obj, value);
                        } else if (type == BigDecimal.class) {
                            method.invoke(obj, BigDecimal.valueOf(Long.parseLong(value)));
                        } else if (type != Float.TYPE && type != Float.class) {
                            if (AbstractBaseController.class.isAssignableFrom(type)) {
                                Object typeObj = type.newInstance();
                                Method setIdMethod = getMethod(typeObj.getClass(), "Id", Integer.class);

                                try {
                                    setIdMethod.invoke(typeObj, Integer.valueOf(value));
                                    method.invoke(obj, typeObj);
                                } catch (NumberFormatException ignored) {}
                            } else if (type != Double.class && type != Double.TYPE) {
                                log.error("未设置对应数据类型:" + type, new RuntimeException());
                            } else {
                                method.invoke(obj, Double.valueOf(value));
                            }
                        } else {
                            method.invoke(obj, Float.valueOf(value));
                        }
                    } else {
                        method.invoke(obj, Long.valueOf(value));
                    }
                } else {
                    method.invoke(obj, Integer.valueOf(value));
                }
            } catch (Exception var12) {
                log.error("创建对象错误", var12);
            }

        }
    }

    private static Method getMethod(Class<?> tClass, String name, Class type) throws NoSuchMethodException {
        try {
            return tClass.getDeclaredMethod(parSetName(name), type);
        } catch (NoSuchMethodException var5) {
            Class superClass = tClass.getSuperclass();
            if (superClass != Object.class) {
                return getMethod(superClass, name, type);
            } else {
                throw var5;
            }
        }
    }

    private static String parSetName(String fieldName) {
        if (null != fieldName && !"".equals(fieldName)) {
            int startIndex = 0;
            char placeholder = '_';
            if (fieldName.charAt(0) == placeholder) {
                startIndex = 1;
            }

            return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase() + fieldName.substring(startIndex + 1);
        } else {
            return null;
        }
    }

}
























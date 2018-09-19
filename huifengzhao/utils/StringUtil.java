package com.huifengzhao.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Character.UnicodeBlock;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil class
 *
 * @author huifengzhao
 * @date 2018/08/20
 */
public class StringUtil {

    private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    public StringUtil() {
    }

    /**
     * 判断char类型的参数c是否为中文
     *
     * @param c char类型的参数
     * @return true/false
     */
    public static boolean isChinese(char c) {
        UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 判断该string是不是中文
     *
     * @param strName string类型的参数
     * @return true/false
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        int var3 = ch.length;

        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 将source按照指定格式替换
     * [一-龥] 指的是所有的汉字
     *
     * @param source      需要替换的string参数
     * @param replacement 源字符串
     * @return 替换后的字符串
     */
    public static String replaceChinese(String source, String replacement) {
        if (isEmpty(source)) {
            return "";
        } else {
            replacement = convertNULL(replacement);
            String reg = "[一-龥]";
            Pattern pat = Pattern.compile(reg);
            Matcher mat = pat.matcher(source);
            return mat.replaceAll(replacement);
        }
    }

    /***
     * 判断字符串是否为空
     * @param data  字符串
     * @return true/false
     */
    public static boolean isEmpty(String data) {
        return data == null || data.length() < 1;
    }

    /**
     * 判断是否为空,并判断长度是否为len
     *
     * @param data 字符串
     * @param len  该字符串的长度
     * @return true/false
     */
    public static boolean isEmpty(String data, int len) {
        boolean flag = isEmpty(data);
        return flag || data.length() != len;
    }

    /**
     * 判断字符串是否为空,并判断该字符串的长度
     *
     * @param data      字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return true/false
     */
    public static boolean isEmpty(String data, int minLength, int maxLength) {
        if (data == null) {
            return true;
        } else if (data.isEmpty()) {
            return true;
        } else if (minLength > 0 && maxLength > 0) {
            if (minLength > maxLength) {
                throw new IllegalArgumentException(" minLength > maxLength");
            } else {
                return data.length() > maxLength || data.length() < minLength;
            }
        } else {
            throw new IllegalArgumentException(" minLength or maxLength <=0");
        }
    }

    /**
     * 修建字符串,将\\s* 全部替换为""
     *
     * @param data 需要修建的字符串
     * @return 修建后的字符串
     */
    public static String trimAll(String data) {
        return isEmpty(data) ? "" : data.replaceAll("\\s*", "");
    }

    /**
     * 将html页面处理为可存入数据库的字段
     *
     * @param input 字符串类型的页面
     * @return 可存入数据库的页面string
     */
    public static String filterHTML(String input) {
        if (input == null) {
            return null;
        } else if (input.length() == 0) {
            return input;
        } else {
            input = input.trim();
            input = input.replaceAll("<", "&lt;");
            input = input.replaceAll(">", "&gt;");
            input = input.replaceAll("'", "&#39;");
            return input;
        }
    }

    /**
     * 将存到数据库的页面编译为html页面
     *
     * @param input 从数据库中取出的页面string
     * @return 可直接显示的string
     */
    public static String compileHtml(String input) {
        if (input == null) {
            return null;
        } else if (input.length() == 0) {
            return input;
        } else {
            input = input.replaceAll("&amp;", "&");
            input = input.replaceAll("&lt;", "<");
            input = input.replaceAll("&gt;", ">");
            input = input.replaceAll("&nbsp;", " ");
            input = input.replaceAll("&#39;", "'");
            input = input.replaceAll("&quot;", "\"");
            return input.replaceAll("<br>", "\n");
        }
    }

    /**
     * 将string转换为array格式的string数组
     *
     * @param str array格式的string
     * @return string类型的数组
     */
    public static String[] stringToArray(String str) {
        return stringToArray(str, ",; \t\n");
    }

    /**
     * 根据指定的规则,将string转换为array格式的string数组
     *
     * @param str       需要被转换的string
     * @param separator 规则
     * @return string类型的数组
     */
    public static String[] stringToArray(String str, String separator) {
        if (str != null && separator != null) {
            int i = 0;
            StringTokenizer st = new StringTokenizer(str, separator);

            String[] array;
            array = new String[st.countTokens()];
            while (st.hasMoreTokens()) {
                array[i++] = st.nextToken();
            }

            return array;
        } else {
            return null;
        }
    }

    /**
     * 根据指定的规则,将string转换为array集合
     *
     * @param str       需要被转换的string
     * @param separator 规则
     * @return array集合
     */
    public static ArrayList<String> stringToArrayList(String str, String separator) {
        ArrayList<String> arr = new ArrayList<>();
        if (str != null && separator != null) {
            StringTokenizer st = new StringTokenizer(str, separator);

            while (st.hasMoreTokens()) {
                arr.add(st.nextToken());
            }

            return arr;
        } else {
            return arr;
        }
    }

    /**
     * 根据指定的规则,将string转换为int类型的数组
     *
     * @param str       需要被转换的string
     * @param separator 规则
     * @return int类型的数组
     */
    public static int[] stringToIntArray(String str, String separator) {
        if (str != null && separator != null) {
            int i = 0;
            StringTokenizer st = new StringTokenizer(str, separator);

            int[] array;
            array = new int[st.countTokens()];
            while (st.hasMoreTokens()) {
                array[i++] = parseInt(st.nextToken());
            }

            return array;
        } else {
            return null;
        }
    }

    /**
     * 将string转换为int
     *
     * @param num string类型的数字
     * @return int类型的数字
     */
    public static int parseInt(String num) {
        return parseInt((String) num, 0);
    }

    /**
     * 将Object类型的数字转换为int
     *
     * @param num string类型的数字
     * @return int类型的数字
     */
    public static int parseInt(Object num) {
        return num == null ? 0 : parseInt(num.toString(), 0);
    }

    /**
     * 将object类型的数字转换为int类型,转换失败返回默认值
     *
     * @param obj        需要转换的Object类型的数字
     * @param defaultNum 转换失败的默认返回值
     * @return int类型的数字
     */
    public static int parseInt(Object obj, int defaultNum) {
        return parseInt(convertNULL(obj), defaultNum);
    }

    /**
     * 将string类型的数字转换为int类型,转换失败返回默认值
     *
     * @param num        需要转换的Object类型的数字
     * @param defaultNum 转换失败的默认返回值
     * @return int类型的数字
     */
    public static int parseInt(String num, int defaultNum) {
        if (num != null && num.length() != 0) {
            try {
                return Integer.parseInt(num);
            } catch (NumberFormatException var3) {
                return defaultNum;
            }
        } else {
            return defaultNum;
        }
    }

    /**
     * 将string类型的数字转换为long类型
     *
     * @param num 数字
     * @return long类型的数字
     */
    public static long parseLong(String num) {
        if (num == null) {
            return 0L;
        } else {
            try {
                return Long.parseLong(num);
            } catch (NumberFormatException var2) {
                return 0L;
            }
        }
    }

    /**
     * 将string类型的数字转换为float类型
     *
     * @param num 数字
     * @return float类型的数字
     */
    public static float parseFloat(String num) {
        if (num == null) {
            return 0.0F;
        } else {
            try {
                return Float.parseFloat(num);
            } catch (NumberFormatException var2) {
                return 0.0F;
            }
        }
    }

    /**
     * 将double类型的数字转换为float类型
     *
     * @param num 数字
     * @return double类型的数字
     */
    public static double parseDouble(String num) {
        if (num == null) {
            return 0.0D;
        } else {
            try {
                return Double.parseDouble(num);
            } catch (NumberFormatException var2) {
                return 0.0D;
            }
        }
    }

    /**
     * 给字符串去前后空格,并添加到字符串池中
     * trim()前后去空格
     * intern()返回原字符串,会判断字符串池有是否有这个字符串,没有就添加到池中
     *
     * @param input 炫耀操作的字符串
     * @return 空或者操作后的字符串
     */
    public static String convertNULL(String input) {
        return input == null ? "" : input.trim().intern();
    }

    /**
     * 给字符串去前后空格
     * trim()前后去空格
     *
     * @param input 炫耀操作的字符串
     * @return 空或者操作后的字符串
     */
    public static String convertNULL(Object input) {
        return input == null ? "" : convertNULL(input.toString());
    }


    /**
     * 将当前异常转换为string
     *
     * @param e 异常对象
     * @return string类型的异常对象
     */
    public static String fromException(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception var3) {
            return "";
        }
    }

    /**
     * 给当前string去除斜杠一般在处理路径的时候用,去除多余的斜杠
     *
     * @param input 需要去除多余斜杠的路径
     * @return 处理后的路径
     */
    public static String clearPath(String input) {
        input = input.replace('\\', '/');
        return clearPathHead(input);
    }

    /**
     * 去除路径开头的前缀
     *
     * @param input 需要处理的string路径
     * @return 处理后的对象
     */
    private static String clearPathHead(String input) {
        int from = 0;
        int j = input.indexOf("://");
        if (j != -1) {
            from = j + 3;
        }

        int i = input.indexOf("//", from);
        if (i == -1) {
            return input;
        } else {
            String inputHead = input.substring(0, i) + "/" + input.substring(i + 2);
            return clearPathHead(inputHead);
        }
    }

    /**
     * 将该string的手字母大写
     *
     * @param inString string
     * @return string
     */
    public static String captureName(String inString) {
        if (isEmpty(inString)) {
            return "";
        } else {
            return inString.length() > 1 ? inString.substring(0, 1).toUpperCase() + inString.substring(1) : inString.toUpperCase();
        }
    }

    /**
     * 在层级包结构路径中获取className
     *
     * @param className className
     * @return 处理后的string
     */
    public static String simplifyClassName(String className) {
        String[] packages = stringToArray(className, ".");
        if (packages != null && packages.length >= 1) {
            int len = packages.length;
            if (len == 1) {
                return packages[0];
            } else {
                StringBuilder name = new StringBuilder();

                for (int i = 0; i < len - 1; ++i) {
                    String item = packages[i];
                    name.append(item, 0, 1).append(".");
                }

                name.append(packages[len - 1]);
                return name.toString();
            }
        } else {
            return className;
        }
    }

    /**
     * 掉用join方法获取锁对象,等方法执行完成后才会往下执行
     *
     * @param iterator  iterator
     * @param separator separator
     * @return string
     */
    public static String join(Iterator iterator, CharSequence separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                return first == null ? "" : first.toString();
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while (iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null && !isEmpty(obj.toString())) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }

    /**
     * 掉用join方法获取锁对象,等方法执行完成后才会往下执行
     *
     * @param collection collection
     * @param separator  separator
     * @return string
     */
    public static String join(Collection collection, CharSequence separator) {
        return collection == null ? null : join(collection.iterator(), separator);
    }

    /**
     * 掉用join方法获取锁对象,等方法执行完成后才会往下执行
     *
     * @param collection collection
     * @param separator  separator
     * @return string
     */
    public static String join(Object[] collection, CharSequence separator) {
        return collection == null ? null : join((Collection) Arrays.asList(collection), (CharSequence) separator);
    }

    /**
     * 掉用join方法获取锁对象,等方法执行完成后才会往下执行
     *
     * @param collection collection
     * @param separator  separator
     * @return string
     */
    public static String join(CharSequence separator, Object... collection) {
        return collection == null ? null : join((Collection) Arrays.asList(collection), (CharSequence) separator);
    }

}

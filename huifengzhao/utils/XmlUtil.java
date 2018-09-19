package com.huifengzhao.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;

/**
 * @author huifengzhao
 * @ClassName XmlUtil
 * @date 2018/8/24
 */
public class XmlUtil {

    private static SAXReader reader = new SAXReader();

    public XmlUtil() {
    }

    public static Document load(String path) throws DocumentException {
        return path == null ? null : load(new File(path));
    }

    public static Document load(File file) throws DocumentException {
        if (!file.exists()) {
            return null;
        } else {
            return reader.read(file);
        }
    }

    public static Document load(InputStream inputStream) throws DocumentException {
        return reader.read(inputStream);
    }

}

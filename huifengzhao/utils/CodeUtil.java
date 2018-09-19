package com.huifengzhao.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author huifengzhao
 * @ClassName CodeUtil
 * @date 2018/8/28
 */
public class CodeUtil {
    private CodeUtil() {
    }

    private static Random r = new Random();

    /**
     * 计算字体大小
     */
    private static final double FACTOR = 0.75;

    /**
     * 只保存一次当前验证码
     */
    private static StringBuilder validateCode = new StringBuilder();

    private static final char[] NUM = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'};

    public static Image getValidateCodeImage() {
        return getValidateCodeImage(100, 50);
    }

    public static Image getValidateCodeImage(int width, int height) {
        return getValidateCodeImage(width, height, 4);
    }

    public static Image getValidateCodeImage(int width, int height, int length) {
        return getValidateCodeImage(width, height, length, 10, 50);
    }

    public static Image getValidateCodeImage(int width, int height, int length, int randomLineCount,
                                             int randomPointCount) {
        // 构建一副图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取设置图片内容的画笔
        Graphics g = image.getGraphics();
        // 设置背景
        setBackGround(g, width, height);
        // 设置边框
        setBorder(g);
        // 添加干扰线
        drawRandomLine(g, width, height, randomLineCount);
        // 添加干扰点
        drawRandomPoint(g, width, height, randomPointCount);
        // 写随机数
        drawRandomNum((Graphics2D) g, width, height, length);
        g.dispose();
        return image;

    }

    public static String getValidateCode() {
        return validateCode.toString();
    }

    private static void drawRandomNum(Graphics2D g, int width, int height, int length) {
        // 下次获取验证码时必须先清空
        validateCode.delete(0, validateCode.length());
        int x = 3;
        int size = (int) (height * FACTOR);
        int y = height / 2 + size / 2 - 5;
        g.setFont(new Font("宋体", 0, size));
        // 文字旋转度数
        int degree = 0;
        for (int i = 0; i < length; i++) {
            g.setColor(getRandomColor(0, 255));
            // 设置旋转角度
            degree = r.nextInt(60) - 30;
            // 进行文字旋转
            g.rotate((degree * Math.PI) / 180, x, y);
            // 保存本次数组角标
            int index = r.nextInt(NUM.length);
            g.drawString(String.valueOf(NUM[index]), x, y);
            // 还原旋转角度
            g.rotate(-(degree * Math.PI) / 180, x, y);
            x = x + width / length;

            // 记录随机验证码
            validateCode.append(NUM[index]);
        }
    }

    private static void drawRandomPoint(Graphics g, int width, int height, int randomPointCount) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < randomPointCount; i++) {
            g.setColor(getRandomColor(0, 255));
            x = r.nextInt(width);
            y = r.nextInt(height);
            g.drawLine(x, y, x, y);
        }
    }

    private static void drawRandomLine(Graphics g, int width, int height, int randomLineCount) {
        for (int i = 0; i < randomLineCount; i++) {
            g.setColor(getRandomColor(0, 255));
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }
    }

    private static void setBorder(Graphics g) {

    }

    private static void setBackGround(Graphics g, int width, int height) {
        g.setColor(getRandomColor(220, 255));
        g.fillRect(0, 0, width, height);
    }

    /**
     * 获取一个随机颜色
     */
    private static Color getRandomColor(int low, int high) {
        int sub = high - low + 1;
        return new Color(low + r.nextInt(sub), low + r.nextInt(sub), low + r.nextInt(sub));
    }

}

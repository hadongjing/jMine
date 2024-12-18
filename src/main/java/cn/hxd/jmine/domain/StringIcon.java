package cn.hxd.jmine.domain;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;

public class StringIcon implements Icon {

    private String text;        // 要绘制的字符串
    private Color textColor;    // 字符串的颜色

    // 构造函数
    public StringIcon(String text, Color textColor) {
        this.text = text;
        this.textColor = textColor;
    }

    @Override
    public int getIconWidth() {
        return 0; // 返回图标的宽度
    }

    @Override
    public int getIconHeight() {
        return 0; // 返回图标的高度
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
    	  // 获取组件的当前宽度和高度
        int width = c.getWidth();
        int height = c.getHeight();
        
        // 设置绘制的颜色
        g.setColor(textColor);
        
        // 动态计算字体大小，使其与组件的尺寸成比例
        Font oldFont = g.getFont();
        int fontSize =Math.max(oldFont.getSize(),  Math.min(width, height) / 2); // 字体大小为宽度或高度的1/5
        g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), fontSize));

        // 获取字体的Metrics信息
        FontMetrics fm = g.getFontMetrics();
        
        // 计算字符串的宽度和高度
        int stringWidth = fm.stringWidth(text);
        int stringHeight = fm.getAscent(); // 字符的高度
        
        // 计算字符串的居中位置
        int xPosition = (width - stringWidth) / 2; // 字符串X轴居中
        int yPosition = (height + stringHeight) / 2; // 字符串Y轴居中

        // 绘制字符串
        g.drawString(text,   xPosition,   yPosition);
    }

}
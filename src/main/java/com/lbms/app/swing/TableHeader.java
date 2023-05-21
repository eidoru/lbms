package com.lbms.app.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class TableHeader extends JLabel {

    public TableHeader(String text) {
        super(text);
        setOpaque(false);
        setFont(new Font("SF Pro Text Light", 1, 12));
        setBorder(new EmptyBorder(0, 20, 0, 0));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.height = 40;
        return preferredSize;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(new Color(48, 51, 54));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(new Color(90, 94, 96));
        graphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        graphics.setColor(new Color(90, 94, 96));
        graphics.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        super.paintComponent(graphics);
    }
}

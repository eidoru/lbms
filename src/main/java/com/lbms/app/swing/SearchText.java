package com.lbms.app.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SearchText extends JTextField {
    
    public SearchText() {
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (getText().length() != 0) {
            return;
        }
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setFont(new Font("SF Pro Text Light", 0, 12));
        graphics.drawString("Search here...", getInsets().left, getHeight() / 2 + graphics.getFontMetrics().getAscent() / 2 - 2);
    }
    
}

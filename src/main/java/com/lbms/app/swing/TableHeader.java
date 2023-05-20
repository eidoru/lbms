package com.lbms.app.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
// import javax.swing.SwingConstants;

public class TableHeader extends JLabel {

    public TableHeader(String text) {
        super(text);
        setOpaque(true);
        setBackground(new Color(48, 51, 54));
        setFont(new Font("SF Pro Text Light", 1, 12));
        setBorder(new EmptyBorder(0, 20, 0, 0));
        // setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.height = 40;
        return preferredSize;
    }
}

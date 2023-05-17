package com.lbms.app.panels;

import com.lbms.app.objects.MenuModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuItem extends javax.swing.JPanel {
    
    private boolean selected;

    public MenuItem(MenuModel data) {
        initComponents();
        setOpaque(false);
        switch (data.getType()) {
            case MENU -> {
                menuIcon.setIcon(data.toIcon());
                menuLabel.setText(data.getName());
            }
            case TITLE -> {
                menuIcon.setText(data.getName());
                menuIcon.setFont(new Font("Segoe UI", 1, 12));
                menuLabel.setVisible(false);
            }
            default -> menuLabel.setText(" ");
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuIcon = new javax.swing.JLabel();
        menuLabel = new javax.swing.JLabel();

        menuLabel.setText("Menu Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(menuIcon)
                .addGap(20, 20, 20)
                .addComponent(menuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics graphics) {
        if (selected) {
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setColor(new Color(74, 76, 78));
            graphics2d.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 5, 5);
        }
        super.paintComponent(graphics);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel menuIcon;
    private javax.swing.JLabel menuLabel;
    // End of variables declaration//GEN-END:variables
}

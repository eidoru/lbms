package com.lbms.app.swing;

import com.lbms.app.object.MenuModel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuItem extends javax.swing.JPanel {

    private final MenuModel data;
    private boolean selected;

    public MenuItem(MenuModel data) {
        this.data = data;
        initComponents();
        setOpaque(false);
        if (data.getType() == MenuModel.MenuType.MENU) {
            icon.setIcon(data.toIcon());
            name.setText(data.getName());
        } else {
            name.setText("");
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (selected) {
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setColor(new Color(60, 63, 65));
            graphics2d.fillRoundRect(0, 0, getWidth(), getHeight() , 20, 20);
            graphics2d.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
//            graphics2d.fillRoundRect(0, 5, getWidth(), getHeight() - 10, 20, 20);
//            graphics2d.fillRect(getWidth() - 20, 5, getWidth(), getHeight() - 10);
        }
        super.paintComponent(graphics);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        name = new javax.swing.JLabel();

        name.setFont(new java.awt.Font("SF Pro Text Light", 0, 14)); // NOI18N
        name.setText("Menu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(icon)
                .addGap(20, 20, 20)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel icon;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}

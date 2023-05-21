package com.lbms.app.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import com.lbms.app.event.OnLogoutEvent;

public class Logout extends javax.swing.JPanel {

    private OnLogoutEvent event;
    private boolean selected;

    public Logout() {
        initComponents();
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                setSelected(true);
                if (event != null) {
                    event.selected(selected);
                }
                setSelected(false);
            }
        });
    }

    public void onLogoutSelectEvent(OnLogoutEvent event) {
        this.event = event;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        name = new javax.swing.JLabel();

        setOpaque(false);

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N

        name.setFont(new java.awt.Font("SF Pro Text Light", 0, 14)); // NOI18N
        name.setText("Logout");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(icon)
                .addGap(20, 20, 20)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(icon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics graphics) {
        if (selected) {
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setColor(new Color(60, 63, 65));
            graphics2d.fillRoundRect(0, 5, getWidth(), getHeight() - 10, 20, 20);
        }
        super.paintComponent(graphics);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel icon;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}

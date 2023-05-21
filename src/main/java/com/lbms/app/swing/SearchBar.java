package com.lbms.app.swing;

import com.lbms.app.event.OnSearchEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

public class SearchBar extends javax.swing.JPanel {
    
    private OnSearchEvent onSearchEvent;
    private DocumentListener documentListener;

    public SearchBar() {
        initComponents();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onSearchEvent != null) {
                    onSearchEvent.onButtonClicked();
                }
            }
        });
    }
    
    public void setDocumentListener(DocumentListener listener) {
        search.getDocument().addDocumentListener(listener);
    }
    
    public void setSearchEvent(OnSearchEvent onSearchEvent) {
        this.onSearchEvent = onSearchEvent;
    }
    
    public String getText() {
        return search.getText();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        search = new com.lbms.app.swing.SearchText();
        button = new javax.swing.JButton();

        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchActionPerformed

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        
    }//GEN-LAST:event_buttonActionPerformed

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintChildren(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setColor(getBackground());
        graphics2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        graphics2d.setColor(new Color(97, 99, 101));
        graphics2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button;
    private com.lbms.app.swing.SearchText search;
    // End of variables declaration//GEN-END:variables
}

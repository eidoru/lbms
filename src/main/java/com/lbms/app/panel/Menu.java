package com.lbms.app.panel;

import com.lbms.app.event.MenuItemSelectEvent;
import com.lbms.app.object.MenuModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class Menu extends javax.swing.JPanel {
    
    private MenuItemSelectEvent event;
    
    public Menu() {
        initComponents();
        setOpaque(false);
        menuList.setOpaque(false);
        initList();
    }
    
    public void onMenuItemSelect(MenuItemSelectEvent event) {
        this.event = event;
        menuList.onMenuItemSelect(event);
    }
    
    public void initList() {
        menuList.addItem(new MenuModel("dashboard", "DASHBOARD", MenuModel.MenuType.MENU));
        
        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("", "USER MANAGEMENT", MenuModel.MenuType.TITLE));
        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("person", "ADD USER", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("person", "VIEW USERS", MenuModel.MenuType.MENU));

        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("", "BOOK MANAGEMENT", MenuModel.MenuType.TITLE));
        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("book", "ADD BOOK", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("book", "VIEW BOOKS", MenuModel.MenuType.MENU));
        
        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("", "REQUEST MANAGEMENT", MenuModel.MenuType.TITLE));
        menuList.addItem(new MenuModel("", "", MenuModel.MenuType.EMPTY));
        menuList.addItem(new MenuModel("request", "VIEW REQUESTS", MenuModel.MenuType.MENU));
    }   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        menuList = new com.lbms.app.panel.MenuList<>();

        setBackground(new java.awt.Color(49, 51, 53));

        titlePanel.setOpaque(false);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("LIBRARY MANAGER");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuList.setBackground(new java.awt.Color(49, 51, 53));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(menuList, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintChildren(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setColor(getBackground());
        graphics2d.fillRect(0, 0, getWidth(), getHeight());
        super.paintChildren(graphics);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.lbms.app.panel.MenuList<String> menuList;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}

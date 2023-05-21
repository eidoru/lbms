package com.lbms.app.swing;

import com.lbms.app.object.MenuModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import com.lbms.app.event.OnMenuItemSelectEvent;
import com.lbms.app.event.OnLogoutEvent;

public final class Menu extends javax.swing.JPanel {

    private OnMenuItemSelectEvent menuEvent;
    private OnLogoutEvent logoutEvent;
    private int userType;

    public Menu() {
        initComponents();
        setOpaque(false);
    }

    public Menu(int userType) {
        this.userType = userType;

        initComponents();

        switch (userType) {
            case 0 ->
                initMenuItemsForStudent();
            case 1 ->
                initMenuItemsForLibrarian();
        }

        setOpaque(false);
    }

    public void onLogoutSelect(OnLogoutEvent event) {
        logoutEvent = event;
        logout.onLogoutSelectEvent(event);
    }

    public void onMenuItemSelect(OnMenuItemSelectEvent event) {
        menuEvent = event;
        menuList.onMenuItemSelect(event);
    }

    public void initMenuItemsForLibrarian() {
        menuList.addItem(new MenuModel("account", "Users", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("book", "Books", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("borrow", "Borrowers", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("request", "Requests", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("overdue", "Overdues", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("edit_account", "Edit Profile", MenuModel.MenuType.MENU));
    }

    public void initMenuItemsForStudent() {
        menuList.addItem(new MenuModel("book", "Books", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("overdue", "Overdues", MenuModel.MenuType.MENU));
        menuList.addItem(new MenuModel("edit_account", "Edit Profile", MenuModel.MenuType.MENU));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        menuList = new com.lbms.app.swing.MenuList<>();
        logout = new com.lbms.app.swing.Logout();

        setBackground(new java.awt.Color(48, 51, 54));

        container.setBackground(new java.awt.Color(48, 51, 54));
        container.setOpaque(false);

        logo.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logo.png"))); // NOI18N

        name.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        name.setText("LIBRARIAM");

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(logo)
                .addGap(10, 10, 10)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        logout.setBackground(new java.awt.Color(48, 51, 54));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addComponent(menuList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(menuList, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setBackground(getBackground());
        super.paintComponent(graphics);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    private javax.swing.JLabel logo;
    private com.lbms.app.swing.Logout logout;
    private com.lbms.app.swing.MenuList<String> menuList;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}

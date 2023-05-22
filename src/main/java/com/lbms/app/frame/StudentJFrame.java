package com.lbms.app.frame;

import com.lbms.app.database.Database;
import com.lbms.app.object.Book;
import com.lbms.app.object.Borrow;
import com.lbms.app.object.Overdue;
import com.lbms.app.object.User;
import com.lbms.app.swing.SearchBar;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public final class StudentJFrame extends javax.swing.JFrame {

    private DefaultTableModel model;
    private final Database database;
    private static int loggedId;

    public StudentJFrame(int userId) {
        database = new Database();
        loggedId = userId;

        setTitle(database.getCurrentUser(userId).getFirstName());

        // init methods
        initComponents();
        viewBookTable();

        // init events
        onMenuItemSelect();
        onLogoutSelect();
        onSearchListener(bookSearchBar, overdueSearchBar);

        // init listeners
        bookRowSelectionListener();
    }

    public void onLogoutSelect() {
        menu.onLogoutSelect((boolean selected) -> {
            if (selected) {
                int logout = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (logout == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginJFrame().setVisible(true);
                }
            }
        });
    }

    public void onMenuItemSelect() {
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        menu.onMenuItemSelect((int index) -> {
            switch (index) {
                case 0 -> {
                    cardLayout.show(contentPanel, "card1");
                    viewBookTable();
                }
                case 1 -> {
                    cardLayout.show(contentPanel, "card3");
                    // viewMyOverdueTable();
                    viewMyBorrowTable();

                }
//                case 2 -> {
//                    cardLayout.show(contentPanel, "card3");
//                    viewMyBorrowTable();
//                }
            }
        });
    }

    public void emptyFields(JTextField... textFields) {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }

    public void displayBookTableOnSearch(String[] keywords) {
        // clear table
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        // fill table
        ArrayList<Book> books = database.displayBookSearch(keywords);
        Object[] data = new Object[5];
        for (Book book : books) {
            data[0] = book.getBookId();
            data[1] = book.getTitle();
            data[2] = book.getAuthor();
            data[3] = book.getIsbn();
            data[4] = book.getStatus();
            model.addRow(data);
        }
    }

    public void displayOverdueTableOnSearch(String[] keywords) {
        // clear table
        DefaultTableModel model = (DefaultTableModel) overdueTable.getModel();
        model.setRowCount(0);
        // fill table
        ArrayList<Overdue> overdues = database.displayOverdueSearch(keywords);
        Object[] data = new Object[5];
        for (Overdue overdue : overdues) {
            data[0] = overdue.getOverdueId();
            data[1] = overdue.getBookTitle();
            data[2] = overdue.getUserFirstName();
            data[3] = overdue.getUserLastName();
            data[4] = overdue.getFine();
            model.addRow(data);
        }
    }

    public void onBorrowEvent() {
        borrowButton.setEnabled(false);
        reserveButton.setEnabled(true);
    }

    public void bookRowSelectionListener() {
        bookTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int index = bookTable.getSelectedRow();
            if (index == -1) {
                return;
            }
            int bookId = Integer.parseInt(bookTable.getValueAt(index, 0).toString());
            String status = bookTable.getValueAt(index, 4).toString();
            if (database.hasRequest(bookId)) {
                borrowButton.setEnabled(false);
                reserveButton.setEnabled(true);
            } else if ("Borrowed".equals(status)) {
                borrowButton.setEnabled(false);
                reserveButton.setEnabled(true);
            } else {
                borrowButton.setEnabled(true);
                reserveButton.setEnabled(false);
            }
        });
    }

    public void onSearchListener(SearchBar... searchBars) {
        for (SearchBar searchBar : searchBars) {
            searchBar.setSearchEvent(() -> {
                String input = searchBar.getText().toLowerCase();
                if (input.length() == 0) {
                    return;
                }
                String[] keywords = input.split(" ");
                if (searchBar.equals(bookSearchBar)) {
                    displayBookTableOnSearch(keywords);
                }
                if (searchBar.equals(overdueSearchBar)) {
                    displayOverdueTableOnSearch(keywords);
                }
            });
        }
    }

    public void viewBookTable() {
        model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        ArrayList<Book> books = database.getBooks();
        if (books == null) {
            return;
        }
        Object[] object = new Object[5];
        for (Book book : books) {
            object[0] = book.getBookId();
            object[1] = book.getTitle();
            object[2] = book.getAuthor();
            object[3] = book.getIsbn();
            switch (book.getStatus()) {
                case 0 ->
                    object[4] = "Available";
                case 1 ->
                    object[4] = "Borrowed";
            }
            model.addRow(object);
        }
    }

    public void viewMyOverdueTable() {
        model = (DefaultTableModel) overdueTable.getModel();
        model.setRowCount(0);
        ArrayList<Overdue> overdues = database.getUserOverdues(loggedId);
        if (overdues == null) {
            return;
        }
        Object[] object = new Object[3];
        for (Overdue overdue : overdues) {
            object[0] = overdue.getOverdueId();
            object[1] = overdue.getBookTitle();
            object[2] = overdue.getFine();
            model.addRow(object);
        }
    }

    public void viewMyBorrowTable() {
        model = (DefaultTableModel) borrowTable.getModel();
        model.setRowCount(0);
        ArrayList<Borrow> borrows = database.getUserBorrows(loggedId);
        if (borrows == null) {
            return;
        }
        Object[] object = new Object[4];
        for (Borrow borrow : borrows) {
            object[0] = borrow.getId();
            object[1] = borrow.getBookTitle();
            object[2] = borrow.getStartDate();
            object[3] = borrow.getEndDate();
            model.addRow(object);
        }
    }

    private void updateAttribute(User user, String attributeName, String attributeValue) {
        switch (attributeName) {
            case "password":
                user.setPassword(attributeValue);
                break;
            case "firstName":
                user.setFirstName(attributeValue);
                break;
            case "lastName":
                user.setLastName(attributeValue);
                break;
            case "email":
                user.setEmail(attributeValue);
                break;
            case "address":
                user.setAddress(attributeValue);
            case "contactNumber":
                user.setContactNumber(attributeValue);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menu = new com.lbms.app.swing.Menu(database.getCurrentUser(loggedId).getUserType());
        contentPanel = new com.lbms.app.swing.ContentPanel();
        card1 = new com.lbms.app.swing.ContentPanel();
        bookSearchBar = new com.lbms.app.swing.SearchBar();
        bookScrollPane = new javax.swing.JScrollPane();
        bookTable = new com.lbms.app.swing.Table();
        borrowButton = new javax.swing.JButton();
        reserveButton = new javax.swing.JButton();
        card2 = new com.lbms.app.swing.ContentPanel();
        overdueSearchBar = new com.lbms.app.swing.SearchBar();
        overdueScrollPane = new javax.swing.JScrollPane();
        overdueTable = new com.lbms.app.swing.Table();
        card3 = new com.lbms.app.swing.ContentPanel();
        profileConfirmButton = new javax.swing.JButton();
        passwordEditLabel = new javax.swing.JLabel();
        passwordEditField = new javax.swing.JPasswordField();
        firstNameEditLabel = new javax.swing.JLabel();
        firstNameEditField = new javax.swing.JTextField();
        lastNameEditLabel = new javax.swing.JLabel();
        lastNameEditField = new javax.swing.JTextField();
        emailEditLabel = new javax.swing.JLabel();
        emailEditField = new javax.swing.JTextField();
        addressEditLabel = new javax.swing.JLabel();
        addressEditField = new javax.swing.JTextField();
        contactEditLabel = new javax.swing.JLabel();
        contactEditField = new javax.swing.JTextField();
        borrowedLabel = new javax.swing.JLabel();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new com.lbms.app.swing.Table();
        updateLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(48, 51, 54));

        contentPanel.setLayout(new java.awt.CardLayout());

        bookSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        bookTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book ID", "Title", "Author", "ISBN", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bookScrollPane.setViewportView(bookTable);

        borrowButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        borrowButton.setText("BORROW");
        borrowButton.setEnabled(false);
        borrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowButtonActionPerformed(evt);
            }
        });

        reserveButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        reserveButton.setText("RESERVE");
        reserveButton.setEnabled(false);
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card1Layout.createSequentialGroup()
                        .addComponent(borrowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(reserveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(bookSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bookScrollPane))
                .addGap(25, 25, 25))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(bookScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reserveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card1, "card1");

        overdueSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        overdueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Overdue ID", "Book Title", "Fine"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        overdueScrollPane.setViewportView(overdueTable);

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(overdueSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(overdueScrollPane))
                .addGap(25, 25, 25))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(overdueSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(overdueScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card2, "card2");

        profileConfirmButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        profileConfirmButton.setText("UPDATE");
        profileConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileConfirmButtonActionPerformed(evt);
            }
        });

        passwordEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        passwordEditLabel.setText("Password");

        passwordEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        firstNameEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        firstNameEditLabel.setText("First Name");

        firstNameEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        lastNameEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        lastNameEditLabel.setText("Last Name");

        lastNameEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        emailEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        emailEditLabel.setText("Email Address");

        emailEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        addressEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        addressEditLabel.setText("Address");

        addressEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        contactEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        contactEditLabel.setText("Contact Number");

        contactEditField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        borrowedLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 18)); // NOI18N
        borrowedLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        borrowedLabel.setText("Borrowed Books");

        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book ID", "Title", "Start Date", "End Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowTable.setRowSelectionAllowed(false);
        borrowTable.getTableHeader().setResizingAllowed(false);
        borrowTable.getTableHeader().setReorderingAllowed(false);
        borrowScrollPane.setViewportView(borrowTable);

        updateLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 18)); // NOI18N
        updateLabel.setText("Update Profile");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contactEditField)
                    .addComponent(contactEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addressEditField)
                    .addComponent(addressEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailEditField)
                    .addComponent(emailEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastNameEditField)
                    .addComponent(lastNameEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstNameEditField)
                    .addComponent(firstNameEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordEditField)
                    .addComponent(passwordEditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(profileConfirmButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(borrowedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(borrowScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(borrowedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card3Layout.createSequentialGroup()
                        .addComponent(passwordEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(passwordEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(firstNameEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(firstNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lastNameEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lastNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(emailEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(emailEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(addressEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(addressEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(contactEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(contactEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(borrowScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE)
                .addComponent(profileConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card3, "card3");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed
        int selectedIndex = bookTable.getSelectedRow();
        int bookId = Integer.parseInt(bookTable.getValueAt(selectedIndex, 0).toString());
        String title = bookTable.getValueAt(selectedIndex, 1).toString();
        String author = bookTable.getValueAt(selectedIndex, 2).toString();

        if (database.hasUserMadeRequest(loggedId, "request", bookId) || database.hasUserMadeRequest(loggedId, "reserve", bookId) || database.hasUserMadeRequest(loggedId, "borrow", bookId)) {
            JOptionPane.showMessageDialog(null, "You already have a pending request!");
            return;
        }

        ReserveJFrame reserveJFrame = new ReserveJFrame(bookId, loggedId, title, author);
        reserveJFrame.setVisible(true);
    }//GEN-LAST:event_reserveButtonActionPerformed

    private void profileConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileConfirmButtonActionPerformed
        Map<String, JTextField> textFieldMap = new HashMap<>();
        textFieldMap.put("password", passwordEditField);
        textFieldMap.put("firstName", firstNameEditField);
        textFieldMap.put("lastName", lastNameEditField);
        textFieldMap.put("email", emailEditField);
        textFieldMap.put("address", addressEditField);
        textFieldMap.put("contactNumber", contactEditField);

        boolean flag = false;

        User user = database.getCurrentUser(loggedId);

        for (Map.Entry<String, JTextField> entry : textFieldMap.entrySet()) {
            String attributeName = entry.getKey();
            JTextField textField = entry.getValue();

            if (!textField.getText().isEmpty()) {
                flag = true;
                updateAttribute(user, attributeName, textField.getText());
            }
        }

        if (!flag) {
            JOptionPane.showMessageDialog(null, "Fill at least one field to update!");
            return;
        }

        database.updateUser(user);
        JOptionPane.showMessageDialog(null, "Your profile has been updated!");
        emptyFields(passwordEditField, firstNameEditField, lastNameEditField, emailEditField, addressEditField, contactEditField);
    }//GEN-LAST:event_profileConfirmButtonActionPerformed

    private void borrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowButtonActionPerformed
        int selectedIndex = bookTable.getSelectedRow();
        int bookId = Integer.parseInt(bookTable.getValueAt(selectedIndex, 0).toString());
        String title = bookTable.getValueAt(selectedIndex, 1).toString();
        String author = bookTable.getValueAt(selectedIndex, 2).toString();

        BorrowJFrame borrowJFrame = new BorrowJFrame(bookId, loggedId, title, author);
        borrowJFrame.setStudentJFrame(this);
        borrowJFrame.setVisible(true);
    }//GEN-LAST:event_borrowButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StudentJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentJFrame(loggedId).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressEditField;
    private javax.swing.JLabel addressEditLabel;
    private javax.swing.JScrollPane bookScrollPane;
    private com.lbms.app.swing.SearchBar bookSearchBar;
    private com.lbms.app.swing.Table bookTable;
    private javax.swing.JButton borrowButton;
    private javax.swing.JScrollPane borrowScrollPane;
    private com.lbms.app.swing.Table borrowTable;
    private javax.swing.JLabel borrowedLabel;
    private com.lbms.app.swing.ContentPanel card1;
    private com.lbms.app.swing.ContentPanel card2;
    private com.lbms.app.swing.ContentPanel card3;
    private javax.swing.JTextField contactEditField;
    private javax.swing.JLabel contactEditLabel;
    private com.lbms.app.swing.ContentPanel contentPanel;
    private javax.swing.JTextField emailEditField;
    private javax.swing.JLabel emailEditLabel;
    private javax.swing.JTextField firstNameEditField;
    private javax.swing.JLabel firstNameEditLabel;
    private javax.swing.JTextField lastNameEditField;
    private javax.swing.JLabel lastNameEditLabel;
    private javax.swing.JPanel mainPanel;
    private com.lbms.app.swing.Menu menu;
    private javax.swing.JScrollPane overdueScrollPane;
    private com.lbms.app.swing.SearchBar overdueSearchBar;
    private com.lbms.app.swing.Table overdueTable;
    private javax.swing.JPasswordField passwordEditField;
    private javax.swing.JLabel passwordEditLabel;
    private javax.swing.JButton profileConfirmButton;
    private javax.swing.JButton reserveButton;
    private javax.swing.JLabel updateLabel;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.lbms.app.frame;

import com.lbms.app.database.Database;
import com.lbms.app.object.Book;
import com.lbms.app.object.Borrow;
import com.lbms.app.object.Request;
import com.lbms.app.object.User;
import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eidoru
 */
public final class LibrarianJFrame extends javax.swing.JFrame {

    private DefaultTableModel model;
    private final Database database;
    private static int loggedId;
    
    public LibrarianJFrame(int userId) {
        loggedId = userId;
        database = new Database();

        // init methods
        initComponents();
        initTables();

        // init events
        onMenuItemSelect();
        onLogoutSelect();

        // init id listeners
        userIdField.setText(String.valueOf(database.getTableId("user", "user_id")));
        bookIdField.setText(String.valueOf(database.getTableId("book", "book_id")));

        // checks for filled fields to enable add button
        bindTextFields(addUserButton, passwordField, firstNameField, lastNameField, emailField, addressField, contactField);
        bindTextFields(addBookButton, titleField, authorField, isbnField);
        System.out.println(loggedId);
    }

    public void bindTextFields(JButton button, JTextField... textFields) {
        for (JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateButtonEnabledState();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateButtonEnabledState();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateButtonEnabledState();
                }

                private void updateButtonEnabledState() {
                    boolean enableButton = true;
                    for (JTextField textField : textFields) {
                        if (textField.getText().isEmpty()) {
                            enableButton = false;
                            break;
                        }
                    }
                    button.setEnabled(enableButton);
                }
            });
        }
    }

//    public boolean checkEmptyFields(JTextField... textFields) {
//        for (JTextField textField : textFields) {
//            if (textField.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(null, "Please enter the remaining fields!");
//                return true;
//            }
//        }
//        return false;
//    }
    public void emptyFields(JTextField... textFields) {
        for (JTextField textField : textFields) {
            if (textField.equals(userIdField)) {
                continue;
            }
            textField.setText("");
        }
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
                case 0 ->
                    cardLayout.show(contentPanel, "card1");  // users
                case 1 ->
                    cardLayout.show(contentPanel, "card2");  // books
                case 2 ->
                    cardLayout.show(contentPanel, "card3");  // borrowers
                case 3 ->
                    cardLayout.show(contentPanel, "card4");  // requests
                case 4 ->
                    cardLayout.show(contentPanel, "card5");   // profile
            }
        });
    }

    public void initTables() {
        viewUserTable();
        viewBookTable();
        viewRequestTable();
        viewBorrowTable();
    }

    public void viewUserTable() {
        model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        ArrayList<User> users = database.getUsers();
        if (users == null) {
            return;
        }
        Object[] object = new Object[9];
        for (User user : users) {
            object[0] = user.getUserId();
            object[1] = user.getFirstName();
            object[2] = user.getLastName();
            object[3] = user.getEmail();
            object[4] = user.getAddress();
            object[5] = user.getContactNumber();
            object[6] = user.getCourse();
            object[7] = user.getYearLevel();
            switch (user.getUserType()) {
                case 0 ->
                    object[8] = "Student";
                case 1 ->
                    object[8] = "Librarian";
            }
            model.addRow(object);
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

    public void viewRequestTable() {
        model = (DefaultTableModel) requestTable.getModel();
        model.setRowCount(0);
        ArrayList<Request> requests = database.getRequests();
        if (requests == null) {
            return;
        }
        Object[] object = new Object[5];
        for (Request request : requests) {
            object[0] = request.getId();
            object[1] = request.getBookTitle();
            object[2] = request.getUserFirstName();
            object[3] = request.getUserLastName();
            object[4] = request.getDuration();
            model.addRow(object);
        }
    }

    public void viewBorrowTable() {
        model = (DefaultTableModel) borrowTable.getModel();
        model.setRowCount(0);
        ArrayList<Borrow> borrowers = database.getBorrowers();
        if (borrowers == null) {
            return;
        }
        Object[] object = new Object[7];
        for (Borrow borrow : borrowers) {
            object[0] = borrow.getId();
            object[1] = borrow.getBookTitle();
            object[2] = borrow.getUserFirstName();
            object[3] = borrow.getUserLastName();
            object[4] = borrow.getDuration();
            object[5] = borrow.getStartDate();
            object[6] = borrow.getEndDate();
            model.addRow(object);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        card2AddUser1 = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        menu = new com.lbms.app.swing.Menu();
        contentPanel = new com.lbms.app.swing.ContentPanel();
        card1 = new com.lbms.app.swing.ContentPanel();
        card1Search = new com.lbms.app.swing.SearchBar();
        card1AddUser = new javax.swing.JButton();
        card1RemoveUser = new javax.swing.JButton();
        userScrollPane = new javax.swing.JScrollPane();
        userTable = new com.lbms.app.swing.Table();
        card2 = new com.lbms.app.swing.ContentPanel();
        card2Search = new com.lbms.app.swing.SearchBar();
        card2AddBook = new javax.swing.JButton();
        card2RemoveBook = new javax.swing.JButton();
        bookScrollPane = new javax.swing.JScrollPane();
        bookTable = new com.lbms.app.swing.Table();
        card3 = new com.lbms.app.swing.ContentPanel();
        card3Search = new com.lbms.app.swing.SearchBar();
        card3ReturnBook = new javax.swing.JButton();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new com.lbms.app.swing.Table();
        card4 = new com.lbms.app.swing.ContentPanel();
        card4Search = new com.lbms.app.swing.SearchBar();
        card4ApproveRequestButton = new javax.swing.JButton();
        card4RejectRequestButton = new javax.swing.JButton();
        requestScrollPane = new javax.swing.JScrollPane();
        requestTable = new com.lbms.app.swing.Table();
        card_useradd = new com.lbms.app.swing.ContentPanel();
        userIdLabel = new javax.swing.JLabel();
        userIdField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        firstNameLabel = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        contactLabel = new javax.swing.JLabel();
        contactField = new javax.swing.JTextField();
        courseLabel = new javax.swing.JLabel();
        courseCombo = new javax.swing.JComboBox<>();
        yearLevelLabel = new javax.swing.JLabel();
        yearLevelCombo = new javax.swing.JComboBox<>();
        userTypeLabel = new javax.swing.JLabel();
        studentRadioButton = new javax.swing.JRadioButton();
        librarianRadioButton = new javax.swing.JRadioButton();
        addUserButton = new javax.swing.JButton();
        cancelUserButton = new javax.swing.JButton();
        card_bookadd = new com.lbms.app.swing.ContentPanel();
        bookIdLabel = new javax.swing.JLabel();
        bookIdField = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        authorLabel = new javax.swing.JLabel();
        authorField = new javax.swing.JTextField();
        isbnLabel = new javax.swing.JLabel();
        isbnField = new javax.swing.JTextField();
        addBookButton = new javax.swing.JButton();
        cancelBookButton = new javax.swing.JButton();

        card2AddUser1.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card2AddUser1.setText("Add User");
        card2AddUser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card2AddUser1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(48, 51, 54));

        contentPanel.setLayout(new java.awt.CardLayout());

        card1Search.setBackground(new java.awt.Color(70, 73, 75));

        card1AddUser.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card1AddUser.setText("Add User");
        card1AddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card1AddUserActionPerformed(evt);
            }
        });

        card1RemoveUser.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card1RemoveUser.setText("Remove User");
        card1RemoveUser.setEnabled(false);
        card1RemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card1RemoveUserActionPerformed(evt);
            }
        });

        userTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "First Name", "Last Name", "Email", "Address", "Contact No.", "Course", "Year Level", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setToolTipText("");
        userTable.setGridColor(new java.awt.Color(97, 99, 101));
        userScrollPane.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setResizable(false);
            userTable.getColumnModel().getColumn(1).setResizable(false);
            userTable.getColumnModel().getColumn(2).setResizable(false);
            userTable.getColumnModel().getColumn(3).setResizable(false);
            userTable.getColumnModel().getColumn(4).setResizable(false);
            userTable.getColumnModel().getColumn(5).setResizable(false);
            userTable.getColumnModel().getColumn(6).setResizable(false);
            userTable.getColumnModel().getColumn(7).setResizable(false);
            userTable.getColumnModel().getColumn(8).setResizable(false);
        }

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                        .addComponent(card1AddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(card1RemoveUser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(userScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(card1Search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(card1Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(userScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(card1AddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card1RemoveUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card1, "card1");

        card2Search.setBackground(new java.awt.Color(70, 73, 75));

        card2AddBook.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card2AddBook.setText("Add Book");
        card2AddBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card2AddBookActionPerformed(evt);
            }
        });

        card2RemoveBook.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card2RemoveBook.setText("Remove Book");
        card2RemoveBook.setEnabled(false);
        card2RemoveBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card2RemoveBookActionPerformed(evt);
            }
        });

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
        if (bookTable.getColumnModel().getColumnCount() > 0) {
            bookTable.getColumnModel().getColumn(0).setResizable(false);
            bookTable.getColumnModel().getColumn(1).setResizable(false);
            bookTable.getColumnModel().getColumn(2).setResizable(false);
            bookTable.getColumnModel().getColumn(3).setResizable(false);
            bookTable.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card2Layout.createSequentialGroup()
                        .addComponent(card2AddBook, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(card2RemoveBook, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(card2Layout.createSequentialGroup()
                        .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bookScrollPane)
                            .addComponent(card2Search, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(card2Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(bookScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card2AddBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card2RemoveBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card2, "card2");

        card3Search.setBackground(new java.awt.Color(70, 73, 75));

        card3ReturnBook.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card3ReturnBook.setText("Return Book");
        card3ReturnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                card3ReturnBookActionPerformed(evt);
            }
        });

        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Borrow ID", "Book Title", "FIrst Name", "Last Name", "Duration", "Start Date", "End Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowScrollPane.setViewportView(borrowTable);
        if (borrowTable.getColumnModel().getColumnCount() > 0) {
            borrowTable.getColumnModel().getColumn(0).setResizable(false);
            borrowTable.getColumnModel().getColumn(1).setResizable(false);
            borrowTable.getColumnModel().getColumn(2).setResizable(false);
            borrowTable.getColumnModel().getColumn(3).setResizable(false);
            borrowTable.getColumnModel().getColumn(4).setResizable(false);
            borrowTable.getColumnModel().getColumn(5).setResizable(false);
            borrowTable.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card3Layout.createSequentialGroup()
                        .addComponent(card3ReturnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(card3Layout.createSequentialGroup()
                        .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(borrowScrollPane)
                            .addComponent(card3Search, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(card3Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(borrowScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(card3ReturnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card3, "card3");

        card4Search.setBackground(new java.awt.Color(70, 73, 75));

        card4ApproveRequestButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card4ApproveRequestButton.setText("Approve Request");
        card4ApproveRequestButton.setEnabled(false);

        card4RejectRequestButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        card4RejectRequestButton.setText("Reject Request");
        card4RejectRequestButton.setEnabled(false);

        requestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Request ID", "Book Title", "First Name", "Last Name", "Duration"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        requestScrollPane.setViewportView(requestTable);
        if (requestTable.getColumnModel().getColumnCount() > 0) {
            requestTable.getColumnModel().getColumn(0).setResizable(false);
            requestTable.getColumnModel().getColumn(1).setResizable(false);
            requestTable.getColumnModel().getColumn(2).setResizable(false);
            requestTable.getColumnModel().getColumn(3).setResizable(false);
            requestTable.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card4Layout.createSequentialGroup()
                        .addComponent(card4ApproveRequestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(card4RejectRequestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(requestScrollPane)
                        .addComponent(card4Search, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(card4Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(requestScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card4ApproveRequestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card4RejectRequestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card4, "card4");

        userIdLabel.setText("User ID (System Generated)");

        userIdField.setEnabled(false);

        passwordLabel.setText("Password");

        firstNameLabel.setText("First name");

        lastNameLabel.setText("Last name");

        emailLabel.setText("Email address");

        addressLabel.setText("Address");

        contactLabel.setText("Contact number");

        courseLabel.setText("Course");

        courseCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "BSCS", "BSIT" }));

        yearLevelLabel.setText("Year level");

        yearLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "1", "2", "3", "4" }));

        userTypeLabel.setText("User Type");

        buttonGroup1.add(studentRadioButton);
        studentRadioButton.setSelected(true);
        studentRadioButton.setText("Student");

        buttonGroup1.add(librarianRadioButton);
        librarianRadioButton.setText("Librarian");

        addUserButton.setText("ADD");
        addUserButton.setEnabled(false);
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
            }
        });

        cancelUserButton.setText("CANCEL");
        cancelUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card_useraddLayout = new javax.swing.GroupLayout(card_useradd);
        card_useradd.setLayout(card_useraddLayout);
        card_useraddLayout.setHorizontalGroup(
            card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_useraddLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card_useraddLayout.createSequentialGroup()
                        .addComponent(studentRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(librarianRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(userTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card_useraddLayout.createSequentialGroup()
                        .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(yearLevelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(675, Short.MAX_VALUE))
        );
        card_useraddLayout.setVerticalGroup(
            card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_useraddLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(userIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(userIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(contactLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearLevelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(userTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(card_useraddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(studentRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(librarianRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cancelUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card_useradd, "card_useradd");

        bookIdLabel.setText("Book ID (System Generated)");

        bookIdField.setEnabled(false);

        titleLabel.setText("Title");

        authorLabel.setText("Author");

        isbnLabel.setText("ISBN");

        addBookButton.setText("ADD");
        addBookButton.setEnabled(false);
        addBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookButtonActionPerformed(evt);
            }
        });

        cancelBookButton.setText("CANCEL");
        cancelBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBookButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card_bookaddLayout = new javax.swing.GroupLayout(card_bookadd);
        card_bookadd.setLayout(card_bookaddLayout);
        card_bookaddLayout.setHorizontalGroup(
            card_bookaddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_bookaddLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card_bookaddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(isbnField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(isbnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(675, Short.MAX_VALUE))
        );
        card_bookaddLayout.setVerticalGroup(
            card_bookaddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_bookaddLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bookIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(bookIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(authorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(authorField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(isbnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(isbnField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(addBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cancelBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card_bookadd, "card_bookadd");

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
            .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void card2AddUser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card2AddUser1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card2AddUser1ActionPerformed

    private void cancelBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBookButtonActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card2");
        emptyFields(titleField, authorField, isbnField);
        viewBookTable();
    }//GEN-LAST:event_cancelBookButtonActionPerformed

    private void cancelUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelUserButtonActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card1");
        emptyFields(passwordField, firstNameField, lastNameField, emailField, addressField, contactField);
        viewUserTable();
    }//GEN-LAST:event_cancelUserButtonActionPerformed

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserButtonActionPerformed

        if (studentRadioButton.isSelected() && (courseCombo.getSelectedItem().toString().equals("None") || yearLevelCombo.getSelectedItem().toString().equals("None"))) {
            JOptionPane.showMessageDialog(null, "Please select a course and year!");
            return;
        }
        
        if (librarianRadioButton.isSelected() && (!courseCombo.getSelectedItem().toString().equals("None") || !yearLevelCombo.getSelectedItem().toString().equals("None"))) {
            JOptionPane.showMessageDialog(null, "Choose 'None' and '0' for both course and year for librarian!");
            return;
        }

        int userId = Integer.parseInt(userIdField.getText());
        String password = new String(passwordField.getPassword());
        String firstname = firstNameField.getText();
        String lastname = lastNameField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String contactNumber = contactField.getText();
        String course = (courseCombo.getSelectedItem().toString().equals("None")) ? "None" : (String) courseCombo.getSelectedItem();
        int yearLevel = (yearLevelCombo.getSelectedItem().toString().equals("None")) ? 0 : Integer.parseInt((String) yearLevelCombo.getSelectedItem());
        int userType = (librarianRadioButton.isSelected()) ? 1 : 0;

        User user = new User(userId, password, firstname, lastname, email, address, contactNumber, course, yearLevel, userType);

        database.insertUser(user);
        userIdField.setText(Integer.toString(database.getTableId("user", "user_id")));
        JOptionPane.showMessageDialog(null, "User has been added to the database!");
        emptyFields(passwordField, firstNameField, lastNameField, emailField, addressField, contactField);
    }//GEN-LAST:event_addUserButtonActionPerformed

    private void card2RemoveBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card2RemoveBookActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card2RemoveBookActionPerformed

    private void card2AddBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card2AddBookActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card_bookadd");
    }//GEN-LAST:event_card2AddBookActionPerformed

    private void card1RemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card1RemoveUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card1RemoveUserActionPerformed

    private void card1AddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card1AddUserActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card_useradd");
    }//GEN-LAST:event_card1AddUserActionPerformed

    private void card3ReturnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_card3ReturnBookActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_card3ReturnBookActionPerformed

    private void addBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookButtonActionPerformed

        int id = Integer.parseInt(bookIdField.getText());
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        int status = 0;
        
        Book book = new Book(id, title, author, isbn, status);
        
        database.insertBook(book);
        bookIdField.setText(Integer.toString(database.getTableId("book", "book_id")));
        JOptionPane.showMessageDialog(null, "Book has been added to the database!");
        emptyFields(titleField, authorField, isbnField);
    }//GEN-LAST:event_addBookButtonActionPerformed

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
            java.util.logging.Logger.getLogger(LibrarianJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LibrarianJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LibrarianJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LibrarianJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LibrarianJFrame(loggedId).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBookButton;
    private javax.swing.JButton addUserButton;
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField authorField;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JTextField bookIdField;
    private javax.swing.JLabel bookIdLabel;
    private javax.swing.JScrollPane bookScrollPane;
    private com.lbms.app.swing.Table bookTable;
    private javax.swing.JScrollPane borrowScrollPane;
    private com.lbms.app.swing.Table borrowTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelBookButton;
    private javax.swing.JButton cancelUserButton;
    private com.lbms.app.swing.ContentPanel card1;
    private javax.swing.JButton card1AddUser;
    private javax.swing.JButton card1RemoveUser;
    private com.lbms.app.swing.SearchBar card1Search;
    private com.lbms.app.swing.ContentPanel card2;
    private javax.swing.JButton card2AddBook;
    private javax.swing.JButton card2AddUser1;
    private javax.swing.JButton card2RemoveBook;
    private com.lbms.app.swing.SearchBar card2Search;
    private com.lbms.app.swing.ContentPanel card3;
    private javax.swing.JButton card3ReturnBook;
    private com.lbms.app.swing.SearchBar card3Search;
    private com.lbms.app.swing.ContentPanel card4;
    private javax.swing.JButton card4ApproveRequestButton;
    private javax.swing.JButton card4RejectRequestButton;
    private com.lbms.app.swing.SearchBar card4Search;
    private com.lbms.app.swing.ContentPanel card_bookadd;
    private com.lbms.app.swing.ContentPanel card_useradd;
    private javax.swing.JTextField contactField;
    private javax.swing.JLabel contactLabel;
    private com.lbms.app.swing.ContentPanel contentPanel;
    private javax.swing.JComboBox<String> courseCombo;
    private javax.swing.JLabel courseLabel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField isbnField;
    private javax.swing.JLabel isbnLabel;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JRadioButton librarianRadioButton;
    private javax.swing.JPanel mainPanel;
    private com.lbms.app.swing.Menu menu;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JScrollPane requestScrollPane;
    private com.lbms.app.swing.Table requestTable;
    private javax.swing.JRadioButton studentRadioButton;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField userIdField;
    private javax.swing.JLabel userIdLabel;
    private javax.swing.JScrollPane userScrollPane;
    private com.lbms.app.swing.Table userTable;
    private javax.swing.JLabel userTypeLabel;
    private javax.swing.JComboBox<String> yearLevelCombo;
    private javax.swing.JLabel yearLevelLabel;
    // End of variables declaration//GEN-END:variables
}

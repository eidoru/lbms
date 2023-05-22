package com.lbms.app.frame;

import com.lbms.app.database.Database;
import com.lbms.app.object.Book;
import com.lbms.app.object.Borrow;
import com.lbms.app.object.Overdue;
import com.lbms.app.object.Request;
import com.lbms.app.object.User;
import com.lbms.app.swing.SearchBar;
import com.lbms.app.swing.Table;
import java.awt.CardLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public final class LibrarianJFrame extends javax.swing.JFrame {

    private DefaultTableModel model;
    private final Database database;
    private static int loggedId;

    public LibrarianJFrame(int userId) {
        loggedId = userId;
        database = new Database();

        setTitle(database.getCurrentUser(userId).getFirstName());

        // init methods
        initComponents();
        viewUserTable();

        // init events
        onMenuItemSelect();
        onLogoutSelect();

        // listeners
        userIdField.setText(String.valueOf(database.getTableId("user", "user_id")));
        bookIdField.setText(String.valueOf(database.getTableId("book", "book_id")));
        rowSelectionListener(userTable, bookTable, borrowTable, requestTable); // listens when row is selected
        onSearchListener(userSearchBar, bookSearchBar, borrowSearchBar, requestSearchBar, overdueSearchBar);

        // checks for filled fields to enable add button
        textFieldsListener(addUserButton, passwordField, firstNameField, lastNameField, emailField, addressField, contactField);
        textFieldsListener(addBookButton, titleField, authorField, isbnField);
        System.out.println(loggedId);
    }

    public void displayUserTableOnSearch(String[] keywords) {
        // clear table
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        // fill table
        ArrayList<User> users = database.displayUserSearch(keywords);
        Object[] data = new Object[9];
        for (User user : users) {
            data[0] = user.getUserId();
            data[1] = user.getFirstName();
            data[2] = user.getLastName();
            data[3] = user.getEmail();
            data[4] = user.getAddress();
            data[5] = user.getContactNumber();
            data[6] = user.getCourse();
            data[7] = user.getYearLevel();
            switch (user.getUserType()) {
                case 0 ->
                    data[8] = "Student";
                case 1 ->
                    data[8] = "Librarian";
            }
            model.addRow(data);
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

    public void displayRequestTableOnSearch(String[] keywords) {
        // clear table
        DefaultTableModel model = (DefaultTableModel) requestTable.getModel();
        model.setRowCount(0);
        // fill table
        ArrayList<Request> requests = database.displayRequestSearch(keywords);
        Object[] data = new Object[5];
        for (Request request : requests) {
            data[0] = request.getId();
            data[1] = request.getBookTitle();
            data[2] = request.getUserFirstName();
            data[3] = request.getUserLastName();
            data[4] = request.getDuration();
            model.addRow(data);
        }
    }

    public void displayBorrowTableOnSearch(String[] keywords) {
        // clear table
        DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
        model.setRowCount(0);
        // fill table
        ArrayList<Borrow> borrowers = database.displayBorrowSearch(keywords);
        Object[] data = new Object[7];
        for (Borrow borrow : borrowers) {
            data[0] = borrow.getId();
            data[1] = borrow.getBookTitle();
            data[2] = borrow.getUserFirstName();
            data[3] = borrow.getUserLastName();
            data[4] = borrow.getDuration();
            data[5] = borrow.getStartDate();
            data[6] = borrow.getEndDate();
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

    public void onSearchListener(SearchBar... searchBars) {
        for (SearchBar searchBar : searchBars) {
            searchBar.setSearchEvent(() -> {
                String input = searchBar.getText().toLowerCase();
                if (input.length() == 0) {
                    return;
                }
                String[] keywords = input.split(" ");
                if (searchBar.equals(userSearchBar)) {
                    displayUserTableOnSearch(keywords);
                }
                if (searchBar.equals(bookSearchBar)) {
                    displayBookTableOnSearch(keywords);
                }
                if (searchBar.equals(requestSearchBar)) {
                    displayRequestTableOnSearch(keywords);
                }
                if (searchBar.equals(borrowSearchBar)) {
                    displayBorrowTableOnSearch(keywords);
                }
                if (searchBar.equals(overdueSearchBar)) {
                    displayOverdueTableOnSearch(keywords);
                }
            });
        }
    }

    public void rowSelectionListener(Table... tables) {
        Map<JButton, Table> buttonTableMapping = new HashMap<>();
        buttonTableMapping.put(userRemoveButton, userTable);
        buttonTableMapping.put(bookRemoveButton, bookTable);
        buttonTableMapping.put(borrowReturnButton, borrowTable);
        buttonTableMapping.put(requestApproveButton, requestTable);
        buttonTableMapping.put(requestRejectButton, requestTable);

        for (Table table : tables) {
            table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                int selectedRow = table.getSelectedRow();
                for (Map.Entry<JButton, Table> entry : buttonTableMapping.entrySet()) {
                    JButton currentButton = entry.getKey();
                    Table currentTable = entry.getValue();
                    if (table.equals(currentTable) && selectedRow >= 0) {
                        currentButton.setEnabled(true);
                    } else {
                        currentButton.setEnabled(false);
                    }
                }
            });
        }
    }

    public void textFieldsListener(JButton button, JTextField... textFields) {
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
                case 0 -> {
                    cardLayout.show(contentPanel, "card1");  // users
                    viewUserTable();
                }
                case 1 -> {
                    cardLayout.show(contentPanel, "card2");  // books
                    viewBookTable();
                }
                case 2 -> {
                    cardLayout.show(contentPanel, "card3");  // requests
                    viewRequestTable();
                }
                case 3 -> {
                    cardLayout.show(contentPanel, "card4");  // borrowers
                    viewBorrowTable();
                }
                case 4 -> {
                    cardLayout.show(contentPanel, "card6");   // overdues
                    // viewOverdueTable();
                }
//                case 5 -> {
//                    cardLayout.show(contentPanel, "card6"); // profile
//                }
            }
        });
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

    public void viewOverdueTable() {
        model = (DefaultTableModel) overdueTable.getModel();
        model.setRowCount(0);
        ArrayList<Overdue> overdues = database.getOverdues();
        if (overdues == null) {
            return;
        }
        Object[] object = new Object[5];
        for (Overdue overdue : overdues) {
            object[0] = overdue.getOverdueId();
            object[1] = overdue.getBookTitle();
            object[2] = overdue.getUserFirstName();
            object[3] = overdue.getUserLastName();
            object[4] = overdue.getFine();
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
                break;
            case "contactNumber":
                user.setContactNumber(attributeValue);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        menu = new com.lbms.app.swing.Menu(database.getCurrentUser(loggedId).getUserType());
        contentPanel = new com.lbms.app.swing.ContentPanel();
        card1 = new com.lbms.app.swing.ContentPanel();
        userSearchBar = new com.lbms.app.swing.SearchBar();
        userAddButton = new javax.swing.JButton();
        userRemoveButton = new javax.swing.JButton();
        userScrollPane = new javax.swing.JScrollPane();
        userTable = new com.lbms.app.swing.Table();
        card2 = new com.lbms.app.swing.ContentPanel();
        bookSearchBar = new com.lbms.app.swing.SearchBar();
        bookAddButton = new javax.swing.JButton();
        bookRemoveButton = new javax.swing.JButton();
        bookScrollPane = new javax.swing.JScrollPane();
        bookTable = new com.lbms.app.swing.Table();
        card3 = new com.lbms.app.swing.ContentPanel();
        requestSearchBar = new com.lbms.app.swing.SearchBar();
        requestApproveButton = new javax.swing.JButton();
        requestRejectButton = new javax.swing.JButton();
        requestScrollPane = new javax.swing.JScrollPane();
        requestTable = new com.lbms.app.swing.Table();
        card4 = new com.lbms.app.swing.ContentPanel();
        borrowSearchBar = new com.lbms.app.swing.SearchBar();
        borrowReturnButton = new javax.swing.JButton();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new com.lbms.app.swing.Table();
        card5 = new com.lbms.app.swing.ContentPanel();
        overdueSearchBar = new com.lbms.app.swing.SearchBar();
        overdueScrollPane = new javax.swing.JScrollPane();
        overdueTable = new com.lbms.app.swing.Table();
        overdueButton = new javax.swing.JButton();
        card6 = new com.lbms.app.swing.ContentPanel();
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
        updateLabel = new javax.swing.JLabel();
        userAddPanel = new com.lbms.app.swing.ContentPanel();
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
        bookAddPanel = new com.lbms.app.swing.ContentPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(48, 51, 54));

        contentPanel.setLayout(new java.awt.CardLayout());

        userSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        userAddButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        userAddButton.setText("ADD USER ACCOUNT");
        userAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userAddButtonActionPerformed(evt);
            }
        });

        userRemoveButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        userRemoveButton.setText("REMOVE USER ACCOUNT");
        userRemoveButton.setEnabled(false);
        userRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userRemoveButtonActionPerformed(evt);
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

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                        .addComponent(userAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(userRemoveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(userScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userSearchBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(userSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(userScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userRemoveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card1, "card1");

        bookSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        bookAddButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        bookAddButton.setText("ADD BOOK");
        bookAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookAddButtonActionPerformed(evt);
            }
        });

        bookRemoveButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        bookRemoveButton.setText("REMOVE BOOK");
        bookRemoveButton.setEnabled(false);
        bookRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookRemoveButtonActionPerformed(evt);
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

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card2Layout.createSequentialGroup()
                        .addComponent(bookAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(bookRemoveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(card2Layout.createSequentialGroup()
                        .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bookScrollPane)
                            .addComponent(bookSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(bookScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookRemoveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card2, "card2");

        requestSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        requestApproveButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        requestApproveButton.setText("APPROVE REQUEST");
        requestApproveButton.setEnabled(false);
        requestApproveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestApproveButtonActionPerformed(evt);
            }
        });

        requestRejectButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        requestRejectButton.setText("REJECT REQUEST");
        requestRejectButton.setEnabled(false);
        requestRejectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestRejectButtonActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card3Layout.createSequentialGroup()
                        .addComponent(requestApproveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(requestRejectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(requestScrollPane)
                        .addComponent(requestSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(requestSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(requestScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requestApproveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requestRejectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        contentPanel.add(card3, "card3");

        borrowSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        borrowReturnButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        borrowReturnButton.setText("RETURN BOOK");
        borrowReturnButton.setEnabled(false);
        borrowReturnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowReturnButtonActionPerformed(evt);
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

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card4Layout.createSequentialGroup()
                        .addComponent(borrowReturnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(card4Layout.createSequentialGroup()
                        .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(borrowScrollPane)
                            .addComponent(borrowSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(borrowSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(borrowScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(borrowReturnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card4, "card4");

        overdueSearchBar.setBackground(new java.awt.Color(70, 73, 75));

        overdueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Overdue ID", "Book Title", "First Name", "Last Name", "Fine"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        overdueScrollPane.setViewportView(overdueTable);

        overdueButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        overdueButton.setText("SETTLE FINE");

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card5Layout.createSequentialGroup()
                        .addComponent(overdueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(overdueSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(overdueScrollPane))
                .addGap(25, 25, 25))
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(overdueSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(overdueScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(overdueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card5, "card5");

        profileConfirmButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        profileConfirmButton.setText("UPDATE");
        profileConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileConfirmButtonActionPerformed(evt);
            }
        });

        passwordEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        passwordEditLabel.setText("Password");

        firstNameEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        firstNameEditLabel.setText("First Name");

        lastNameEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        lastNameEditLabel.setText("Last Name");

        emailEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        emailEditLabel.setText("Email Address");

        addressEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        addressEditLabel.setText("Address");

        contactEditLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        contactEditLabel.setText("Contact Number");

        updateLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 18)); // NOI18N
        updateLabel.setText("Update Profile");

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordEditLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(675, Short.MAX_VALUE))
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(updateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
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
                .addComponent(contactEditField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE)
                .addComponent(profileConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(card6, "card6");

        userIdLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        userIdLabel.setText("User ID (System Generated)");

        userIdField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        userIdField.setEnabled(false);

        passwordLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        passwordLabel.setText("Password");

        passwordField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        firstNameLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        firstNameLabel.setText("First Name");

        firstNameField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        lastNameLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        lastNameLabel.setText("Last Name");

        lastNameField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        emailLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        emailLabel.setText("Email Address");

        emailField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        addressLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        addressLabel.setText("Address");

        addressField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        contactLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        contactLabel.setText("Contact Number");

        contactField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        courseLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        courseLabel.setText("Course");

        courseCombo.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        courseCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "BSCS", "BSIT" }));

        yearLevelLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        yearLevelLabel.setText("Year level");

        yearLevelCombo.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        yearLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "1", "2", "3", "4" }));

        userTypeLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        userTypeLabel.setText("User Type");

        buttonGroup1.add(studentRadioButton);
        studentRadioButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        studentRadioButton.setSelected(true);
        studentRadioButton.setText("Student");

        buttonGroup1.add(librarianRadioButton);
        librarianRadioButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        librarianRadioButton.setText("Librarian");

        addUserButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        addUserButton.setText("ADD");
        addUserButton.setEnabled(false);
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
            }
        });

        cancelUserButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        cancelUserButton.setText("CANCEL");
        cancelUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userAddPanelLayout = new javax.swing.GroupLayout(userAddPanel);
        userAddPanel.setLayout(userAddPanelLayout);
        userAddPanelLayout.setHorizontalGroup(
            userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userAddPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(userAddPanelLayout.createSequentialGroup()
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
                    .addGroup(userAddPanelLayout.createSequentialGroup()
                        .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(yearLevelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(675, Short.MAX_VALUE))
        );
        userAddPanelLayout.setVerticalGroup(
            userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userAddPanelLayout.createSequentialGroup()
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
                .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearLevelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(userTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(userAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(studentRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(librarianRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cancelUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        contentPanel.add(userAddPanel, "card_useradd");

        bookIdLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        bookIdLabel.setText("Book ID (System Generated)");

        bookIdField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        bookIdField.setEnabled(false);

        titleLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        titleLabel.setText("Title");

        titleField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        authorLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        authorLabel.setText("Author");

        authorField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        isbnLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        isbnLabel.setText("ISBN");

        isbnField.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N

        addBookButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        addBookButton.setText("ADD");
        addBookButton.setEnabled(false);
        addBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookButtonActionPerformed(evt);
            }
        });

        cancelBookButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        cancelBookButton.setText("CANCEL");
        cancelBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBookButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bookAddPanelLayout = new javax.swing.GroupLayout(bookAddPanel);
        bookAddPanel.setLayout(bookAddPanelLayout);
        bookAddPanelLayout.setHorizontalGroup(
            bookAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookAddPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(bookAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
        bookAddPanelLayout.setVerticalGroup(
            bookAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookAddPanelLayout.createSequentialGroup()
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

        contentPanel.add(bookAddPanel, "card_bookadd");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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

    private void bookRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookRemoveButtonActionPerformed
        int index = bookTable.getSelectedRow();
        int bookId = Integer.parseInt(bookTable.getValueAt(index, 0).toString());

        if (database.deleteBook(bookId)) {
            JOptionPane.showMessageDialog(null, "Book has been deleted!");
        }

        bookRemoveButton.setEnabled(false); // set enable to false once row is deleted
        viewBookTable();
    }//GEN-LAST:event_bookRemoveButtonActionPerformed

    private void bookAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookAddButtonActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card_bookadd");
    }//GEN-LAST:event_bookAddButtonActionPerformed

    private void userRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userRemoveButtonActionPerformed
        int index = userTable.getSelectedRow();
        int userId = Integer.parseInt(userTable.getValueAt(index, 0).toString());

        if (database.deleteUser(userId)) {
            JOptionPane.showMessageDialog(null, "User account has been deleted!");
        }

        userRemoveButton.setEnabled(false); // set enable to false once row is deleted
        viewUserTable();
    }//GEN-LAST:event_userRemoveButtonActionPerformed

    private void userAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userAddButtonActionPerformed
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "card_useradd");
    }//GEN-LAST:event_userAddButtonActionPerformed

    private void borrowReturnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowReturnButtonActionPerformed
        int selectedIndex = borrowTable.getSelectedRow();
        int borrowId = Integer.parseInt(borrowTable.getValueAt(selectedIndex, 0).toString());
        database.returnBook(borrowId);
        viewBorrowTable();
    }//GEN-LAST:event_borrowReturnButtonActionPerformed

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

    private void requestApproveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestApproveButtonActionPerformed
        int selectedIndex = requestTable.getSelectedRow();
        int requestId = Integer.parseInt(requestTable.getValueAt(selectedIndex, 0).toString());
        int duration = Integer.parseInt(requestTable.getValueAt(selectedIndex, 4).toString());

        Request request = database.getRequest(requestId);
        Borrow borrowRequest = new Borrow();
        borrowRequest.setId(requestId);
        borrowRequest.setBookId(request.getBookId());
        borrowRequest.setUserId(request.getUserId());
        borrowRequest.setDuration(duration);

        LocalDate now = LocalDate.now();
        LocalDate sum = now.plusDays(duration);

        String startDate = now.toString();
        String endDate = sum.toString();

        borrowRequest.setStartDate(startDate);
        borrowRequest.setEndDate(endDate);

        database.insertRequest(borrowRequest, "borrow");
        database.updateBookStatus(request.getBookId());
        if (database.deleteRequest("request", requestId)) {
            database.resetAutoIncrement("request");
        }
        viewRequestTable();
    }//GEN-LAST:event_requestApproveButtonActionPerformed

    private void requestRejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestRejectButtonActionPerformed
        int selectedIndex = requestTable.getSelectedRow();
        int requestId = Integer.parseInt(requestTable.getValueAt(selectedIndex, 0).toString());

        Request request = database.getRequest(requestId);

        if (database.deleteRequest("request", requestId)) {
            database.resetAutoIncrement("request");
        }
        database.checkForReserveAfterReject(request.getBookId());
        viewRequestTable();
    }//GEN-LAST:event_requestRejectButtonActionPerformed

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
    private javax.swing.JTextField addressEditField;
    private javax.swing.JLabel addressEditLabel;
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField authorField;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JButton bookAddButton;
    private com.lbms.app.swing.ContentPanel bookAddPanel;
    private javax.swing.JTextField bookIdField;
    private javax.swing.JLabel bookIdLabel;
    private javax.swing.JButton bookRemoveButton;
    private javax.swing.JScrollPane bookScrollPane;
    private com.lbms.app.swing.SearchBar bookSearchBar;
    private com.lbms.app.swing.Table bookTable;
    private javax.swing.JButton borrowReturnButton;
    private javax.swing.JScrollPane borrowScrollPane;
    private com.lbms.app.swing.SearchBar borrowSearchBar;
    private com.lbms.app.swing.Table borrowTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelBookButton;
    private javax.swing.JButton cancelUserButton;
    private com.lbms.app.swing.ContentPanel card1;
    private com.lbms.app.swing.ContentPanel card2;
    private com.lbms.app.swing.ContentPanel card3;
    private com.lbms.app.swing.ContentPanel card4;
    private com.lbms.app.swing.ContentPanel card5;
    private com.lbms.app.swing.ContentPanel card6;
    private javax.swing.JTextField contactEditField;
    private javax.swing.JLabel contactEditLabel;
    private javax.swing.JTextField contactField;
    private javax.swing.JLabel contactLabel;
    private com.lbms.app.swing.ContentPanel contentPanel;
    private javax.swing.JComboBox<String> courseCombo;
    private javax.swing.JLabel courseLabel;
    private javax.swing.JTextField emailEditField;
    private javax.swing.JLabel emailEditLabel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField firstNameEditField;
    private javax.swing.JLabel firstNameEditLabel;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField isbnField;
    private javax.swing.JLabel isbnLabel;
    private javax.swing.JTextField lastNameEditField;
    private javax.swing.JLabel lastNameEditLabel;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JRadioButton librarianRadioButton;
    private javax.swing.JPanel mainPanel;
    private com.lbms.app.swing.Menu menu;
    private javax.swing.JButton overdueButton;
    private javax.swing.JScrollPane overdueScrollPane;
    private com.lbms.app.swing.SearchBar overdueSearchBar;
    private com.lbms.app.swing.Table overdueTable;
    private javax.swing.JPasswordField passwordEditField;
    private javax.swing.JLabel passwordEditLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton profileConfirmButton;
    private javax.swing.JButton requestApproveButton;
    private javax.swing.JButton requestRejectButton;
    private javax.swing.JScrollPane requestScrollPane;
    private com.lbms.app.swing.SearchBar requestSearchBar;
    private com.lbms.app.swing.Table requestTable;
    private javax.swing.JRadioButton studentRadioButton;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel updateLabel;
    private javax.swing.JButton userAddButton;
    private com.lbms.app.swing.ContentPanel userAddPanel;
    private javax.swing.JTextField userIdField;
    private javax.swing.JLabel userIdLabel;
    private javax.swing.JButton userRemoveButton;
    private javax.swing.JScrollPane userScrollPane;
    private com.lbms.app.swing.SearchBar userSearchBar;
    private com.lbms.app.swing.Table userTable;
    private javax.swing.JLabel userTypeLabel;
    private javax.swing.JComboBox<String> yearLevelCombo;
    private javax.swing.JLabel yearLevelLabel;
    // End of variables declaration//GEN-END:variables
}

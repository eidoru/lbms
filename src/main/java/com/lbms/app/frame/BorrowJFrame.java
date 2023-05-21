package com.lbms.app.frame;

import com.lbms.app.database.Database;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public final class BorrowJFrame extends javax.swing.JFrame {

    private StudentJFrame studentJFrame;
    private Database database;
    private int bookId;
    private int userId;
    private String title;
    private String author;

    public BorrowJFrame(int bookId, int userId, String title, String author) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.author = author;

        database = new Database();

        setResizable(false);

        // init methods
        initComponents();
        initTextFields();

        // init listener
        textFieldsListener(borrowButton, durationField);
    }

    private BorrowJFrame() {
        initComponents();
    }

    public void setStudentJFrame(StudentJFrame studentJFrame) {
        this.studentJFrame = studentJFrame;
    }

    public void initTextFields() {
        bookIdField.setText(String.valueOf(bookId));
        titleField.setText(title);
        authorField.setText(author);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bookIdLabel = new javax.swing.JLabel();
        bookIdField = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        authorLabel = new javax.swing.JLabel();
        authorField = new javax.swing.JTextField();
        durationLabel = new javax.swing.JLabel();
        durationField = new javax.swing.JTextField();
        borrowButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        windowLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bookIdLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        bookIdLabel.setText("Book ID");

        bookIdField.setFont(new java.awt.Font("SF Pro Display Light", 0, 12)); // NOI18N
        bookIdField.setEnabled(false);

        titleLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        titleLabel.setText("Title");

        titleField.setFont(new java.awt.Font("SF Pro Display Light", 0, 12)); // NOI18N
        titleField.setEnabled(false);

        authorLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        authorLabel.setText("Author");

        authorField.setFont(new java.awt.Font("SF Pro Display Light", 0, 12)); // NOI18N
        authorField.setEnabled(false);

        durationLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        durationLabel.setText("Duration");

        durationField.setFont(new java.awt.Font("SF Pro Display Light", 0, 12)); // NOI18N

        borrowButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        borrowButton.setText("BORROW");
        borrowButton.setEnabled(false);
        borrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("SF Pro Text Light", 0, 12)); // NOI18N
        cancelButton.setText("CANCEL");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        windowLabel.setFont(new java.awt.Font("SF Pro Text Light", 0, 18)); // NOI18N
        windowLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        windowLabel.setText("Borrow Book");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bookIdLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookIdField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(borrowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(durationField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(durationLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(windowLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(windowLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
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
                .addComponent(durationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(durationField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void borrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowButtonActionPerformed
        int duration = Integer.parseInt(durationField.getText());
        int response = JOptionPane.showConfirmDialog(this, "Confirm Book Request?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            database.sendBookRequest(bookId, userId, duration);
            JOptionPane.showMessageDialog(this, "Book request sent! Please wait for the librarian to accept your request.");
            if (studentJFrame != null) {
                studentJFrame.onBorrowEvent();
            }
            this.dispose();
        }
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
            java.util.logging.Logger.getLogger(BorrowJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BorrowJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BorrowJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BorrowJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BorrowJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authorField;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JTextField bookIdField;
    private javax.swing.JLabel bookIdLabel;
    private javax.swing.JButton borrowButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField durationField;
    private javax.swing.JLabel durationLabel;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel windowLabel;
    // End of variables declaration//GEN-END:variables
}

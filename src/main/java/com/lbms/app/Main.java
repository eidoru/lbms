package com.lbms.app;

import com.formdev.flatlaf.FlatDarkLaf;
import com.lbms.app.frame.LoginJFrame;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("RootPane.background", new Color(48, 51, 54));
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        LoginJFrame loginJFrame = new LoginJFrame();
        loginJFrame.setVisible(true);
    }
}

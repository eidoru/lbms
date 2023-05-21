package com.lbms.app.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setShowVerticalLines(true);
        setRowHeight(40);
        setSelectionBackground(new Color(97, 99, 101));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setFocusable(false);

        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new TableHeader(value.toString());
            }
        });

        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 20, 0, 0));
                return cellComponent;
            }
        });
    }
}

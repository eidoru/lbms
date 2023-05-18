package com.lbms.app.panel;

import com.lbms.app.event.MenuItemSelectEvent;
import com.lbms.app.object.MenuModel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

@SuppressWarnings("unchecked")
public class MenuList<E extends Object> extends JList<E> {

    private MenuItemSelectEvent event;
    private final DefaultListModel model;
    private int selectedIndex = 0;

    public MenuList() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onSelect(e);
            }
        });
    }
    
    public void onMenuItemSelect(MenuItemSelectEvent event) {
        this.event = event;
    }
    
    public void onSelect(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int index = locationToIndex(e.getPoint());
            Object object = model.getElementAt(index);
            if (object instanceof MenuModel) {
                MenuModel menu = (MenuModel) object;
                if (menu.getType() == MenuModel.MenuType.MENU) {
                    selectedIndex = index;
                    if (event != null) {
                        event.selected(index);
                    }
                }
                repaint();
            }
        }
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                MenuModel data;
                if (value instanceof MenuModel) {
                    data = (MenuModel) value;
                } else {
                    data = new MenuModel("", value + "", MenuModel.MenuType.EMPTY);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                return item;
            }
        };
    }

    public void addItem(MenuModel data) {
        model.addElement(data);
    }
    
    
}

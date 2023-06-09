package com.lbms.app.swing;

import com.lbms.app.object.MenuModel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import com.lbms.app.event.OnMenuItemSelectEvent;

public class MenuList<E extends Object> extends JList<E> {
    
    private OnMenuItemSelectEvent event;
    private final DefaultListModel model;
    private int selectedIndex = 0;
    
    public MenuList() {
        model = new DefaultListModel();
        super.setModel(model);
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onSelect(e);
            }
        });
    }
    
    public void onMenuItemSelect(OnMenuItemSelectEvent event) {
        this.event = event;
    }
    
    public void onSelect(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }
        int index = locationToIndex(e.getPoint());
        Object object = model.getElementAt(index);
        if (!(object instanceof MenuModel)) {
            return;
        }
        MenuModel menu = (MenuModel) object;
        if (menu.getType() == MenuModel.MenuType.MENU) {
            selectedIndex = index;
            if (event != null) {
                event.selected(index);
            }
        }
        repaint();
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
                    data = new MenuModel("person", value + "", MenuModel.MenuType.MENU);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                return item;
            }
        };
    }

//    @Override
//    public void setModel(ListModel<E> listModel) {
//        for (int i = 0; i < listModel.getSize(); i++) {
//            model.addElement(listModel.getElementAt(i));
//        }
//    }
    
    public void addItem(MenuModel data) {
        model.addElement(data);
    }
}

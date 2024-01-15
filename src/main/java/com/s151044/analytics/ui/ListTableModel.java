package com.s151044.analytics.ui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ListTableModel extends AbstractTableModel {
    private final List<ListTableColumn<?>> columns;
    public ListTableModel(List<ListTableColumn<?>> cols) {
        columns = new ArrayList<>(cols);
    }
    @Override
    public int getRowCount() {
        return columns.get(0).size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return columns.get(col).data.get(row);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ListTableColumn col = columns.get(columnIndex); // raw types: runtime type checked by explicit check below
        checkClasses(col.getColumnClass(), aValue.getClass());
        col.set(rowIndex, aValue); // unchecked insertion, as above
    }
    public void setRow(int rowIndex, Object... objects) {
        checkBounds(objects);
        for (int i = 0; i < objects.length; i++) {
            setValueAt(objects[i], rowIndex, i);
        }
    }
    public void removeRow() {
        int len = columns.get(0).size() - 1;
        if (len < 0) {
            throw new IllegalStateException("Unable to remove from empty list model.");
        }
        columns.forEach(c -> c.remove(len));
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addRow(Object... objects) {
        checkBounds(objects);
        for (int i = 0; i < columns.size(); i++) {
            ListTableColumn col = columns.get(i); // raw types: runtime type checked by explicit check below
            checkClasses(col.getColumnClass(), objects[i].getClass());
            col.add(objects[i]); // unchecked insertion, as above
        }
    }

    @Override
    public int findColumn(String columnName) {
        return columns.stream().map(ListTableColumn::getName).toList().indexOf(columnName);
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnClass();
    }
    public static class ListTableColumn<T> {
        private final List<T> data = new ArrayList<>();
        private final Class<T> clazz;
        private final String name;
        public ListTableColumn(String name, Class<T> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        public void add(T t) {
            data.add(t);
        }
        public int size() {
            return data.size();
        }
        public T set(int i, T t) {
            return data.set(i, t);
        }
        public T remove(int i) {
            return data.remove(i);
        }
        public boolean remove(T t) {
            return data.remove(t);
        }
        public Class<T> getColumnClass() {
            return clazz;
        }
        public String getName() {
            return name;
        }
    }
    private void checkBounds(Object... objects) {
        if (objects.length != columns.size()) {
            throw new IllegalArgumentException("Mismatched lengths: Expected " +
                    columns.size() + " arguments but got " + objects.length);
        }
    }
    private void checkClasses(Class<?> expected, Class<?> actual) {
        if (!expected.equals(actual)) {
            throw new ClassCastException("Class mismatch: Expected " +
                    expected + " but got " + actual);
        }
    }
}

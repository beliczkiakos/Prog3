package swingmvclab;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/*
 * A hallgatók adatait tároló osztály.
 */
public class StudentData extends AbstractTableModel {

    /*
     * Ez a tagváltozó tárolja a hallgatói adatokat.
     * NE MÓDOSÍTSD!
     */
    List<Student> students = new ArrayList<Student>();

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch(columnIndex) {
            case 0: return student.getName();
            case 1: return student.getNeptun();
            case 2: return student.hasSignature();
            default: return student.getGrade();
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return "Név";
            case 1: return "Neptun";
            case 2: return "Aláírás";
            case 3: return "Jegy";
            default: return "Valami";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 2) {
            return Boolean.class;
        }
        if(columnIndex == 3) {
            return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 2 || col == 3) {
            return true;
        }
        return super.isCellEditable(row, col);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch(columnIndex) {
            case 2:
                student.setSignature((Boolean) value);
                break;
            case 3:
                student.setGrade((Integer) value);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addStudent(String name, String neptun) {
        students.add(new Student(name, neptun, false, 0));
        fireTableDataChanged();
    }
}

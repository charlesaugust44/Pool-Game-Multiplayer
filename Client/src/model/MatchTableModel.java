package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MatchTableModel extends AbstractTableModel {

    private ArrayList<Match> data;
    private String[] header = {"Group IP", "Capacity", "Status"};

    public MatchTableModel(ArrayList<Match> data) {
        this.data = data;
        this.header = header;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public void setValueAt(Match aValue, int rowIndex) {
        Match match = data.get(rowIndex);

        match.setGroupIp(aValue.getGroupIp());
        match.setMaxPlayers(aValue.getMaxPlayers());
        match.setStatus(aValue.getStatus());
        match.setTotalPlayers(aValue.getTotalPlayers());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        setValueAt((Match) aValue, rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Match match = data.get(rowIndex);
        String valueObject = null;
        switch (columnIndex) {
            case 0:
                valueObject = match.getGroupIp();
                break;
            case 1:
                valueObject = match.getTotalPlayers() + "/" + match.getMaxPlayers();
                break;
            case 2:
                valueObject = match.getStatus();
                break;
            default:
                throw new IndexOutOfBoundsException("Column `" + columnIndex + "` doesn't exist");
        }

        return valueObject;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}

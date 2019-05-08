package model;

import game.objects.Player;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class PlayerTableModel extends AbstractTableModel {

    private ArrayList<Player> data;
    private String[] header = {"User", "Total Points", "Total Lost", "Total Won"};

    public PlayerTableModel(ArrayList<Player> data) {
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
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public void setValueAt(Player aValue, int rowIndex) {
        Player player = data.get(rowIndex);

        player.setUser(aValue.getUser());
        player.setTotalWon(aValue.getTotalWon());
        player.setTotalPoints(aValue.getTotalPoints());
        player.setTotalLost(aValue.getTotalLost());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
        fireTableCellUpdated(rowIndex, 3);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        setValueAt((Player) aValue, rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Player player = data.get(rowIndex);
        Object valueObject = null;
        switch (columnIndex) {
            case 0:
                valueObject = player.getUser();
                break;
            case 1:
                valueObject = player.getTotalPoints();
                break;
            case 2:
                valueObject = player.getTotalLost();
                break;
            case 3:
                valueObject = player.getTotalWon();
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

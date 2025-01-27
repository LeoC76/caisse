package caisse;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class buttonEditorSUPP extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private JButton button;
    private String label;
    private boolean isPushed;
    private int row;
    private panier parent;

    public buttonEditorSUPP(JTable table, panier parent) {
        this.parent = parent;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "Supprimer" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPushed) {
            parent.supprimerProduit(row);
        }
        fireEditingStopped(); // Termine l'édition pour permettre un nouveau clic
    }
}

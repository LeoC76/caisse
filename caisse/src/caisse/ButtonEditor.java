package caisse;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private JButton button;
    private String label;
    private boolean isPushed;
    private int row;
    private JTable table;
    private liste_produit parent;
    private panier panier;

    public ButtonEditor(JTable table, liste_produit parent) {
        this.table = table;
        this.parent = parent;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label); // Le texte du bouton est déterminant
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
            // Différencier l'action en fonction du texte du bouton
            if ("Ajouter".equals(label)) {
                String productName = (String) table.getValueAt(row, 0);
                parent.showQuantityDialog(productName);
            } else if ("Supprimer".equals(label)) {
                panier.supprimerProduit(row);
            }
        }
        fireEditingStopped(); // Termine l'édition
    }
}
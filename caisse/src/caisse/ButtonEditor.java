package caisse;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {

    private JButton button;
    private String label;
    private boolean isPushed;
    private liste_produit parent;

    public ButtonEditor(JTable table, liste_produit parent) {
        super(new JTextField()); // Appel du constructeur parent avec un JTextField
        this.parent = parent; // Référence à la classe principale
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddToCart(); // Ajouter au panier
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Si le bouton est cliqué, gérer l'ajout au panier
        }
        isPushed = false;
        return label;
    }

    private void onAddToCart() {
        String productName = (String) parent.tableModel.getValueAt(parent.table.getSelectedRow(), 0);
        parent.showQuantityDialog(productName);
    }
}

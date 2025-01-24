package caisse;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Éditeur personnalisé pour la colonne des boutons.
 */
public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JTable table) {
        super(new JTextField());
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> onAddToCart());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "Ajouter" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = table.getSelectedRow();
            String produit = table.getValueAt(selectedRow, 0).toString();
            JOptionPane.showMessageDialog(button, "Ajouté : " + produit);
        }
        isPushed = false;
        return label;
    }
    private void onAddToCart() {
        // Appeler la fenêtre pop-up pour saisir la quantité
        String productName = (String) table.getValueAt(table.getSelectedRow(), 0);
        showQuantityDialog(productName);
    }
    private void showQuantityDialog(String productName) {
        // Créer une fenêtre pop-up pour saisir la quantité
        JDialog dialog = new JDialog();
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 150);
        
        JLabel quantityLabel = new JLabel("Quantité pour " + productName + ":");
        JTextField quantityField = new JTextField(10);
        JButton validateButton = new JButton("Valider");
        
        validateButton.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                // Ajouter au panier avec la quantité spécifiée (vous pouvez ajouter cette logique)
                System.out.println("Produit: " + productName + " ajouté au panier avec quantité: " + quantity);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer une quantité valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(validateButton);
        dialog.setVisible(true);
    }


    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

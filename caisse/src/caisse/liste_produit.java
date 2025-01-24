package caisse;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class liste_produit extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private DefaultComboBoxModel<String> comboModel;
	private JComboBox comboBox;
	//données table
	private List<Object[]> allData;
	/**
	 * Create the frame.
	 */
	public liste_produit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		tableModel = new DefaultTableModel(new String[]{"Nom", "Quantité", "Ajouter"}, 0);
		table = new JTable(tableModel);

		// Ajout des renderers et éditeurs après s'assurer que le modèle est défini
		table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(table));
		table.setBounds(50, 61, 329, 176);
		contentPane.add(table);
		
		comboModel = new DefaultComboBoxModel<>();
		comboBox = new JComboBox(comboModel);
		comboBox.setBounds(50, 10, 329, 22);
		contentPane.add(comboBox);
		//remplir comboBox
		remplirComboBox();
		// Ajouter les produits à la table
        filtrerParAction();
		//listener pour la comboBox
        comboBox.addActionListener(e -> filtrerParAction());
	}
	
	
	private void remplirComboBox() {
        try {
            List<String> actions = Connexion.getActions();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Tous"); // Option pour afficher tous les produits
            for (String action : actions) {
                model.addElement(action); // Ajout des libellés à la comboBox
            }
            comboBox.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	private void filtrerParAction() {
        String selectedAction = (String) comboBox.getSelectedItem();

        // Vider le modèle de la table
        tableModel.setRowCount(0);

        try {
            if ("Tous".equals(selectedAction)) {
                // Si "Tous" est sélectionné, afficher tous les produits
                List<String> produits = Connexion.getProduits();
                for (int i = 0; i < produits.size(); i += 2) {
                    tableModel.addRow(new Object[]{produits.get(i), produits.get(i + 1)});
                }
            } else {
                // Récupérer les produits filtrés par l'action sélectionnée
                List<String[]> produitsFiltres = Connexion.getProduitsParAction(selectedAction);
                for (String[] produit : produitsFiltres) {
                    tableModel.addRow(produit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

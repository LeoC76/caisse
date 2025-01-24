package caisse;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;

public class liste_produit extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private JTextField textField;
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
		tableModel = new DefaultTableModel(new String[]{"Nom", "Quantité"}, 0);
        table = new JTable(tableModel);
		table.setBounds(50, 61, 329, 176);
		contentPane.add(table);
		
		textField = new JTextField();
		textField.setBounds(175, 11, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(69, 10, 89, 23);
		contentPane.add(btnNewButton);
		// Ajouter les produits à la table
        remplirTable();
		
		
	}
	
	private void remplirTable() {
		
        List<String> produits = Connexion.getProduits();
        for (int i = 0  ; i < produits.size(); i+=2) {
            tableModel.addRow(new Object[]{produits.get(i), produits.get(1)});
        }
    }
	
}

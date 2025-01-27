package caisse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class panier extends JFrame {
    private List<String[]> panier;  // Liste de produits du panier
    DefaultTableModel tableModel;
    JLabel totalLabel;
    private JTable table;
    // Constructeur qui prend une liste de produits
    public panier(List<String[]> panier) {
    	getContentPane().setLayout(null);
    	
    	JPanel panel = new JPanel();
    	panel.setBounds(0, 0, 434, 36);
    	getContentPane().add(panel);
    }

    // Méthode pour afficher le panier
    public void afficher() {
        String[] columns = {"Produit", "Quantité", "Prix"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Ajouter les produits du panier à la table
        for (String[] item : panier) {
            tableModel.addRow(item);  // Ajoute chaque ligne du panier à la table
        }

        // Calculer le total
        double total = 0;
        for (String[] item : panier) {
            int quantity = Integer.parseInt(item[1]);
            double price = Double.parseDouble(item[2]);
            total += quantity * price;
        }

        // Ajouter un label pour afficher le total
        totalLabel = new JLabel("Total : " + String.format("%.2f", total) + " €", JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout());
        totalPanel.add(totalLabel, BorderLayout.EAST);  // Aligner à droite
        getContentPane().add(totalPanel, BorderLayout.SOUTH);

        setVisible(true);  // Afficher la fenêtre
    }
    public double calculerTotal() {
    	// Calculer le total
        double total = 0;
        for (String[] item : panier) {
            int quantity = Integer.parseInt(item[1]);
            double price = Double.parseDouble(item[2]);
            total += quantity * price;
            
        }
        return total;
    }
    public void supprimerProduit(int row) {
        panier.remove(row); // Supprime de la liste
        tableModel.removeRow(row); // Supprime de la table
        totalLabel.setText("Total : " + calculerTotal() + " €"); // Met à jour le total
    }
}

package caisse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class panier extends JFrame {
    private List<String[]> panier;  // Liste de produits du panier

    // Constructeur qui prend une liste de produits
    public panier(List<String[]> panier) {
        this.panier = panier;
        setTitle("Panier");
        setSize(400, 250);  // Ajuster la taille de la fenêtre selon vos besoins
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrer la fenêtre
    }

    // Méthode pour afficher le panier
    public void afficher() {
        String[] columns = {"Produit", "Quantité", "Prix"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Ajouter les produits du panier à la table
        for (String[] item : panier) {
            tableModel.addRow(item);  // Ajoute chaque ligne du panier à la table
        }

        // Créer un JScrollPane pour la table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Calculer le total
        double total = 0;
        for (String[] item : panier) {
            int quantity = Integer.parseInt(item[1]);
            double price = Double.parseDouble(item[2]);
            total += quantity * price;
        }

        // Ajouter un label pour afficher le total
        JLabel totalLabel = new JLabel("Total : " + String.format("%.2f", total) + " €", JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout());
        totalPanel.add(totalLabel, BorderLayout.EAST);  // Aligner à droite
        add(totalPanel, BorderLayout.SOUTH);

        setVisible(true);  // Afficher la fenêtre
    }
}

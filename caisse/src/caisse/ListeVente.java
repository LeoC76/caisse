package caisse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ListeVente extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ListeVente() {
        setTitle("Liste des Ventes");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Définition des colonnes
        String[] colonnes = {"Date", "Produit", "Quantité Vendue", "Prix Unitaire", "Total", "Type de Paiement", "Stock Restant"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        // Barre d'outils avec boutons
        JToolBar toolBar = new JToolBar();
        JButton refreshButton = new JButton("Rafraîchir");
        JButton exportButton = new JButton("Exporter en CSV");

        toolBar.add(refreshButton);
        toolBar.add(exportButton);

        // Ajouter les actions aux boutons
        refreshButton.addActionListener(e -> chargerVentes());
        exportButton.addActionListener(e -> exporterCSV());

        // Ajouter la barre d'outils et la table à la fenêtre
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Charger les ventes initialement
        chargerVentes();
    }

    private void chargerVentes() {
        model.setRowCount(0); // Effacer les lignes existantes
        List<String[]> ventes = Connexion.getListeVentes();
        for (String[] vente : ventes) {
            model.addRow(vente);
        }
    }

    private void exporterCSV() {
        try (FileWriter writer = new FileWriter("ventes.csv")) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.append(model.getColumnName(i)).append(",");
            }
            writer.append("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.append(model.getValueAt(i, j).toString()).append(",");
                }
                writer.append("\n");
            }
            JOptionPane.showMessageDialog(this, "Exporté en CSV avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void afficher() {
    	setVisible(true);
    }
    
}
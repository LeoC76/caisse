package caisse;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionCategories {
    private JFrame frame;

    public GestionCategories() {
        frame = new JFrame("Gestion des Catégories");
        frame.setSize(400, 300);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JComboBox<String> categorieComboBox = new JComboBox<>();
        categorieComboBox.setBounds(110, 80, 200, 50);
        JButton supprimerCategorieButton = new JButton("Supprimer Catégorie");
        supprimerCategorieButton.setBounds(20, 157, 150, 50);
        panel.setLayout(null);

        // Charger les catégories dans le ComboBox
        loadCategories(categorieComboBox);

        // Ajouter les composants au panneau
        panel.add(categorieComboBox);
        panel.add(supprimerCategorieButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        JButton ajouterCategorieButton = new JButton("Ajouter Catégorie");
        ajouterCategorieButton.setBounds(220, 157, 150, 50);
        panel.add(ajouterCategorieButton);
        
                // Action pour ajouter une catégorie
                ajouterCategorieButton.addActionListener(e -> showCreateCategoryDialog(categorieComboBox));

        // Action pour supprimer une catégorie
        supprimerCategorieButton.addActionListener(e -> {
            String selectedCategorie = (String) categorieComboBox.getSelectedItem();
            if (selectedCategorie != null) {
                Connexion.supprimerCategorie(selectedCategorie);
                loadCategories(categorieComboBox); // Mise à jour après suppression
            } else {
                showAlert("Erreur", "Veuillez sélectionner une catégorie à supprimer.");
            }
        });
    }

    public void afficher() {
        frame.setVisible(true);
    }

    private void loadCategories(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Vider le ComboBox avant de le remplir
        List<String> ltype = Connexion.getType(); // Récupération des catégories depuis la base
        for (String type : ltype) {
            comboBox.addItem(type);
        }
    }

    

    private void showCreateCategoryDialog(JComboBox<String> categorieComboBox) {
        JDialog dialog = new JDialog(frame, "Créer une Catégorie", true);
        dialog.setSize(300, 150);
        dialog.getContentPane().setLayout(new FlowLayout());

        JLabel categorieLabel = new JLabel("Nom de la catégorie:");
        JTextField categorieField = new JTextField(20);
        JButton ajouterCategorieButton = new JButton("Ajouter");

        dialog.getContentPane().add(categorieLabel);
        dialog.getContentPane().add(categorieField);
        dialog.getContentPane().add(ajouterCategorieButton);

        ajouterCategorieButton.addActionListener(e -> {
            String nomCategorie = categorieField.getText();

            if (nomCategorie != null && !nomCategorie.isEmpty()) {
                Connexion.ajouterCategorie(nomCategorie);
                loadCategories(categorieComboBox); // Mise à jour après ajout
                dialog.dispose();
            } else {
                showAlert("Erreur", "Veuillez entrer un nom pour la catégorie.");
            }
        });

        dialog.setLocationRelativeTo(frame); // Centrer le dialogue sur la fenêtre principale
        dialog.setVisible(true);
    }

  

    public void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}

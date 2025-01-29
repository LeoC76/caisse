package caisse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestionProduits {
	private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestionProduits::new);
    }
    
    
    public GestionProduits() {
        frame = new JFrame("Gestion des Produits");
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        
        // Champs de texte pour le produit
        JLabel nomProduitLabel = new JLabel("Nom du Produit:");
        JTextField nomProduitField = new JTextField();
        JLabel quantiteProduitLabel = new JLabel("Quantite du Produit:");
        JTextField quantiteProduitField = new JTextField();
        JLabel categorieLabel = new JLabel("Catégorie:");
        JComboBox<String> categorieComboBox = new JComboBox<>();

        
        // Charger les catégories dans le ComboBox
        loadCategories(categorieComboBox);

        
        // Boutons
        JButton ajouterProduitButton = new JButton("Ajouter Produit");
        JButton creerCategorieButton = new JButton("Créer une Catégorie");

        
        // Ajouter les composants au panneau
        frame.add(nomProduitLabel);
        frame.add(nomProduitField);
        frame.add(quantiteProduitLabel);
        frame.add(quantiteProduitField);
        frame.add(categorieLabel);
        frame.add(categorieComboBox);
        frame.add(ajouterProduitButton);
        frame.add(creerCategorieButton);
        //frame.add(panel, BorderLayout.CENTER);

        
        // Action pour ajouter un produit
        ajouterProduitButton.addActionListener(e -> {
            String nomProduit = nomProduitField.getText();
            String categorie = (String) categorieComboBox.getSelectedItem();
            String quantite = quantiteProduitField.getText();
            
            if (nomProduit != null && !nomProduit.isEmpty() && categorie != null) {
                Connexion.ajouterProduit(nomProduit, Double.parseDouble(quantite), categorie);
            } else {
                showAlert("Erreur", "Veuillez entrer un nom de produit et sélectionner une catégorie.");
            }
        });

        // Action pour créer une catégorie
        creerCategorieButton.addActionListener(e -> {
            showCreateCategoryDialog(categorieComboBox); // Passer le ComboBox pour mise à jour
        });

        frame.setVisible(true);
    }
    public void afficher() {
        frame.setVisible(true);
    }

	private void loadCategories(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Vider le ComboBox avant de le remplir
        List<String> ltype = new ArrayList<>();
        ltype = Connexion.getType();
        for (String type : ltype) {
                comboBox.addItem(type);
            }
    }        

   

    private void showCreateCategoryDialog(JComboBox<String> categorieComboBox) {
        // Créer une nouvelle fenêtre pour ajouter une catégorie
        JDialog dialog = new JDialog();
        dialog.setTitle("Créer une Catégorie");
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());

        JLabel categorieLabel = new JLabel("Nom de la catégorie:");
        JTextField categorieField = new JTextField(20);
        JButton ajouterCategorieButton = new JButton("Ajouter Catégorie");

        dialog.add(categorieLabel);
        dialog.add(categorieField);
        dialog.add(ajouterCategorieButton);

        ajouterCategorieButton.addActionListener(e -> {
            String nomCategorie = categorieField.getText();

            if (nomCategorie != null && !nomCategorie.isEmpty()) {
                Connexion.ajouterCategorie(nomCategorie);
                refreshCategories(categorieComboBox); // Mettre à jour le ComboBox
                dialog.dispose(); // Fermer le dialogue
            } else {
                showAlert("Erreur", "Veuillez entrer un nom pour la catégorie.");
            }
        });

        dialog.setVisible(true);
    }

   

    private void refreshCategories(JComboBox<String> comboBox) {
        // Recharger les catégories après l'ajout
        loadCategories(comboBox);
    }

    public static void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}

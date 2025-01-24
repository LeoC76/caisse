package caisse;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;

public class liste_produit extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    public DefaultTableModel tableModel;
    public JTable table;
    private DefaultComboBoxModel<String> comboModel;
    private JComboBox comboBox;
    // Données table
    private List<Object[]> allData;

    // Liste pour stocker les produits du panier
    private List<String[]> panier = new ArrayList<>();

    public liste_produit() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        tableModel = new DefaultTableModel(new String[]{"Nom", "Quantité","Tarif", "Ajouter"}, 0);
        table = new JTable(tableModel);

        // Ajout des renderers et éditeurs après s'assurer que le modèle est défini
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(table, this)); // Passage de la référence à `liste_produit`
        table.setBounds(50, 61, 329, 176);
        contentPane.add(table);

        comboModel = new DefaultComboBoxModel<>();
        comboBox = new JComboBox(comboModel);
        comboBox.setBounds(50, 10, 329, 22);
        contentPane.add(comboBox);
        
        // Remplir comboBox
        remplirComboBox();
        
        // Ajouter les produits à la table
        filtrerParAction();
        
        // Listener pour la comboBox
        comboBox.addActionListener(e -> filtrerParAction());

        // Bouton "Voir Panier"
        JButton btnVoirPanier = new JButton("Voir Panier");
        btnVoirPanier.setBounds(50, 30, 110, 20);
        btnVoirPanier.addActionListener(e -> afficherPanier());
        contentPane.add(btnVoirPanier);
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
                    String productName = produits.get(i);
                    String productPrice = String.valueOf(Connexion.getPrixProduit(productName)); // Récupérer le prix du produit
                    tableModel.addRow(new Object[]{productName, "1", productPrice, "Ajouter"});  // Afficher le prix
                }
            } else {
                // Récupérer les produits filtrés par l'action sélectionnée
                List<String[]> produitsFiltres = Connexion.getProduitsParAction(selectedAction);
                for (String[] produit : produitsFiltres) {
                    String productName = produit[0];
                    String productPrice = String.valueOf(Connexion.getPrixProduit(productName)); // Récupérer le prix du produit
                    tableModel.addRow(new Object[]{productName, "1", productPrice, "Ajouter"});  // Afficher le prix
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void showQuantityDialog(String productName) {
        JDialog dialog = new JDialog(this, "Ajouter au panier", true);
        dialog.getContentPane().setLayout(new FlowLayout());
        dialog.setSize(300, 150);

        JLabel quantityLabel = new JLabel("Quantité pour " + productName + ":");
        JTextField quantityField = new JTextField(10);
        JButton validateButton = new JButton("Valider");

        validateButton.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                ajouterAuPanier(productName, quantity);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer une quantité valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.getContentPane().add(quantityLabel);
        dialog.getContentPane().add(quantityField);
        dialog.getContentPane().add(validateButton);
        dialog.setVisible(true);
    }
    
    // Méthode pour afficher le panier
    private void afficherPanier() {
        if (panier.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide", "Panier", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Créer une fenêtre pour afficher le panier
        JDialog panierDialog = new JDialog(this, "Panier", true);
        panierDialog.getContentPane().setLayout(new BorderLayout());
        panierDialog.setSize(300, 200);

        String[] columns = {"Produit", "Quantité"};
        DefaultTableModel panierTableModel = new DefaultTableModel(columns, 0);
        JTable panierTable = new JTable(panierTableModel);

        // Ajouter les produits du panier à la table
        for (String[] item : panier) {
            panierTableModel.addRow(item);
        }

        JScrollPane scrollPane = new JScrollPane(panierTable);
        panierDialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

        panierDialog.setVisible(true);
    }

    // Méthode pour ajouter un produit au panier
    public void ajouterAuPanier(String productName, int quantity) {
        // Vérifier si le produit existe déjà dans le panier
        for (String[] item : panier) {
            if (item[0].equals(productName)) {
                // Si le produit existe déjà, ajouter la quantité
                item[1] = String.valueOf(Integer.parseInt(item[1]) + quantity);
                return;
            }
        }
        // Si le produit n'existe pas encore, l'ajouter au panier
        panier.add(new String[]{productName, String.valueOf(quantity)});
    }
}
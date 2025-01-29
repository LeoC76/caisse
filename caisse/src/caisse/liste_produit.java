package caisse;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
    private List<String[]> Panier = new ArrayList<>();
    private JLabel lblNewLabel;

    public liste_produit() {
    	setTitle("AppliCaisse");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Récupérer la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(192, 192, 192));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        tableModel = new DefaultTableModel(new String[]{"Nom", "Quantité", "Tarif", "Ajouter"}, 0);

        comboModel = new DefaultComboBoxModel<>();
        contentPane.setLayout(null);
        comboBox = new JComboBox(comboModel);
        comboBox.setBounds(50, 52, 329, 22);
        comboBox.setBackground(new Color(128, 255, 255));
        contentPane.add(comboBox);
        
        // Remplir comboBox
        remplirComboBox();
        
        // Ajouter les produits à la table
        filtrerParAction();
        // Listener pour la comboBox
        comboBox.addActionListener(e -> filtrerParAction());
        
        // Bouton "Voir Panier"
        ImageIcon icon = new ImageIcon("C:\\\\\\\\Users\\\\\\\\lecan\\\\\\\\OneDrive\\\\\\\\Pictures\\\\\\\\panier.png\\\\");
        JButton btnVoirPanier = new JButton("Voir panier", new ImageIcon(liste_produit.class.getResource("/caisse/panier.png")));
        btnVoirPanier.setBounds(854, 10, 161, 64);
        btnVoirPanier.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnVoirPanier.setBackground(new Color(0, 128, 255));
        btnVoirPanier.addActionListener(e -> afficherPanier());
        contentPane.add(btnVoirPanier);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 81, screenSize.width-100, screenSize.height-400);
        contentPane.add(scrollPane);
        table = new JTable(tableModel);
        scrollPane.setViewportView(table);
        
                // Ajout des renderers et éditeurs après s'assurer que le modèle est défini
                table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
                table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(table, this));
                
                lblNewLabel = new JLabel("Filtrer par action :");
                lblNewLabel.setBounds(54, 27, 134, 14);
                contentPane.add(lblNewLabel);
                
                JButton btnGestion = new JButton("Gestion", null);
                btnGestion.setBounds(683, 10, 161, 64);
                btnGestion.setFont(new Font("Tahoma", Font.BOLD, 11));
                btnGestion.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		Accueil test = new Accueil();
                		test.afficher();
                	}
                });
                btnGestion.setBackground(new Color(255, 128, 64));
                contentPane.add(btnGestion);
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
                    String productQT = produits.get(i+1);
                    String productPrice = String.valueOf(Connexion.getPrixProduit(productName)); // Récupérer le prix du produit
                    tableModel.addRow(new Object[]{productName, productQT, productPrice, "Ajouter"});  // Afficher le prix
                }
            } else {
            	System.out.println("Action sélectionnée : " + selectedAction);
                // Récupérer les produits filtrés par l'action sélectionnée
                List<String[]> produitsFiltres = Connexion.getProduitsParAction(selectedAction);
                for (String[] produit : produitsFiltres) {
                    String productName = produit[0];
                    String productQT = produit[1];
                    String productPrice = String.valueOf(Connexion.getPrixProduit(productName)); // Récupérer le prix du produit
                    tableModel.addRow(new Object[]{productName, productQT, productPrice, "Ajouter"});  // Afficher le prix
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
        if (Panier.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide", "Panier", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

     // Ouvrir la fenêtre Panier
        panier panierFrame = new panier(Panier);
        panierFrame.afficher();
    }

 // Méthode pour ajouter un produit au panier
    public void ajouterAuPanier(String productName, int quantity) {
        // Récupérer le prix du produit depuis la base de données
        double prix = Connexion.getPrixProduit(productName);

        // Vérifier si le produit existe déjà dans le panier
        for (String[] item : Panier) {
            if (item[0].equals(productName)) {
                // Si le produit existe déjà, ajouter la quantité
                item[1] = String.valueOf(Integer.parseInt(item[1]) + quantity);
                return;
            }
        }

        // Si le produit n'existe pas encore, l'ajouter au panier avec le prix
        Panier.add(new String[]{productName, String.valueOf(quantity), String.valueOf(prix)});
    }
}
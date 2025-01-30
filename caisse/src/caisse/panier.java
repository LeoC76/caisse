package caisse;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class panier extends JFrame {
    private List<String[]> panier;  // Liste de produits du panier
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JComboBox comboBox;

    // Constructeur qui prend une liste de produits
    public panier(List<String[]> panier) {
        this.panier = panier; // Initialisation de la liste de produits
        setTitle("Panier");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialisation de la table
        String[] columns = {"Produit", "Quantité", "Prix"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Seule la colonne "Quantité" (index 1) est éditable
                return column == 1;
            }
        };

        JTable table = new JTable(tableModel);

        // Ajouter un écouteur pour détecter les modifications dans la table
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    // Vérifier si c'est la colonne "Quantité" qui a été modifiée
                    if (column == 1) {
                        try {
                            // Mettre à jour la quantité dans la liste `panier`
                            String newQuantityStr = tableModel.getValueAt(row, column).toString();
                            int newQuantity = Integer.parseInt(newQuantityStr);
                            panier.get(row)[1] = String.valueOf(newQuantity);

                            // Mettre à jour le total
                            mettreAJourTotal();
                        } catch (NumberFormatException ex) {
                            // Si l'utilisateur entre une valeur invalide, réinitialiser la cellule
                            JOptionPane.showMessageDialog(null, "Veuillez entrer une quantité valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            tableModel.setValueAt(panier.get(row)[1], row, column); // Restaurer la valeur précédente
                        }
                    }
                }
            }
        });
        getContentPane().setLayout(null);

        // Ajouter un panneau pour la table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 584, 269);
        getContentPane().add(scrollPane);
        JPanel totalPanel = new JPanel();
        totalPanel.setBounds(0, 402, 584, 17);
        totalPanel.setLayout(null);
        getContentPane().add(totalPanel);
        
                // Ajouter un panneau pour le total
                totalLabel = new JLabel("Total : 0.00 €", JLabel.RIGHT);
                totalLabel.setBounds(440, 298, 144, 63);
                getContentPane().add(totalLabel);
                totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
                
                JLabel lblNewLabel = new JLabel("Si quantité = 0 -> supprimer du panier");
                lblNewLabel.setBounds(10, 280, 240, 14);
                getContentPane().add(lblNewLabel);
                
                JButton btnNewButton = new JButton("Valider");
                btnNewButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		validerPanier();
                	}
                });
                btnNewButton.setBounds(220, 324, 100, 21);
                getContentPane().add(btnNewButton);
                
                comboBox = new JComboBox();
                comboBox.setBounds(20, 324, 100, 13);
                getContentPane().add(comboBox);
                
                JLabel lblNewLabel_1 = new JLabel("Type de paiement :");
                lblNewLabel_1.setBounds(20, 313, 100, 13);
                getContentPane().add(lblNewLabel_1);

        // Remplir la table et calculer le total
        remplirTable();
        remplirComboBox();
    }

    // Méthode pour remplir la table et calculer le total
    private void remplirTable() {
        // Vider le modèle de table
        tableModel.setRowCount(0);

        // Ajouter les produits du panier à la table
        for (String[] item : panier) {
            tableModel.addRow(item); // Ajouter chaque produit à la table
        }

        // Mettre à jour le total
        mettreAJourTotal();
    }
	
    private void mettreAJourTotal() {
        double total = 0;

        // Utiliser un itérateur pour éviter ConcurrentModificationException
        Iterator<String[]> iterator = panier.iterator();
        int row = 0;

        while (iterator.hasNext()) {
            String[] item = iterator.next();
            int quantity = Integer.parseInt(item[1]);
            double price = Double.parseDouble(item[2]);

            if (quantity == 0) {
                // Supprimer l'élément si la quantité est 0
                iterator.remove();
                tableModel.removeRow(row);
            } else {
                total += quantity * price;
                row++;
            }
        }

        totalLabel.setText("Total : " + String.format("%.2f", total) + " €");
    }



    // Méthode pour supprimer un produit du panier
    public void supprimerProduit(int row) {
        if (row >= 0 && row < panier.size()) {
            panier.remove(row); // Supprimer de la liste
            tableModel.removeRow(row); // Supprimer de la table
            remplirTable(); // Recalculer le total
        }
    }

    // Méthode pour afficher la fenêtre
    public void afficher() {
        setVisible(true);
    }

	private void remplirComboBox() {
	    try {
	        List<String> lpaiement = Connexion.getTypePaiement();
	        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	        model.addElement(""); // Option vide
	        for (String action : lpaiement) {
	            model.addElement(action); // Ajout des libellés à la comboBox
	        }
	        comboBox.setModel(model);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void validerPanier() {
	    int idTypeMvt = 1; // 1 correspond à une vente
	    String typePaiement = (String) comboBox.getSelectedItem();
	    int idTypePaiement = Connexion.getIdTypePaiement(typePaiement);

	    if (idTypePaiement == -1) {
	        JOptionPane.showMessageDialog(null, "Type de paiement invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    if (panier.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "Le panier est vide, aucune vente possible.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // 🔹 1. Récupération des stocks actuels
	    List<String[]> lqte = Connexion.getProduits();
	    Map<String, Double> stockProduits = new HashMap<>();

	    for (String[] produit : lqte) {
	        stockProduits.put(produit[0], Double.parseDouble(produit[1]));
	    }

	    // 🔹 2. Construire le récapitulatif du panier
	    StringBuilder recapitulatif = new StringBuilder("🛒 Récapitulatif du panier 🛒\n\n");
	    double total = 0;

	    for (String[] produit : panier) {
	        String nomProduit = produit[0];
	        double quantite = Double.parseDouble(produit[1]);
	        double prixUnitaire = Connexion.getPrixProduit(nomProduit); // Fonction à créer si besoin
	        double prixTotal = quantite * prixUnitaire;

	        recapitulatif.append(String.format("- %s : %.2f x %.2f € = %.2f €\n", nomProduit, quantite, prixUnitaire, prixTotal));
	        total += prixTotal;

	        // Vérifier si le stock est suffisant
	        if (!stockProduits.containsKey(nomProduit) || stockProduits.get(nomProduit) < quantite) {
	            JOptionPane.showMessageDialog(null, "Stock insuffisant pour : " + nomProduit, "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	    }

	    recapitulatif.append(String.format("\n💰 Total : %.2f €\n", total));

	    // 🔹 3. Demander confirmation à l'utilisateur
	    int confirmation = JOptionPane.showConfirmDialog(null, recapitulatif.toString(), "Confirmer la vente", JOptionPane.YES_NO_OPTION);

	    if (confirmation != JOptionPane.YES_OPTION) {
	        JOptionPane.showMessageDialog(null, "Vente annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }

	    // 🔹 4. Valider la vente et mettre à jour le stock
	    for (String[] produit : panier) {
	        String nomProduit = produit[0];
	        double qteMvt = Double.parseDouble(produit[1]);
	        int idPdt = Connexion.getIdProduit(nomProduit);

	        if (idPdt != -1) {
	            double nouveauStock = stockProduits.get(nomProduit) - qteMvt;

	            // Enregistrer la vente
	            Connexion.vente(idTypeMvt, qteMvt, idPdt, idTypePaiement);

	            // Mettre à jour la quantité restante
	            Connexion.updateQte(idPdt, nouveauStock);
	        } else {
	            System.err.println("Produit non trouvé : " + nomProduit);
	        }
	    }

	    // 🔹 5. Confirmation finale
	    JOptionPane.showMessageDialog(null, "✅ Vente validée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
	    
	    // Nettoyer le panier
	    panier.clear();
	    tableModel.setRowCount(0);
	    mettreAJourTotal();
	}
}

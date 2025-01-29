package caisse;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
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
                	}
                });
                btnNewButton.setBounds(220, 324, 100, 21);
                getContentPane().add(btnNewButton);
                
                comboBox = new JComboBox();
                comboBox.setBounds(10, 320, 80, 21);
                getContentPane().add(comboBox);

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
}

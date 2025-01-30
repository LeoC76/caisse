package caisse;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class AccueilGestion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Lancer l'application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AccueilGestion frame = new AccueilGestion();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Construire la fenêtre principale (Accueil).
     */
    public AccueilGestion() {
        setTitle("Accueil");
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Bouton pour la gestion des produits
        JButton btnGestionProduits = new JButton("Gestion des Produits");
        btnGestionProduits.setBackground(Color.BLACK);
        btnGestionProduits.setForeground(Color.WHITE);
        btnGestionProduits.setBounds(36, 91, 148, 70);
        btnGestionProduits.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            GestionProduits gestionProduits = new GestionProduits();
            gestionProduits.afficher();
        }));
        contentPane.add(btnGestionProduits);

        // Bouton pour la gestion des catégories
        JButton btnGestionCategories = new JButton("Gestion des Catégories");
        btnGestionCategories.setBackground(Color.BLACK);
        btnGestionCategories.setForeground(Color.WHITE);
        btnGestionCategories.setBounds(240, 91, 148, 70);
        btnGestionCategories.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            GestionCategories gestionCategories = new GestionCategories();
            gestionCategories.afficher();
        }));
        contentPane.add(btnGestionCategories);
    }

    /**
     * Afficher la fenêtre Accueil.
     */
    public void afficher() {
        setVisible(true);
    }
}

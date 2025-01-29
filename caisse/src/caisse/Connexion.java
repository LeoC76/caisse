package caisse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connexion {
    static String url = "jdbc:mysql://localhost:3306/caisse";
    static String user = "root";
    static String password = "";

    public static void main(String[] args) {
        
		// Test de la récupération des produits
        List<String[]> produits = getProduitsParAction("noel");
        System.out.println("Produit: " + produits);
        for (String[] produit : produits) {
        	System.out.println("Produit: " + produit[0] + ", Quantité: " + produit[1]);
        }
   }
    

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Récupère la liste des produits (libellé et quantité).
     */
    public static List<String> getProduits() {
        List<String> produits = new ArrayList<>();
        String query = "SELECT libelleProduit, quantitePrdt FROM produit";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                produits.add(resultSet.getString("libelleProduit"));
                produits.add(resultSet.getString("quantitePrdt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
    //recupere prix 
    public static double getPrixProduit(String productName) {
        String query = "SELECT prixPdt FROM tarif WHERE idPdt = (SELECT id FROM produit WHERE libelleProduit = ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productName);  // Passer le nom du produit
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("prixPdt");  // Retourner le prix du produit
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Retourner 0 si aucun prix trouvé
    }
    /**
     * Récupère la liste des actions disponibles.
     */
    public static List<String> getActions() {
        List<String> actions = new ArrayList<>();
        String query = "SELECT libelleVente FROM action"; // On récupère les libellés des actions

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                actions.add(resultSet.getString("libelleVente")); // Ajout des libellés à la liste
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actions;
    }
    public static List<String> getType() {
        List<String> types = new ArrayList<>();
        String query = "SELECT libelleTypePdt FROM typeproduit"; // On récupère les libellés des actions

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                types.add(resultSet.getString("libelleTypePdt")); // Ajout des libellés à la liste
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    /**
     * Récupère les produits filtrés par idAction.
     *
     * @param idAction L'ID de l'action pour le filtrage.
     * @return Une liste contenant les produits et leurs quantités.
     */
    public static List<String[]> getProduitsParAction(String idAction) {
        List<String[]> produits = new ArrayList<>();
        String query = "SELECT produit.libelleProduit, produit.quantitePrdt " +
                "FROM produit " +
                "JOIN detailaction ON produit.id = detailaction.idPdt " +
                "JOIN action ON action.id = detailaction.idAction " +
                "WHERE action.libelleVente = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, idAction);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String libelleProduit = resultSet.getString("libelleProduit");
                    String quantite = resultSet.getString("quantitePrdt");
                    produits.add(new String[]{libelleProduit, quantite});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
   
   /**
     * Ajoute un produit dans la base de données.
     */
    public static void ajouterProduit(String nomProduit, Double quantite, String type) {
        String query = "INSERT INTO produit (libelleProduit, quantitePrdt, idTypePdt) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	// Obtenir l'ID du type en fonction du libellé
            int idType = getIdType(type);

            if (idType == -1) {
                System.err.println("Type invalide : " + type);
                return;
            }

            preparedStatement.setString(1, nomProduit);
            preparedStatement.setDouble(2, quantite);
            preparedStatement.setLong(3, idType);
            preparedStatement.executeUpdate();

            System.out.println("Produit ajouté avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }
    public static Integer getIdType(String libelleType) {
        String query = "SELECT id FROM typeproduit WHERE libelleTypePdt = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, libelleType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    System.err.println("Type invalide : " + libelleType);
                    return null; // Type non trouvé
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du type : " + e.getMessage());
            return null; // Erreur de connexion ou autre
        }
    }

    /**
     * Ajoute une catégorie dans la base de données.
     */
    public static void ajouterCategorie(String nomCategorie) {
        String query = "INSERT INTO typeproduit (libelleTypePdt) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nomCategorie);
            preparedStatement.executeUpdate();
            System.out.println("Catégorie ajoutée avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la catégorie: " + e.getMessage());
        }
    }

    /**
     * Supprime une catégorie dans la base de données.
     */
    public static void supprimerCategorie(String categorie) {
        String query = "DELETE FROM typeproduit WHERE libelleTypePdt = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categorie);
            preparedStatement.executeUpdate();
            System.out.println("Catégorie supprimée avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
        }
    }
}


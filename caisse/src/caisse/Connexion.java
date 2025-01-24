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
        List<String> produits = getProduits();
        for (String produit : produits) {
            System.out.println("Produit: " + produit);
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


    /**
     * Récupère les détails des actions (produits et quantités associés).
     */
    public static List<String> getDetailAction() {
        List<String> detailActions = new ArrayList<>();
        String query = "SELECT idAction, idPdt FROM detailaction";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                detailActions.add(resultSet.getString("idAction"));
                detailActions.add(resultSet.getString("idPdt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailActions;
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
                       "JOIN detailaction da ON produit.id = da.idPdt " +
                       "WHERE da.idAction = ?";

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
}

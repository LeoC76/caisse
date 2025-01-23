package caisse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Connexion {
	static String url = "jdbc:mysql://localhost:3306/stage";
    static String user = "root";
    static String password = "";
    public static void main(String[] args) {
        // Tester la récupération des logins
        List<String> logins = getLogins();
        for (String login : logins) {
            System.out.println("Login: " + login);
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public static List<String> getLogins() {
        List<String> logins = new ArrayList<>();
        String query = "SELECT login FROM utilisateur"; // Assurez-vous que la table et la colonne existent dans la BDD

        // Utiliser le try-with-resources pour gérer les ressources
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Parcourir les résultats et ajouter les logins à la liste
            while (resultSet.next()) {
                logins.add(resultSet.getString("login"));
            }

        } catch (Exception e) {
            // Afficher une erreur en cas de problème
            e.printStackTrace();
        }

        return logins;
    }
}

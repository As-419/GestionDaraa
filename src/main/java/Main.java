import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Démarrage de l'application...");

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("default");

        System.out.println("Connexion à la base réussie.");
        System.out.println("Les tables ont été créées ou mises à jour.");

        entityManagerFactory.close();

        System.out.println("Application terminée.");
    }
}
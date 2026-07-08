package sn.l2gl.aida.daara.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Fournit une unique {@link SessionFactory} (singleton) construite a partir
 * de {@code hibernate.cfg.xml}. C'est le seul point de contact avec la
 * configuration Hibernate : le reste du code n'utilise que des Session.
 */
public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = construire();

    private HibernateUtil() {
        // classe utilitaire : pas d'instanciation
    }

    private static SessionFactory construire() {
        try {
            // Charge hibernate.cfg.xml depuis le classpath (src/main/resources).
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Echec de creation de la SessionFactory : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /** A appeler a la fermeture de l'application pour liberer les ressources. */
    public static void fermer() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}

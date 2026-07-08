package sn.l2gl.aida.daara;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import sn.l2gl.aida.daara.util.HibernateUtil;

/**
 * Base commune des tests : vide toutes les tables avant chaque test pour
 * garantir l'isolation (les tests partagent la meme base H2 en memoire).
 */
public abstract class DaaraTestBase {

    @BeforeEach
    void nettoyer() {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.createMutationQuery("delete from Progression").executeUpdate();
            s.createMutationQuery("delete from Talibe").executeUpdate();
            s.createMutationQuery("delete from Classe").executeUpdate();
            s.createMutationQuery("delete from Maitre").executeUpdate();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}

package sn.l2gl.aida.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.aida.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Base commune a tous les DAO : implemente le CRUD generique via Hibernate.
 * Les DAO concrets heritent de cette classe et ajoutent leurs recherches
 * specifiques (par critere).
 *
 * @param <T>  type de l'entite
 * @param <ID> type de la cle primaire
 */
public abstract class GenericDao<T, ID> implements Dao<T, ID> {

    private final Class<T> typeEntite;

    protected GenericDao(Class<T> typeEntite) {
        this.typeEntite = typeEntite;
    }

    /** Ouvre une nouvelle session Hibernate. */
    protected Session ouvrir() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public T inserer(T entity) {
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            s.persist(entity);
            tx.commit();
            return entity;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Optional<T> trouver(ID id) {
        try (Session s = ouvrir()) {
            return Optional.ofNullable(s.get(typeEntite, id));
        }
    }

    @Override
    public List<T> listerTous() {
        try (Session s = ouvrir()) {
            return s.createQuery("from " + typeEntite.getSimpleName(), typeEntite).list();
        }
    }

    @Override
    public Optional<T> modifier(T entity) {
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            T fusionnee = s.merge(entity);
            tx.commit();
            return Optional.of(fusionnee);
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public boolean supprimer(ID id) {
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            T entite = s.get(typeEntite, id); // lecture DANS la transaction
            if (entite == null) {
                tx.rollback();
                return false;
            }
            s.remove(entite);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}

package sn.l2gl.aida.daara.model.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface generique d'acces aux donnees.
 *
 * @param <T>  type de l'entite
 * @param <ID> type de la cle primaire
 */
public interface Dao<T, ID> {

    /** Insere une nouvelle entite et la renvoie. */
    T inserer(T entity);

    /** RECHERCHE UNIQUE par cle : au plus un resultat. */
    Optional<T> trouver(ID id);

    /** Liste toutes les entites. */
    List<T> listerTous();

    /** Met a jour une entite existante. */
    Optional<T> modifier(T entity);

    /** Supprime l'entite identifiee par sa cle ; renvoie true si supprimee. */
    boolean supprimer(ID id);
}

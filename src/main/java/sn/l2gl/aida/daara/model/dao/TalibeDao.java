package sn.l2gl.aida.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.aida.daara.exception.TalibeDejaExistantException;
import sn.l2gl.aida.daara.exception.TalibeIntrouvableException;
import sn.l2gl.aida.daara.model.models.Talibe;

import java.util.List;

/** Acces aux donnees pour l'entite {@link Talibe}. */
public class TalibeDao extends GenericDao<Talibe, String> {

    public TalibeDao() {
        super(Talibe.class);
    }

    /** Insertion : refuse un matricule deja existant. */
    @Override
    public Talibe inserer(Talibe talibe) {
        if (trouver(talibe.getMatricule()).isPresent()) {
            throw new TalibeDejaExistantException(talibe.getMatricule());
        }
        return super.inserer(talibe);
    }

    /**
     * Suppression d'un talibe : supprime d'abord ses progressions (cascade
     * applicative) puis le talibe lui-meme, dans une seule transaction.
     */
    @Override
    public boolean supprimer(String matricule) {
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Talibe talibe = s.get(Talibe.class, matricule); // lecture DANS la transaction
            if (talibe == null) {
                tx.rollback();
                return false;
            }
            s.createMutationQuery("delete from Progression p where p.talibe.matricule = :m")
                    .setParameter("m", matricule)
                    .executeUpdate();
            s.remove(talibe);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    /** RECHERCHE UNIQUE obligatoire : leve une exception si absent. */
    public Talibe trouverObligatoire(String matricule) {
        return trouver(matricule)
                .orElseThrow(() -> new TalibeIntrouvableException(matricule));
    }

    /** RECHERCHE PAR CRITERE : talibes dont le nom contient le texte. */
    public List<Talibe> rechercherParNom(String texte) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Talibe t where lower(t.nom) like lower(:n) "
                                    + "order by t.nom", Talibe.class)
                    .setParameter("n", "%" + texte + "%")
                    .list();
        }
    }

    /** RECHERCHE PAR CRITERE : talibes d'une classe donnee. */
    public List<Talibe> listerParClasse(String codeClasse) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Talibe t where t.classe.code = :c order by t.nom", Talibe.class)
                    .setParameter("c", codeClasse)
                    .list();
        }
    }
}

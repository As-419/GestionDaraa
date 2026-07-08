package sn.l2gl.aida.daara.model.dao;

import org.hibernate.Session;
import sn.l2gl.aida.daara.exception.MaitreDejaExistantException;
import sn.l2gl.aida.daara.exception.MaitreIntrouvableException;
import sn.l2gl.aida.daara.exception.SuppressionImpossibleException;
import sn.l2gl.aida.daara.model.models.Maitre;

import java.util.List;

/** Acces aux donnees pour l'entite {@link Maitre}. */
public class MaitreDao extends GenericDao<Maitre, String> {

    public MaitreDao() {
        super(Maitre.class);
    }

    /** Insertion : refuse un matricule deja existant. */
    @Override
    public Maitre inserer(Maitre maitre) {
        if (trouver(maitre.getMatricule()).isPresent()) {
            throw new MaitreDejaExistantException(maitre.getMatricule());
        }
        return super.inserer(maitre);
    }

    /** Suppression INTERDITE si le maitre encadre au moins une classe. */
    @Override
    public boolean supprimer(String matricule) {
        if (compterClasses(matricule) > 0) {
            throw new SuppressionImpossibleException(
                    "Suppression impossible : le maitre " + matricule
                            + " encadre encore au moins une classe.");
        }
        return super.supprimer(matricule);
    }

    /** RECHERCHE UNIQUE obligatoire : leve une exception si absent. */
    public Maitre trouverObligatoire(String matricule) {
        return trouver(matricule)
                .orElseThrow(() -> new MaitreIntrouvableException(matricule));
    }

    /** RECHERCHE PAR CRITERE : maitres dont le nom contient le texte. */
    public List<Maitre> rechercherParNom(String texte) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Maitre m where lower(m.nomComplet) like lower(:n) "
                                    + "order by m.nomComplet", Maitre.class)
                    .setParameter("n", "%" + texte + "%")
                    .list();
        }
    }

    /** Nombre de classes encadrees par ce maitre (controle de suppression). */
    public long compterClasses(String matricule) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "select count(c) from Classe c where c.maitre.matricule = :m", Long.class)
                    .setParameter("m", matricule)
                    .getSingleResult();
        }
    }
}

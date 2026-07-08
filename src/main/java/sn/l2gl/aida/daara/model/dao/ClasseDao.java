package sn.l2gl.aida.daara.model.dao;

import org.hibernate.Session;
import sn.l2gl.aida.daara.exception.ClasseDejaExistanteException;
import sn.l2gl.aida.daara.exception.ClasseIntrouvableException;
import sn.l2gl.aida.daara.exception.SuppressionImpossibleException;
import sn.l2gl.aida.daara.model.models.Classe;

import java.util.List;

/** Acces aux donnees pour l'entite {@link Classe}. */
public class ClasseDao extends GenericDao<Classe, String> {

    public ClasseDao() {
        super(Classe.class);
    }

    /** Insertion : refuse un code deja existant. */
    @Override
    public Classe inserer(Classe classe) {
        if (trouver(classe.getCode()).isPresent()) {
            throw new ClasseDejaExistanteException(classe.getCode());
        }
        return super.inserer(classe);
    }

    /** Suppression INTERDITE si la classe contient au moins un talibe. */
    @Override
    public boolean supprimer(String code) {
        if (compterTalibes(code) > 0) {
            throw new SuppressionImpossibleException(
                    "Suppression impossible : la classe " + code
                            + " contient encore au moins un talibe.");
        }
        return super.supprimer(code);
    }

    /** RECHERCHE UNIQUE obligatoire : leve une exception si absent. */
    public Classe trouverObligatoire(String code) {
        return trouver(code)
                .orElseThrow(() -> new ClasseIntrouvableException(code));
    }

    /** RECHERCHE PAR CRITERE : classes dont le libelle contient le texte. */
    public List<Classe> rechercherParLibelle(String texte) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Classe c where lower(c.libelle) like lower(:l) "
                                    + "order by c.libelle", Classe.class)
                    .setParameter("l", "%" + texte + "%")
                    .list();
        }
    }

    /** Nombre de talibes rattaches a cette classe (controle de suppression). */
    public long compterTalibes(String code) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "select count(t) from Talibe t where t.classe.code = :c", Long.class)
                    .setParameter("c", code)
                    .getSingleResult();
        }
    }
}

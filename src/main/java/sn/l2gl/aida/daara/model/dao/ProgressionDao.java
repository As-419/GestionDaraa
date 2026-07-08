package sn.l2gl.aida.daara.model.dao;

import org.hibernate.Session;
import sn.l2gl.aida.daara.exception.ProgressionIntrouvableException;
import sn.l2gl.aida.daara.exception.ProgressionInvalideException;
import sn.l2gl.aida.daara.model.models.Progression;

import java.util.List;

/** Acces aux donnees pour l'entite {@link Progression}. */
public class ProgressionDao extends GenericDao<Progression, Long> {

    public ProgressionDao() {
        super(Progression.class);
    }

    /** Insertion : valide les donnees metier avant d'enregistrer. */
    @Override
    public Progression inserer(Progression progression) {
        valider(progression);
        return super.inserer(progression);
    }

    /** Modification : valide les donnees metier avant d'enregistrer. */
    @Override
    public java.util.Optional<Progression> modifier(Progression progression) {
        valider(progression);
        return super.modifier(progression);
    }

    /** RECHERCHE UNIQUE obligatoire : leve une exception si absent. */
    public Progression trouverObligatoire(Long id) {
        return trouver(id)
                .orElseThrow(() -> new ProgressionIntrouvableException(id));
    }

    /** RECHERCHE PAR CRITERE : progressions d'un talibe donne. */
    public List<Progression> listerParTalibe(String matriculeTalibe) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Progression p where p.talibe.matricule = :m "
                                    + "order by p.dateEvaluation desc", Progression.class)
                    .setParameter("m", matriculeTalibe)
                    .list();
        }
    }

    /** Coherence metier : talibe present, sourate non vide, versets >= 0. */
    private void valider(Progression p) {
        if (p.getTalibe() == null) {
            throw new ProgressionInvalideException("le talibe est obligatoire.");
        }
        if (p.getSourate() == null || p.getSourate().isBlank()) {
            throw new ProgressionInvalideException("la sourate ne peut pas etre vide.");
        }
        if (p.getNombreVersets() < 0) {
            throw new ProgressionInvalideException("le nombre de versets doit etre >= 0.");
        }
    }
}

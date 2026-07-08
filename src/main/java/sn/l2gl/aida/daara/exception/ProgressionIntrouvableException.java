package sn.l2gl.aida.daara.exception;

/** Levee quand aucune progression ne correspond a l'identifiant recherche. */
public class ProgressionIntrouvableException extends DaaraException {
    public ProgressionIntrouvableException(Long id) {
        super("Aucune progression pour l'identifiant : " + id);
    }
}

package sn.l2gl.aida.daara.exception;

/** Levee quand aucun talibe ne correspond au matricule recherche. */
public class TalibeIntrouvableException extends DaaraException {
    public TalibeIntrouvableException(String matricule) {
        super("Aucun talibe pour le matricule : " + matricule);
    }
}

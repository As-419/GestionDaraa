package sn.l2gl.aida.daara.exception;

/** Levee quand aucun maitre ne correspond au matricule recherche. */
public class MaitreIntrouvableException extends DaaraException {
    public MaitreIntrouvableException(String matricule) {
        super("Aucun maitre pour le matricule : " + matricule);
    }
}

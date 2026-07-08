package sn.l2gl.aida.daara.exception;

/**
 * Levee quand les donnees d'une progression sont incoherentes :
 * nombreVersets &lt; 0, sourate vide, ou talibe non renseigne.
 */
public class ProgressionInvalideException extends DaaraException {
    public ProgressionInvalideException(String message) {
        super("Progression invalide : " + message);
    }
}

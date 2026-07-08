package sn.l2gl.aida.daara.exception;

/**
 * Levee quand une suppression est interdite a cause d'une relation existante
 * (cle etrangere) : un maitre qui encadre des classes, ou une classe qui
 * contient des talibes.
 */
public class SuppressionImpossibleException extends DaaraException {
    public SuppressionImpossibleException(String message) {
        super(message);
    }
}

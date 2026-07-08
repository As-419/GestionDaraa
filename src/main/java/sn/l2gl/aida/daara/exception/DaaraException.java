package sn.l2gl.aida.daara.exception;

/**
 * Exception de base de toute l'application Daara.
 * Toutes les exceptions metier en heritent. Etend RuntimeException
 * pour ne pas imposer de {@code throws} dans toutes les signatures.
 */
public class DaaraException extends RuntimeException {

    public DaaraException(String message) {
        super(message);
    }

    public DaaraException(String message, Throwable cause) {
        super(message, cause);
    }
}

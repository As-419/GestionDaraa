package sn.l2gl.aida.daara.exception;

/** Levee quand on insere un talibe dont le matricule existe deja. */
public class TalibeDejaExistantException extends DaaraException {
    public TalibeDejaExistantException(String matricule) {
        super("Un talibe existe deja avec le matricule : " + matricule);
    }
}

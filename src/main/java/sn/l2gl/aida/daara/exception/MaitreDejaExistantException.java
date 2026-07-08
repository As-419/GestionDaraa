package sn.l2gl.aida.daara.exception;

/** Levee quand on insere un maitre dont le matricule existe deja. */
public class MaitreDejaExistantException extends DaaraException {
    public MaitreDejaExistantException(String matricule) {
        super("Un maitre existe deja avec le matricule : " + matricule);
    }
}

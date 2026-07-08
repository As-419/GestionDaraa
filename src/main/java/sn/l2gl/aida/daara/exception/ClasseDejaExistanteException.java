package sn.l2gl.aida.daara.exception;

/** Levee quand on insere une classe dont le code existe deja. */
public class ClasseDejaExistanteException extends DaaraException {
    public ClasseDejaExistanteException(String code) {
        super("Une classe existe deja avec le code : " + code);
    }
}

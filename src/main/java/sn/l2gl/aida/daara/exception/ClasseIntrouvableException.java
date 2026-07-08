package sn.l2gl.aida.daara.exception;

/** Levee quand aucune classe ne correspond au code recherche. */
public class ClasseIntrouvableException extends DaaraException {
    public ClasseIntrouvableException(String code) {
        super("Aucune classe pour le code : " + code);
    }
}

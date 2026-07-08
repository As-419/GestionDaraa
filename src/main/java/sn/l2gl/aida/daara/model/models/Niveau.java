package sn.l2gl.aida.daara.model.models;

/** Niveau d'une classe (halqa). */
public enum Niveau {
    DEBUTANT("Debutant"),
    INTERMEDIAIRE("Intermediaire"),
    AVANCE("Avance");

    private final String libelle;

    Niveau(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}

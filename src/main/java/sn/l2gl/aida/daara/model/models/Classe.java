package sn.l2gl.aida.daara.model.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe (halqa) encadree par un maitre.
 * Cle primaire = code saisi par l'utilisateur (ex : "CL-DEB").
 */
@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @Column(length = 200, unique = true)
    private String code; // cle saisie

    @Column(nullable = false)
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;

    @ManyToOne(optional = false) // une classe est encadree par 1 maitre
    @JoinColumn(name = "maitre_matricule")
    private Maitre maitre;

    public Classe() {
    }

    public Classe(String code, String libelle, Niveau niveau, Maitre maitre) {
        this.code = code;
        this.libelle = libelle;
        this.niveau = niveau;
        this.maitre = maitre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Maitre getMaitre() {
        return maitre;
    }

    public void setMaitre(Maitre maitre) {
        this.maitre = maitre;
    }

    /** Affichage lisible dans les listes deroulantes (JComboBox). */
    @Override
    public String toString() {
        return libelle + " (" + code + ")";
    }
}

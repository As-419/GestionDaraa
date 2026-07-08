package sn.l2gl.aida.daara.model.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Maitre (serigne) qui encadre des classes.
 * Cle primaire = matricule saisi par l'utilisateur (ex : "M001").
 */
@Entity
@Table(name = "maitres")
public class Maitre {

    @Id
    @Column(length = 200, unique = true)
    private String matricule; // cle saisie

    @Column(nullable = false)
    private String nomComplet;

    private String telephone;

    public Maitre() {
    }

    public Maitre(String matricule, String nomComplet, String telephone) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.telephone = telephone;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /** Affichage lisible dans les listes deroulantes (JComboBox). */
    @Override
    public String toString() {
        return nomComplet + " (" + matricule + ")";
    }
}

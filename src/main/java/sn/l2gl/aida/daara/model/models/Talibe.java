package sn.l2gl.aida.daara.model.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Talibe (eleve) rattache a une classe.
 * Cle primaire = matricule saisi par l'utilisateur (ex : "T0001").
 */
@Entity
@Table(name = "talibes")
public class Talibe {

    @Id
    @Column(length = 200, unique = true)
    private String matricule; // cle saisie

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    private LocalDate dateNaissance;

    private String nomTuteur;

    private String telephoneTuteur;

    @ManyToOne(optional = false) // un talibe appartient a 1 classe
    @JoinColumn(name = "classe_code")
    private Classe classe;

    public Talibe() {
    }

    public Talibe(String matricule, String prenom, String nom,
                  LocalDate dateNaissance, String nomTuteur,
                  String telephoneTuteur, Classe classe) {
        this.matricule = matricule;
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.nomTuteur = nomTuteur;
        this.telephoneTuteur = telephoneTuteur;
        this.classe = classe;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNomTuteur() {
        return nomTuteur;
    }

    public void setNomTuteur(String nomTuteur) {
        this.nomTuteur = nomTuteur;
    }

    public String getTelephoneTuteur() {
        return telephoneTuteur;
    }

    public void setTelephoneTuteur(String telephoneTuteur) {
        this.telephoneTuteur = telephoneTuteur;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    /** Affichage lisible dans les listes deroulantes (JComboBox). */
    @Override
    public String toString() {
        return prenom + " " + nom + " (" + matricule + ")";
    }
}

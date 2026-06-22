package models;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "talibes")
public class Talibe {

    @Id
    @Column(length = 20)
    private String matricule;

    private String prenom;

    private String nom;

    private LocalDate dateNaissance;

    private String nomTuteur;

    private String telephoneTuteur;

    @ManyToOne(optional = false)
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

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}

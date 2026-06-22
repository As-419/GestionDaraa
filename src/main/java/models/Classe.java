package models;
import enums.Niveau;
import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @Column(length = 20)
    private String code;

    private String libelle;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @ManyToOne(optional = false)
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

    @Override
    public String toString() {
        return libelle;
    }
}

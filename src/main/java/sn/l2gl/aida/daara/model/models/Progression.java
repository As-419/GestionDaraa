package sn.l2gl.aida.daara.model.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Progression : evaluation de la memorisation du Coran pour un talibe.
 * Cle primaire AUTO-GENEREE (entier), contrairement aux autres entites.
 */
@Entity
@Table(name = "progressions")
public class Progression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // cle auto-generee

    @ManyToOne(optional = false) // une progression concerne 1 talibe
    @JoinColumn(name = "talibe_matricule")
    private Talibe talibe;

    @jakarta.persistence.Column(nullable = false)
    private String sourate;

    private int nombreVersets;

    private LocalDate dateEvaluation;

    private String appreciation;

    public Progression() {
    }

    public Progression(Talibe talibe, String sourate, int nombreVersets,
                       LocalDate dateEvaluation, String appreciation) {
        this.talibe = talibe;
        this.sourate = sourate;
        this.nombreVersets = nombreVersets;
        this.dateEvaluation = dateEvaluation;
        this.appreciation = appreciation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Talibe getTalibe() {
        return talibe;
    }

    public void setTalibe(Talibe talibe) {
        this.talibe = talibe;
    }

    public String getSourate() {
        return sourate;
    }

    public void setSourate(String sourate) {
        this.sourate = sourate;
    }

    public int getNombreVersets() {
        return nombreVersets;
    }

    public void setNombreVersets(int nombreVersets) {
        this.nombreVersets = nombreVersets;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    @Override
    public String toString() {
        return sourate + " (" + nombreVersets + " versets)";
    }
}

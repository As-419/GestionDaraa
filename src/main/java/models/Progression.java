package models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "progressions")
public class Progression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "talibe_matricule")
    private Talibe talibe;

    private String sourate;

    private int nombreVersets;

    private LocalDate dateEvaluation;

    private String appreciation;

    public Progression() {
    }

    public Progression(Talibe talibe, String sourate,
                       int nombreVersets,
                       LocalDate dateEvaluation,
                       String appreciation) {
        this.talibe = talibe;
        this.sourate = sourate;
        this.nombreVersets = nombreVersets;
        this.dateEvaluation = dateEvaluation;
        this.appreciation = appreciation;
    }

    public Long getId() {
        return id;
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
}

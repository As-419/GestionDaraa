package sn.l2gl.aida.daara.controller;

import sn.l2gl.aida.daara.exception.DaaraException;
import sn.l2gl.aida.daara.model.dao.ProgressionDao;
import sn.l2gl.aida.daara.model.dao.TalibeDao;
import sn.l2gl.aida.daara.model.models.Progression;
import sn.l2gl.aida.daara.model.models.Talibe;
import sn.l2gl.aida.daara.util.CsvExporter;
import sn.l2gl.aida.daara.view.ProgressionView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur des progressions. Alimente les listes de talibes, valide la
 * saisie (versets entier >= 0, date au bon format), capture les exceptions
 * metier et permet de filtrer la table par talibe.
 */
public class ProgressionController {

    private final ProgressionDao dao = new ProgressionDao();
    private final TalibeDao talibeDao = new TalibeDao();
    private final ProgressionView vue;
    private Progression enCours;

    public ProgressionController(ProgressionView vue) {
        this.vue = vue;

        vue.getBoutonFiltrer().addActionListener(e -> filtrer());
        vue.getBoutonToutAfficher().addActionListener(e -> rafraichir());
        vue.getBoutonNouveau().addActionListener(e -> nouveau());
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        vue.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chargerSelection();
            }
        });

        rafraichir();
    }

    public void rafraichir() {
        vue.chargerTalibes(talibeDao.listerTous());
        vue.afficher(dao.listerTous());
        nouveau();
    }

    /** Filtre la table par le talibe selectionne (recherche par critere). */
    private void filtrer() {
        Talibe talibe = (Talibe) vue.getChampFiltreTalibe().getSelectedItem();
        if (talibe == null) {
            erreur("Aucun talibe a filtrer.");
            return;
        }
        vue.afficher(dao.listerParTalibe(talibe.getMatricule()));
    }

    private void nouveau() {
        enCours = null;
        vue.reinitialiser();
    }

    private void chargerSelection() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne < 0) {
            return;
        }
        Long id = ((Number) vue.getTable().getValueAt(ligne, 0)).longValue();
        try {
            enCours = dao.trouverObligatoire(id);
            vue.remplir(enCours);
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void enregistrer() {
        Talibe talibe = (Talibe) vue.getChampTalibe().getSelectedItem();
        String sourate = vue.getChampSourate().getText().trim();
        String appreciation = vue.getChampAppreciation().getText().trim();

        if (talibe == null) {
            erreur("Selectionnez un talibe. Creez d'abord un talibe si la liste est vide.");
            return;
        }
        if (sourate.isEmpty()) {
            erreur("La sourate ne peut pas etre vide.");
            return;
        }

        int nombreVersets;
        try {
            nombreVersets = vue.lireNombreVersets();
        } catch (NumberFormatException ex) {
            erreur("Le nombre de versets doit etre un entier (ex : 7).");
            return;
        }
        if (nombreVersets < 0) {
            erreur("Le nombre de versets doit etre superieur ou egal a 0.");
            return;
        }

        LocalDate dateEvaluation;
        try {
            dateEvaluation = vue.lireDateEvaluation();
        } catch (DateTimeParseException ex) {
            erreur("Date d'evaluation invalide. Format attendu : AAAA-MM-JJ.");
            return;
        }

        try {
            if (enCours == null) {
                dao.inserer(new Progression(talibe, sourate, nombreVersets,
                        dateEvaluation, appreciation));
                info("Progression ajoutee avec succes.");
            } else {
                enCours.setTalibe(talibe);
                enCours.setSourate(sourate);
                enCours.setNombreVersets(nombreVersets);
                enCours.setDateEvaluation(dateEvaluation);
                enCours.setAppreciation(appreciation);
                dao.modifier(enCours);
                info("Progression modifiee avec succes.");
            }
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void supprimer() {
        if (enCours == null) {
            erreur("Selectionnez d'abord une progression dans la liste.");
            return;
        }
        int choix = JOptionPane.showConfirmDialog(vue,
                "Supprimer cette progression (" + enCours.getSourate() + ") ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choix != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.supprimer(enCours.getId());
            info("Progression supprimee.");
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("progressions.csv"));
        if (chooser.showSaveDialog(vue) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String[] entetes = {"id", "talibe", "sourate", "nombreVersets", "dateEvaluation", "appreciation"};
        List<String[]> lignes = new ArrayList<>();
        for (int i = 0; i < vue.getTable().getRowCount(); i++) {
            lignes.add(new String[]{
                    texte(vue.getTable().getValueAt(i, 0)),
                    texte(vue.getTable().getValueAt(i, 1)),
                    texte(vue.getTable().getValueAt(i, 2)),
                    texte(vue.getTable().getValueAt(i, 3)),
                    texte(vue.getTable().getValueAt(i, 4)),
                    texte(vue.getTable().getValueAt(i, 5))});
        }
        try {
            CsvExporter.exporter(chooser.getSelectedFile(), entetes, lignes);
            info("Export reussi : " + chooser.getSelectedFile().getName());
        } catch (IOException ex) {
            erreur("Echec de l'export : " + ex.getMessage());
        }
    }

    private String texte(Object o) {
        return o == null ? "" : o.toString();
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(vue, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void erreur(String message) {
        JOptionPane.showMessageDialog(vue, message, "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }
}

package sn.l2gl.aida.daara.controller;

import sn.l2gl.aida.daara.exception.DaaraException;
import sn.l2gl.aida.daara.model.dao.ClasseDao;
import sn.l2gl.aida.daara.model.dao.TalibeDao;
import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Talibe;
import sn.l2gl.aida.daara.util.CsvExporter;
import sn.l2gl.aida.daara.view.TalibeView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur des talibes. Alimente la liste deroulante des classes, valide
 * la saisie (classe obligatoire, date au bon format) et capture les
 * exceptions metier.
 */
public class TalibeController {

    private final TalibeDao dao = new TalibeDao();
    private final ClasseDao classeDao = new ClasseDao();
    private final TalibeView vue;
    private Talibe enCours;

    public TalibeController(TalibeView vue) {
        this.vue = vue;

        vue.getBoutonChercher().addActionListener(e -> rechercher());
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
        vue.chargerClasses(classeDao.listerTous());
        vue.afficher(dao.listerTous());
        nouveau();
    }

    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParNom(texte));
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
        String matricule = (String) vue.getTable().getValueAt(ligne, 0);
        try {
            enCours = dao.trouverObligatoire(matricule);
            vue.remplir(enCours);
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void enregistrer() {
        String matricule = vue.getChampMatricule().getText().trim();
        String prenom = vue.getChampPrenom().getText().trim();
        String nom = vue.getChampNom().getText().trim();
        String nomTuteur = vue.getChampNomTuteur().getText().trim();
        String telTuteur = vue.getChampTelephoneTuteur().getText().trim();
        Classe classe = (Classe) vue.getChampClasse().getSelectedItem();

        if (matricule.isEmpty() || prenom.isEmpty() || nom.isEmpty()) {
            erreur("Le matricule, le prenom et le nom sont obligatoires.");
            return;
        }
        if (classe == null) {
            erreur("Un talibe doit obligatoirement etre rattache a une classe. "
                    + "Creez d'abord une classe.");
            return;
        }

        LocalDate dateNaissance;
        try {
            dateNaissance = vue.lireDateNaissance();
        } catch (DateTimeParseException ex) {
            erreur("Date de naissance invalide. Format attendu : AAAA-MM-JJ (ex : 2012-05-10).");
            return;
        }

        try {
            if (enCours == null) {
                dao.inserer(new Talibe(matricule, prenom, nom, dateNaissance,
                        nomTuteur, telTuteur, classe));
                info("Talibe ajoute avec succes.");
            } else {
                enCours.setPrenom(prenom);
                enCours.setNom(nom);
                enCours.setDateNaissance(dateNaissance);
                enCours.setNomTuteur(nomTuteur);
                enCours.setTelephoneTuteur(telTuteur);
                enCours.setClasse(classe);
                dao.modifier(enCours);
                info("Talibe modifie avec succes.");
            }
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void supprimer() {
        if (enCours == null) {
            erreur("Selectionnez d'abord un talibe dans la liste.");
            return;
        }
        int choix = JOptionPane.showConfirmDialog(vue,
                "Supprimer le talibe " + enCours.getPrenom() + " " + enCours.getNom()
                        + " ? (ses progressions seront aussi supprimees)",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choix != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.supprimer(enCours.getMatricule());
            info("Talibe supprime.");
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("talibes.csv"));
        if (chooser.showSaveDialog(vue) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String[] entetes = {"matricule", "prenom", "nom", "dateNaissance", "tuteur", "classe"};
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

package sn.l2gl.aida.daara.controller;

import sn.l2gl.aida.daara.exception.DaaraException;
import sn.l2gl.aida.daara.model.dao.ClasseDao;
import sn.l2gl.aida.daara.model.dao.MaitreDao;
import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.model.models.Niveau;
import sn.l2gl.aida.daara.util.CsvExporter;
import sn.l2gl.aida.daara.view.ClasseView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur des classes. Alimente la liste deroulante des maitres,
 * valide la saisie (maitre obligatoire), appelle le DAO et capture les
 * exceptions metier.
 */
public class ClasseController {

    private final ClasseDao dao = new ClasseDao();
    private final MaitreDao maitreDao = new MaitreDao();
    private final ClasseView vue;
    private Classe enCours;

    public ClasseController(ClasseView vue) {
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

    /** Recharge la liste deroulante des maitres + la table. Public : appele a l'affichage. */
    public void rafraichir() {
        vue.chargerMaitres(maitreDao.listerTous());
        vue.afficher(dao.listerTous());
        nouveau();
    }

    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParLibelle(texte));
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
        String code = (String) vue.getTable().getValueAt(ligne, 0);
        try {
            enCours = dao.trouverObligatoire(code);
            vue.remplir(enCours);
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void enregistrer() {
        String code = vue.getChampCode().getText().trim();
        String libelle = vue.getChampLibelle().getText().trim();
        Niveau niveau = (Niveau) vue.getChampNiveau().getSelectedItem();
        Maitre maitre = (Maitre) vue.getChampMaitre().getSelectedItem();

        if (code.isEmpty() || libelle.isEmpty()) {
            erreur("Le code et le libelle sont obligatoires.");
            return;
        }
        if (maitre == null) {
            erreur("Une classe doit obligatoirement avoir un maitre. "
                    + "Creez d'abord un maitre.");
            return;
        }

        try {
            if (enCours == null) {
                dao.inserer(new Classe(code, libelle, niveau, maitre));
                info("Classe ajoutee avec succes.");
            } else {
                enCours.setLibelle(libelle);
                enCours.setNiveau(niveau);
                enCours.setMaitre(maitre);
                dao.modifier(enCours);
                info("Classe modifiee avec succes.");
            }
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void supprimer() {
        if (enCours == null) {
            erreur("Selectionnez d'abord une classe dans la liste.");
            return;
        }
        int choix = JOptionPane.showConfirmDialog(vue,
                "Supprimer la classe " + enCours.getLibelle() + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choix != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.supprimer(enCours.getCode());
            info("Classe supprimee.");
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("classes.csv"));
        if (chooser.showSaveDialog(vue) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String[] entetes = {"code", "libelle", "niveau", "maitre"};
        List<String[]> lignes = new ArrayList<>();
        for (int i = 0; i < vue.getTable().getRowCount(); i++) {
            lignes.add(new String[]{
                    texte(vue.getTable().getValueAt(i, 0)),
                    texte(vue.getTable().getValueAt(i, 1)),
                    texte(vue.getTable().getValueAt(i, 2)),
                    texte(vue.getTable().getValueAt(i, 3))});
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

package sn.l2gl.aida.daara.controller;

import sn.l2gl.aida.daara.exception.DaaraException;
import sn.l2gl.aida.daara.model.dao.MaitreDao;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.util.CsvExporter;
import sn.l2gl.aida.daara.view.MaitreView;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controleur des maitres : branche les ecouteurs de la vue, valide les
 * saisies, appelle le DAO, capture les exceptions metier et rafraichit la vue.
 * Aucune requete HQL ici.
 */
public class MaitreController {

    private final MaitreDao dao = new MaitreDao();
    private final MaitreView vue;
    private Maitre enCours; // null = creation, sinon = modification

    public MaitreController(MaitreView vue) {
        this.vue = vue;

        vue.getBoutonChercher().addActionListener(e -> rechercher());
        vue.getBoutonToutAfficher().addActionListener(e -> rafraichir());
        vue.getBoutonNouveau().addActionListener(e -> nouveau());
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        // Selection d'une ligne -> charge la fiche dans le formulaire.
        vue.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chargerSelection();
            }
        });

        rafraichir();
    }

    /** Liste tous les maitres. Public : appele aussi quand la page s'affiche. */
    public void rafraichir() {
        vue.afficher(dao.listerTous());
        nouveau();
    }

    /** Recherche par nom (critere). */
    private void rechercher() {
        String texte = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParNom(texte));
    }

    /** Prepare un formulaire vide pour une creation. */
    private void nouveau() {
        enCours = null;
        vue.reinitialiser();
    }

    /** Charge le maitre selectionne dans la table. */
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

    /** Cree ou met a jour selon l'etat courant. */
    private void enregistrer() {
        String matricule = vue.getChampMatricule().getText().trim();
        String nom = vue.getChampNomComplet().getText().trim();
        String tel = vue.getChampTelephone().getText().trim();

        if (matricule.isEmpty() || nom.isEmpty()) {
            erreur("Le matricule et le nom complet sont obligatoires.");
            return;
        }

        try {
            if (enCours == null) {
                dao.inserer(new Maitre(matricule, nom, tel));
                info("Maitre ajoute avec succes.");
            } else {
                enCours.setNomComplet(nom);
                enCours.setTelephone(tel);
                dao.modifier(enCours);
                info("Maitre modifie avec succes.");
            }
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    /** Supprime apres confirmation. */
    private void supprimer() {
        if (enCours == null) {
            erreur("Selectionnez d'abord un maitre dans la liste.");
            return;
        }
        int choix = JOptionPane.showConfirmDialog(vue,
                "Supprimer le maitre " + enCours.getNomComplet() + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choix != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.supprimer(enCours.getMatricule());
            info("Maitre supprime.");
            rafraichir();
        } catch (DaaraException ex) {
            erreur(ex.getMessage());
        }
    }

    /** Exporte la liste affichee au format CSV. */
    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("maitres.csv"));
        if (chooser.showSaveDialog(vue) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String[] entetes = {"matricule", "nomComplet", "telephone"};
        List<String[]> lignes = new ArrayList<>();
        for (int i = 0; i < vue.getTable().getRowCount(); i++) {
            lignes.add(new String[]{
                    texte(vue.getTable().getValueAt(i, 0)),
                    texte(vue.getTable().getValueAt(i, 1)),
                    texte(vue.getTable().getValueAt(i, 2))});
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

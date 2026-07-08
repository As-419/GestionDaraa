package sn.l2gl.aida.daara.view;

import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Talibe;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;

/**
 * Vue (JPanel) de gestion des talibes.
 * La classe d'affectation est choisie via une liste deroulante.
 */
public class TalibeView extends JPanel {

    private final JTextField champMatricule = Theme.champ();
    private final JTextField champPrenom = Theme.champ();
    private final JTextField champNom = Theme.champ();
    private final JTextField champDateNaissance = Theme.champ(); // format AAAA-MM-JJ
    private final JTextField champNomTuteur = Theme.champ();
    private final JTextField champTelephoneTuteur = Theme.champ();
    private final JComboBox<Classe> champClasse = new JComboBox<>();
    private final JTextField champRecherche = Theme.champ();

    private final JButton boutonChercher = Theme.bouton("Rechercher", Theme.PRIMAIRE);
    private final JButton boutonToutAfficher = Theme.bouton("Tout afficher", Theme.TEXTE_DOUX);
    private final JButton boutonNouveau = Theme.bouton("Nouveau", Theme.ACCENT);
    private final JButton boutonEnregistrer = Theme.bouton("Enregistrer", Theme.PRIMAIRE);
    private final JButton boutonSupprimer = Theme.bouton("Supprimer", Theme.DANGER);
    private final JButton boutonExporter = Theme.bouton("Exporter CSV", Theme.PRIMAIRE_CLAIR);

    private final DefaultTableModel modeleTable;
    private final JTable table;

    public TalibeView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.FOND);
        setBorder(Theme.padding(20, 24));

        champClasse.setFont(Theme.NORMALE);

        add(construireEntete(), BorderLayout.NORTH);

        modeleTable = new DefaultTableModel(
                new Object[]{"Matricule", "Prenom", "Nom", "Naissance", "Tuteur", "Classe"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(modeleTable);

        JPanel centre = new JPanel(new BorderLayout(16, 0));
        centre.setOpaque(false);
        centre.add(construireFormulaire(), BorderLayout.WEST);
        centre.add(Theme.tableStylee(table), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
    }

    private JPanel construireEntete() {
        JPanel entete = new JPanel(new BorderLayout(12, 4));
        entete.setOpaque(false);

        JLabel titre = new JLabel("Gestion des talibes");
        titre.setFont(Theme.TITRE);
        titre.setForeground(Theme.TEXTE);
        JLabel sous = new JLabel("Les eleves de la daara, rattaches a une classe.");
        sous.setFont(Theme.PETITE);
        sous.setForeground(Theme.TEXTE_DOUX);
        JPanel titres = new JPanel();
        titres.setOpaque(false);
        titres.setLayout(new BoxLayout(titres, BoxLayout.Y_AXIS));
        titres.add(titre);
        titres.add(sous);
        entete.add(titres, BorderLayout.WEST);

        JPanel recherche = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        recherche.setOpaque(false);
        champRecherche.setPreferredSize(new Dimension(200, 36));
        recherche.add(Theme.label("Nom :"));
        recherche.add(champRecherche);
        recherche.add(boutonChercher);
        recherche.add(boutonToutAfficher);
        entete.add(recherche, BorderLayout.EAST);
        return entete;
    }

    private JPanel construireFormulaire() {
        JPanel carte = new JPanel(new GridBagLayout());
        carte.setBackground(Theme.CARTE);
        carte.setBorder(Theme.carte());
        carte.setPreferredSize(new Dimension(330, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(5, 0, 1, 0);

        JLabel titreForm = new JLabel("Fiche talibe");
        titreForm.setFont(Theme.SOUS_TITRE);
        titreForm.setForeground(Theme.PRIMAIRE);
        carte.add(titreForm, g);

        g.gridy++;
        carte.add(Theme.label("Matricule (ex : T0001)"), g);
        g.gridy++;
        carte.add(champMatricule, g);
        g.gridy++;
        carte.add(Theme.label("Prenom"), g);
        g.gridy++;
        carte.add(champPrenom, g);
        g.gridy++;
        carte.add(Theme.label("Nom"), g);
        g.gridy++;
        carte.add(champNom, g);
        g.gridy++;
        carte.add(Theme.label("Date de naissance (AAAA-MM-JJ)"), g);
        g.gridy++;
        carte.add(champDateNaissance, g);
        g.gridy++;
        carte.add(Theme.label("Nom du tuteur"), g);
        g.gridy++;
        carte.add(champNomTuteur, g);
        g.gridy++;
        carte.add(Theme.label("Telephone du tuteur"), g);
        g.gridy++;
        carte.add(champTelephoneTuteur, g);
        g.gridy++;
        carte.add(Theme.label("Classe d'affectation"), g);
        g.gridy++;
        carte.add(champClasse, g);

        g.gridy++;
        g.insets = new Insets(14, 0, 0, 0);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(boutonEnregistrer);
        actions.add(boutonNouveau);
        carte.add(actions, g);

        g.gridy++;
        g.insets = new Insets(8, 0, 0, 0);
        JPanel actions2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions2.setOpaque(false);
        actions2.add(boutonSupprimer);
        actions2.add(boutonExporter);
        carte.add(actions2, g);

        g.gridy++;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        carte.add(Box.createGlue(), g);
        return carte;
    }

    // ----- Methodes demandees par l'enonce -----

    public void afficher(List<Talibe> talibes) {
        modeleTable.setRowCount(0);
        for (Talibe t : talibes) {
            modeleTable.addRow(new Object[]{
                    t.getMatricule(),
                    t.getPrenom(),
                    t.getNom(),
                    t.getDateNaissance() == null ? "" : t.getDateNaissance().toString(),
                    t.getNomTuteur(),
                    t.getClasse() == null ? "" : t.getClasse().getLibelle()});
        }
    }

    public void remplir(Talibe t) {
        champMatricule.setText(t.getMatricule());
        champMatricule.setEditable(false);
        champPrenom.setText(t.getPrenom());
        champNom.setText(t.getNom());
        champDateNaissance.setText(t.getDateNaissance() == null ? "" : t.getDateNaissance().toString());
        champNomTuteur.setText(t.getNomTuteur());
        champTelephoneTuteur.setText(t.getTelephoneTuteur());
        selectionnerClasse(t.getClasse());
    }

    public void reinitialiser() {
        champMatricule.setText("");
        champMatricule.setEditable(true);
        champPrenom.setText("");
        champNom.setText("");
        champDateNaissance.setText("");
        champNomTuteur.setText("");
        champTelephoneTuteur.setText("");
        if (champClasse.getItemCount() > 0) {
            champClasse.setSelectedIndex(0);
        }
        table.clearSelection();
    }

    /** Alimente la liste deroulante des classes. */
    public void chargerClasses(List<Classe> classes) {
        champClasse.setModel(new DefaultComboBoxModel<>(classes.toArray(new Classe[0])));
    }

    private void selectionnerClasse(Classe classe) {
        if (classe == null) {
            return;
        }
        for (int i = 0; i < champClasse.getItemCount(); i++) {
            if (champClasse.getItemAt(i).getCode().equals(classe.getCode())) {
                champClasse.setSelectedIndex(i);
                return;
            }
        }
    }

    /** Convertit le texte du champ date en LocalDate (null si vide). */
    public LocalDate lireDateNaissance() {
        String texte = champDateNaissance.getText().trim();
        if (texte.isEmpty()) {
            return null;
        }
        return LocalDate.parse(texte); // peut lever DateTimeParseException
    }

    // ----- Getters -----

    public JTextField getChampMatricule() {
        return champMatricule;
    }

    public JTextField getChampPrenom() {
        return champPrenom;
    }

    public JTextField getChampNom() {
        return champNom;
    }

    public JTextField getChampDateNaissance() {
        return champDateNaissance;
    }

    public JTextField getChampNomTuteur() {
        return champNomTuteur;
    }

    public JTextField getChampTelephoneTuteur() {
        return champTelephoneTuteur;
    }

    public JComboBox<Classe> getChampClasse() {
        return champClasse;
    }

    public JTextField getChampRecherche() {
        return champRecherche;
    }

    public JButton getBoutonChercher() {
        return boutonChercher;
    }

    public JButton getBoutonToutAfficher() {
        return boutonToutAfficher;
    }

    public JButton getBoutonNouveau() {
        return boutonNouveau;
    }

    public JButton getBoutonEnregistrer() {
        return boutonEnregistrer;
    }

    public JButton getBoutonSupprimer() {
        return boutonSupprimer;
    }

    public JButton getBoutonExporter() {
        return boutonExporter;
    }

    public JTable getTable() {
        return table;
    }
}

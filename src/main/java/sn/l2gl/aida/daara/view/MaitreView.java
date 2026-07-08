package sn.l2gl.aida.daara.view;

import sn.l2gl.aida.daara.model.models.Maitre;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Vue (JPanel) de gestion des maitres.
 * Expose ses composants via des getters et fournit afficher / remplir /
 * reinitialiser. AUCUN acces base de donnees, AUCUNE logique metier ici.
 */
public class MaitreView extends JPanel {

    private final JTextField champMatricule = Theme.champ();
    private final JTextField champNomComplet = Theme.champ();
    private final JTextField champTelephone = Theme.champ();
    private final JTextField champRecherche = Theme.champ();

    private final JButton boutonChercher = Theme.bouton("Rechercher", Theme.PRIMAIRE);
    private final JButton boutonToutAfficher = Theme.bouton("Tout afficher", Theme.TEXTE_DOUX);
    private final JButton boutonNouveau = Theme.bouton("Nouveau", Theme.ACCENT);
    private final JButton boutonEnregistrer = Theme.bouton("Enregistrer", Theme.PRIMAIRE);
    private final JButton boutonSupprimer = Theme.bouton("Supprimer", Theme.DANGER);
    private final JButton boutonExporter = Theme.bouton("Exporter CSV", Theme.PRIMAIRE_CLAIR);

    private final DefaultTableModel modeleTable;
    private final JTable table;

    public MaitreView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.FOND);
        setBorder(Theme.padding(20, 24));

        add(construireEntete(), BorderLayout.NORTH);

        // Table en lecture seule
        modeleTable = new DefaultTableModel(
                new Object[]{"Matricule", "Nom complet", "Telephone"}, 0) {
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

        JLabel titre = new JLabel("Gestion des maitres");
        titre.setFont(Theme.TITRE);
        titre.setForeground(Theme.TEXTE);
        JLabel sous = new JLabel("Les serignes qui encadrent les classes (halqas).");
        sous.setFont(Theme.PETITE);
        sous.setForeground(Theme.TEXTE_DOUX);
        JPanel titres = new JPanel();
        titres.setOpaque(false);
        titres.setLayout(new BoxLayout(titres, BoxLayout.Y_AXIS));
        titres.add(titre);
        titres.add(sous);
        entete.add(titres, BorderLayout.WEST);

        // Barre de recherche a droite
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
        carte.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(6, 0, 2, 0);

        JLabel titreForm = new JLabel("Fiche maitre");
        titreForm.setFont(Theme.SOUS_TITRE);
        titreForm.setForeground(Theme.PRIMAIRE);
        carte.add(titreForm, g);

        g.gridy++;
        carte.add(Theme.label("Matricule (ex : M001)"), g);
        g.gridy++;
        carte.add(champMatricule, g);
        g.gridy++;
        carte.add(Theme.label("Nom complet"), g);
        g.gridy++;
        carte.add(champNomComplet, g);
        g.gridy++;
        carte.add(Theme.label("Telephone"), g);
        g.gridy++;
        carte.add(champTelephone, g);

        g.gridy++;
        g.insets = new Insets(16, 0, 0, 0);
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

        // Pousse le contenu vers le haut
        g.gridy++;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        carte.add(Box.createGlue(), g);
        return carte;
    }

    // ----- Methodes demandees par l'enonce -----

    /** Remplit la table avec la liste fournie (lecture seule). */
    public void afficher(List<Maitre> maitres) {
        modeleTable.setRowCount(0);
        for (Maitre m : maitres) {
            modeleTable.addRow(new Object[]{
                    m.getMatricule(), m.getNomComplet(), m.getTelephone()});
        }
    }

    /** Charge un maitre dans le formulaire (le matricule devient non modifiable). */
    public void remplir(Maitre m) {
        champMatricule.setText(m.getMatricule());
        champMatricule.setEditable(false);
        champNomComplet.setText(m.getNomComplet());
        champTelephone.setText(m.getTelephone());
    }

    /** Vide le formulaire et reautorise la saisie du matricule. */
    public void reinitialiser() {
        champMatricule.setText("");
        champMatricule.setEditable(true);
        champNomComplet.setText("");
        champTelephone.setText("");
        table.clearSelection();
    }

    // ----- Getters (la vue expose ses composants au controleur) -----

    public JTextField getChampMatricule() {
        return champMatricule;
    }

    public JTextField getChampNomComplet() {
        return champNomComplet;
    }

    public JTextField getChampTelephone() {
        return champTelephone;
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

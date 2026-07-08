package sn.l2gl.aida.daara.view;

import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.model.models.Niveau;

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
import java.util.List;

/**
 * Vue (JPanel) de gestion des classes (halqas).
 * Le maitre est choisi via une liste deroulante alimentee depuis la base.
 */
public class ClasseView extends JPanel {

    private final JTextField champCode = Theme.champ();
    private final JTextField champLibelle = Theme.champ();
    private final JComboBox<Niveau> champNiveau = new JComboBox<>(Niveau.values());
    private final JComboBox<Maitre> champMaitre = new JComboBox<>();
    private final JTextField champRecherche = Theme.champ();

    private final JButton boutonChercher = Theme.bouton("Rechercher", Theme.PRIMAIRE);
    private final JButton boutonToutAfficher = Theme.bouton("Tout afficher", Theme.TEXTE_DOUX);
    private final JButton boutonNouveau = Theme.bouton("Nouveau", Theme.ACCENT);
    private final JButton boutonEnregistrer = Theme.bouton("Enregistrer", Theme.PRIMAIRE);
    private final JButton boutonSupprimer = Theme.bouton("Supprimer", Theme.DANGER);
    private final JButton boutonExporter = Theme.bouton("Exporter CSV", Theme.PRIMAIRE_CLAIR);

    private final DefaultTableModel modeleTable;
    private final JTable table;

    public ClasseView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.FOND);
        setBorder(Theme.padding(20, 24));

        champNiveau.setFont(Theme.NORMALE);
        champMaitre.setFont(Theme.NORMALE);

        add(construireEntete(), BorderLayout.NORTH);

        modeleTable = new DefaultTableModel(
                new Object[]{"Code", "Libelle", "Niveau", "Maitre"}, 0) {
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

        JLabel titre = new JLabel("Gestion des classes");
        titre.setFont(Theme.TITRE);
        titre.setForeground(Theme.TEXTE);
        JLabel sous = new JLabel("Les halqas, chacune encadree par un maitre.");
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
        recherche.add(Theme.label("Libelle :"));
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

        JLabel titreForm = new JLabel("Fiche classe");
        titreForm.setFont(Theme.SOUS_TITRE);
        titreForm.setForeground(Theme.PRIMAIRE);
        carte.add(titreForm, g);

        g.gridy++;
        carte.add(Theme.label("Code (ex : CL-DEB)"), g);
        g.gridy++;
        carte.add(champCode, g);
        g.gridy++;
        carte.add(Theme.label("Libelle"), g);
        g.gridy++;
        carte.add(champLibelle, g);
        g.gridy++;
        carte.add(Theme.label("Niveau"), g);
        g.gridy++;
        carte.add(champNiveau, g);
        g.gridy++;
        carte.add(Theme.label("Maitre encadrant"), g);
        g.gridy++;
        carte.add(champMaitre, g);

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

        g.gridy++;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        carte.add(Box.createGlue(), g);
        return carte;
    }

    // ----- Methodes demandees par l'enonce -----

    public void afficher(List<Classe> classes) {
        modeleTable.setRowCount(0);
        for (Classe c : classes) {
            modeleTable.addRow(new Object[]{
                    c.getCode(),
                    c.getLibelle(),
                    c.getNiveau() == null ? "" : c.getNiveau().getLibelle(),
                    c.getMaitre() == null ? "" : c.getMaitre().getNomComplet()});
        }
    }

    public void remplir(Classe c) {
        champCode.setText(c.getCode());
        champCode.setEditable(false);
        champLibelle.setText(c.getLibelle());
        champNiveau.setSelectedItem(c.getNiveau());
        selectionnerMaitre(c.getMaitre());
    }

    public void reinitialiser() {
        champCode.setText("");
        champCode.setEditable(true);
        champLibelle.setText("");
        champNiveau.setSelectedIndex(0);
        if (champMaitre.getItemCount() > 0) {
            champMaitre.setSelectedIndex(0);
        }
        table.clearSelection();
    }

    /** Alimente la liste deroulante des maitres (appelee par le controleur). */
    public void chargerMaitres(List<Maitre> maitres) {
        champMaitre.setModel(new DefaultComboBoxModel<>(maitres.toArray(new Maitre[0])));
    }

    private void selectionnerMaitre(Maitre maitre) {
        if (maitre == null) {
            return;
        }
        for (int i = 0; i < champMaitre.getItemCount(); i++) {
            if (champMaitre.getItemAt(i).getMatricule().equals(maitre.getMatricule())) {
                champMaitre.setSelectedIndex(i);
                return;
            }
        }
    }

    // ----- Getters -----

    public JTextField getChampCode() {
        return champCode;
    }

    public JTextField getChampLibelle() {
        return champLibelle;
    }

    public JComboBox<Niveau> getChampNiveau() {
        return champNiveau;
    }

    public JComboBox<Maitre> getChampMaitre() {
        return champMaitre;
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

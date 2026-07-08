package sn.l2gl.aida.daara.view;

import sn.l2gl.aida.daara.model.models.Progression;
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
 * Vue (JPanel) de gestion des progressions.
 * Le talibe est choisi via une liste deroulante ; un second selecteur permet
 * de filtrer la table par talibe.
 */
public class ProgressionView extends JPanel {

    private final JComboBox<Talibe> champTalibe = new JComboBox<>();
    private final JTextField champSourate = Theme.champ();
    private final JTextField champNombreVersets = Theme.champ();
    private final JTextField champDateEvaluation = Theme.champ(); // AAAA-MM-JJ
    private final JTextField champAppreciation = Theme.champ();

    private final JComboBox<Talibe> champFiltreTalibe = new JComboBox<>();

    private final JButton boutonFiltrer = Theme.bouton("Filtrer", Theme.PRIMAIRE);
    private final JButton boutonToutAfficher = Theme.bouton("Tout afficher", Theme.TEXTE_DOUX);
    private final JButton boutonNouveau = Theme.bouton("Nouveau", Theme.ACCENT);
    private final JButton boutonEnregistrer = Theme.bouton("Enregistrer", Theme.PRIMAIRE);
    private final JButton boutonSupprimer = Theme.bouton("Supprimer", Theme.DANGER);
    private final JButton boutonExporter = Theme.bouton("Exporter CSV", Theme.PRIMAIRE_CLAIR);

    private final DefaultTableModel modeleTable;
    private final JTable table;

    public ProgressionView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.FOND);
        setBorder(Theme.padding(20, 24));

        champTalibe.setFont(Theme.NORMALE);
        champFiltreTalibe.setFont(Theme.NORMALE);

        add(construireEntete(), BorderLayout.NORTH);

        modeleTable = new DefaultTableModel(
                new Object[]{"Id", "Talibe", "Sourate", "Versets", "Date", "Appreciation"}, 0) {
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

        JLabel titre = new JLabel("Suivi des progressions");
        titre.setFont(Theme.TITRE);
        titre.setForeground(Theme.TEXTE);
        JLabel sous = new JLabel("La memorisation du Coran, evaluation par evaluation.");
        sous.setFont(Theme.PETITE);
        sous.setForeground(Theme.TEXTE_DOUX);
        JPanel titres = new JPanel();
        titres.setOpaque(false);
        titres.setLayout(new BoxLayout(titres, BoxLayout.Y_AXIS));
        titres.add(titre);
        titres.add(sous);
        entete.add(titres, BorderLayout.WEST);

        JPanel filtre = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtre.setOpaque(false);
        champFiltreTalibe.setPreferredSize(new Dimension(220, 36));
        filtre.add(Theme.label("Talibe :"));
        filtre.add(champFiltreTalibe);
        filtre.add(boutonFiltrer);
        filtre.add(boutonToutAfficher);
        entete.add(filtre, BorderLayout.EAST);
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

        JLabel titreForm = new JLabel("Fiche progression");
        titreForm.setFont(Theme.SOUS_TITRE);
        titreForm.setForeground(Theme.PRIMAIRE);
        carte.add(titreForm, g);

        g.gridy++;
        carte.add(Theme.label("Talibe evalue"), g);
        g.gridy++;
        carte.add(champTalibe, g);
        g.gridy++;
        carte.add(Theme.label("Sourate (ex : Al-Baqara)"), g);
        g.gridy++;
        carte.add(champSourate, g);
        g.gridy++;
        carte.add(Theme.label("Nombre de versets memorises"), g);
        g.gridy++;
        carte.add(champNombreVersets, g);
        g.gridy++;
        carte.add(Theme.label("Date d'evaluation (AAAA-MM-JJ)"), g);
        g.gridy++;
        carte.add(champDateEvaluation, g);
        g.gridy++;
        carte.add(Theme.label("Appreciation"), g);
        g.gridy++;
        carte.add(champAppreciation, g);

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

    public void afficher(List<Progression> progressions) {
        modeleTable.setRowCount(0);
        for (Progression p : progressions) {
            modeleTable.addRow(new Object[]{
                    p.getId(),
                    p.getTalibe() == null ? "" : p.getTalibe().getPrenom() + " " + p.getTalibe().getNom(),
                    p.getSourate(),
                    p.getNombreVersets(),
                    p.getDateEvaluation() == null ? "" : p.getDateEvaluation().toString(),
                    p.getAppreciation()});
        }
    }

    public void remplir(Progression p) {
        selectionnerTalibe(champTalibe, p.getTalibe());
        champSourate.setText(p.getSourate());
        champNombreVersets.setText(String.valueOf(p.getNombreVersets()));
        champDateEvaluation.setText(p.getDateEvaluation() == null ? "" : p.getDateEvaluation().toString());
        champAppreciation.setText(p.getAppreciation());
    }

    public void reinitialiser() {
        if (champTalibe.getItemCount() > 0) {
            champTalibe.setSelectedIndex(0);
        }
        champSourate.setText("");
        champNombreVersets.setText("");
        champDateEvaluation.setText("");
        champAppreciation.setText("");
        table.clearSelection();
    }

    /** Alimente les deux listes deroulantes de talibes (saisie + filtre). */
    public void chargerTalibes(List<Talibe> talibes) {
        champTalibe.setModel(new DefaultComboBoxModel<>(talibes.toArray(new Talibe[0])));
        champFiltreTalibe.setModel(new DefaultComboBoxModel<>(talibes.toArray(new Talibe[0])));
    }

    private void selectionnerTalibe(JComboBox<Talibe> combo, Talibe talibe) {
        if (talibe == null) {
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getMatricule().equals(talibe.getMatricule())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    /** Lit le nombre de versets (peut lever NumberFormatException). */
    public int lireNombreVersets() {
        return Integer.parseInt(champNombreVersets.getText().trim());
    }

    /** Lit la date d'evaluation (null si vide ; peut lever DateTimeParseException). */
    public LocalDate lireDateEvaluation() {
        String texte = champDateEvaluation.getText().trim();
        if (texte.isEmpty()) {
            return null;
        }
        return LocalDate.parse(texte);
    }

    // ----- Getters -----

    public JComboBox<Talibe> getChampTalibe() {
        return champTalibe;
    }

    public JComboBox<Talibe> getChampFiltreTalibe() {
        return champFiltreTalibe;
    }

    public JTextField getChampSourate() {
        return champSourate;
    }

    public JTextField getChampNombreVersets() {
        return champNombreVersets;
    }

    public JTextField getChampDateEvaluation() {
        return champDateEvaluation;
    }

    public JTextField getChampAppreciation() {
        return champAppreciation;
    }

    public JButton getBoutonFiltrer() {
        return boutonFiltrer;
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

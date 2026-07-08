package sn.l2gl.aida.daara.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Charte graphique de l'application : couleurs, polices et fabriques de
 * composants Swing stylises. Centralise le design pour une UI homogene
 * et moderne (la vue ne contient ainsi aucune valeur de couleur en dur).
 */
public final class Theme {

    private Theme() {
    }

    // ----- Palette : DESIGN TOKENS PARTAGES avec la version web (style.css) -----
    // Les memes valeurs hexadecimales sont utilisees dans app/static/css/style.css
    // pour que les deux plateformes aient une identite visuelle identique.
    public static final Color FOND = new Color(0xF4, 0xF7, 0xF5);        // --bg
    public static final Color CARTE = Color.WHITE;                       // --surface
    public static final Color PRIMAIRE = new Color(0x0F, 0x76, 0x6E);    // --accent (vert emeraude)
    public static final Color PRIMAIRE_CLAIR = new Color(0x14, 0x95, 0x8A); // --accent-2
    public static final Color ACCENT = new Color(0xC6, 0x8A, 0x1E);      // --gold (or/sable)
    public static final Color DANGER = new Color(0xC7, 0x40, 0x2F);      // --error
    public static final Color TEXTE = new Color(0x14, 0x20, 0x1C);       // --text
    public static final Color TEXTE_DOUX = new Color(0x5B, 0x6B, 0x65);  // --muted
    public static final Color BORDURE = new Color(0xE3, 0xE9, 0xE6);     // --border
    public static final Color SIDEBAR = new Color(0x0B, 0x3D, 0x33);     // --color-sidebar
    public static final Color LIGNE_PAIRE = new Color(0xF7, 0xFA, 0xF8); // --surface-2

    // ----- Polices -----
    public static final Font TITRE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SOUS_TITRE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font NORMALE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font PETITE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOUTON = new Font("Segoe UI", Font.BOLD, 13);

    /** Marge interne homogene. */
    public static Border padding(int v, int h) {
        return BorderFactory.createEmptyBorder(v, h, v, h);
    }

    /** Bordure de carte : trait fin + marge interne genereuse. */
    public static Border carte() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDURE, 1, true),
                padding(22, 22));
    }

    /** Champ de saisie stylise (coins arrondis simules par la bordure). */
    public static JTextField champ() {
        JTextField t = new JTextField();
        t.setFont(NORMALE);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDURE, 1, true),
                padding(10, 12)));
        return t;
    }

    /** Etiquette de formulaire. */
    public static JLabel label(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(PETITE);
        l.setForeground(TEXTE_DOUX);
        return l;
    }

    /** Bouton plein, colore, avec coins arrondis et effet survol. */
    public static JButton bouton(String texte, Color couleur) {
        JButton b = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = couleur;
                if (getModel().isPressed()) {
                    c = couleur.darker();
                } else if (getModel().isRollover()) {
                    c = couleur.brighter();
                }
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(BOUTON);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(padding(11, 18));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Applique un style "tableau moderne" : lignes alternees, en-tete colore. */
    public static JScrollPane tableStylee(JTable table) {
        table.setFont(NORMALE);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDURE);
        table.setSelectionBackground(PRIMAIRE_CLAIR);
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader entete = table.getTableHeader();
        entete.setFont(SOUS_TITRE);
        entete.setBackground(PRIMAIRE);
        entete.setForeground(Color.WHITE);
        entete.setPreferredSize(new java.awt.Dimension(0, 34));
        entete.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDURE, 1, true));
        scroll.getViewport().setBackground(CARTE);
        return scroll;
    }

    /** Aligne verticalement un composant (utilitaire de mise en page). */
    public static <T extends JComponent> T alignerGauche(T composant) {
        composant.setAlignmentX(Component.LEFT_ALIGNMENT);
        return composant;
    }
}

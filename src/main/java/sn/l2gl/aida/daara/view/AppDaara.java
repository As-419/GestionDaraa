package sn.l2gl.aida.daara.view;

import sn.l2gl.aida.daara.controller.ClasseController;
import sn.l2gl.aida.daara.controller.MaitreController;
import sn.l2gl.aida.daara.controller.ProgressionController;
import sn.l2gl.aida.daara.controller.TalibeController;
import sn.l2gl.aida.daara.util.HibernateUtil;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenetre principale de l'application (JFrame).
 * Contient une barre laterale de navigation et un menu "Affichage" pour
 * basculer entre les quatre pages (CardLayout), comme AppHibernate dans
 * la reference.
 */
public class AppDaara extends JFrame {

    private final CardLayout cartes = new CardLayout();
    private final JPanel conteneur = new JPanel(cartes);

    // Pages (vues)
    private final MaitreView maitreView = new MaitreView();
    private final ClasseView classeView = new ClasseView();
    private final TalibeView talibeView = new TalibeView();
    private final ProgressionView progressionView = new ProgressionView();

    // Controleurs (references gardees pour rafraichir la page a l'affichage)
    private MaitreController maitreController;
    private ClasseController classeController;
    private TalibeController talibeController;
    private ProgressionController progressionController;

    public AppDaara() {
        setTitle("Gestion de la Daara - Ecole coranique");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(null);

        // Branche chaque vue a son controleur (le controleur cable les ecouteurs).
        maitreController = new MaitreController(maitreView);
        classeController = new ClasseController(classeView);
        talibeController = new TalibeController(talibeView);
        progressionController = new ProgressionController(progressionView);

        conteneur.add(maitreView, "maitres");
        conteneur.add(classeView, "classes");
        conteneur.add(talibeView, "talibes");
        conteneur.add(progressionView, "progressions");

        setLayout(new BorderLayout());
        add(construireSidebar(), BorderLayout.WEST);
        add(conteneur, BorderLayout.CENTER);
        setJMenuBar(construireMenu());

        // Ferme proprement la SessionFactory en quittant.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HibernateUtil.fermer();
                dispose();
                System.exit(0);
            }
        });

        afficher("maitres");
    }

    private JPanel construireSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 18, 24, 18));
        sidebar.setPreferredSize(new Dimension(230, 0));

        JLabel logo = new JLabel("★ Daara");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel slogan = new JLabel("Gestion de l'ecole coranique");
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        slogan.setForeground(new Color(0xA7, 0xC4, 0xBC));
        slogan.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(slogan);
        sidebar.add(Box.createVerticalStrut(28));

        sidebar.add(lienNav("Maitres", "maitres"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(lienNav("Classes", "classes"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(lienNav("Talibes", "talibes"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(lienNav("Progressions", "progressions"));

        sidebar.add(Box.createVerticalGlue());
        JLabel pied = new JLabel("L2 Genie Logiciel");
        pied.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        pied.setForeground(new Color(0x6E, 0x90, 0x88));
        pied.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(pied);
        return sidebar;
    }

    /** Bouton de navigation lateral (style plat, plein largeur). */
    private JButton lienNav(String texte, String cle) {
        JButton b = new JButton(texte);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(0x12, 0x52, 0x46));
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setHorizontalAlignment(JButton.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e -> afficher(cle));
        return b;
    }

    private JMenuBar construireMenu() {
        JMenuBar barre = new JMenuBar();
        JMenu affichage = new JMenu("Affichage");

        JMenuItem miMaitres = new JMenuItem("Maitres");
        miMaitres.addActionListener(e -> afficher("maitres"));
        JMenuItem miClasses = new JMenuItem("Classes");
        miClasses.addActionListener(e -> afficher("classes"));
        JMenuItem miTalibes = new JMenuItem("Talibes");
        miTalibes.addActionListener(e -> afficher("talibes"));
        JMenuItem miProgressions = new JMenuItem("Progressions");
        miProgressions.addActionListener(e -> afficher("progressions"));

        affichage.add(miMaitres);
        affichage.add(miClasses);
        affichage.add(miTalibes);
        affichage.add(miProgressions);

        JMenu fichier = new JMenu("Fichier");
        JMenuItem miQuitter = new JMenuItem("Quitter");
        miQuitter.addActionListener(e -> {
            HibernateUtil.fermer();
            dispose();
            System.exit(0);
        });
        fichier.add(miQuitter);

        barre.add(fichier);
        barre.add(affichage);
        return barre;
    }

    /** Affiche la page demandee et rafraichit ses donnees (listes deroulantes a jour). */
    private void afficher(String cle) {
        cartes.show(conteneur, cle);
        switch (cle) {
            case "maitres" -> maitreController.rafraichir();
            case "classes" -> classeController.rafraichir();
            case "talibes" -> talibeController.rafraichir();
            case "progressions" -> progressionController.rafraichir();
            default -> { /* rien */ }
        }
    }

    /** Point d'entree de l'application. */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
            // En cas d'echec on garde le look & feel par defaut.
        }
        SwingUtilities.invokeLater(() -> {
            AppDaara app = new AppDaara();
            app.setExtendedState(JFrame.MAXIMIZED_BOTH);
            app.setVisible(true);
            app.toFront();
            app.requestFocus();
        });
    }
}

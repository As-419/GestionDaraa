package sn.l2gl.aida.daara.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Ecriture de fichiers CSV (encodage UTF-8). Appele par les controleurs,
 * jamais par les vues. Chaque valeur est echappee selon la norme CSV
 * (RFC 4180) pour gerer les virgules, guillemets et retours a la ligne.
 */
public final class CsvExporter {

    private CsvExporter() {
        // classe utilitaire : pas d'instanciation
    }

    /**
     * Ecrit l'en-tete puis une ligne par enregistrement.
     *
     * @param fichier fichier de destination
     * @param entetes noms des colonnes
     * @param lignes  valeurs (une String[] par ligne, meme longueur que entetes)
     */
    public static void exporter(File fichier, String[] entetes, List<String[]> lignes)
            throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(fichier.toPath(), StandardCharsets.UTF_8)) {
            w.write(joindre(entetes));
            w.newLine();
            for (String[] ligne : lignes) {
                w.write(joindre(ligne));
                w.newLine();
            }
        }
    }

    /** Joint les champs d'une ligne par des virgules, en les echappant. */
    private static String joindre(String[] champs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < champs.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(echapper(champs[i]));
        }
        return sb.toString();
    }

    /** Echappe un champ CSV : guillemets doubles si virgule, guillemet ou saut de ligne. */
    private static String echapper(String champ) {
        String valeur = (champ == null) ? "" : champ;
        boolean doitEtreEntoure = valeur.contains(",")
                || valeur.contains("\"")
                || valeur.contains("\n")
                || valeur.contains("\r");
        if (doitEtreEntoure) {
            return '"' + valeur.replace("\"", "\"\"") + '"';
        }
        return valeur;
    }
}

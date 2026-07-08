package sn.l2gl.aida.daara;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import sn.l2gl.aida.daara.util.CsvExporter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvExporterTest {

    @Test
    @DisplayName("ecrit l'en-tete puis une ligne par enregistrement, en UTF-8")
    void exporter_ecritEnteteEtLignes(@TempDir Path dossier) throws IOException {
        Path fichier = dossier.resolve("talibes.csv");
        String[] entetes = {"matricule", "prenom", "nom"};
        List<String[]> lignes = List.of(
                new String[]{"T0001", "Modou", "Fall"},
                new String[]{"T0002", "Awa", "Sow"});

        CsvExporter.exporter(fichier.toFile(), entetes, lignes);

        List<String> contenu = Files.readAllLines(fichier, StandardCharsets.UTF_8);
        assertEquals("matricule,prenom,nom", contenu.get(0));
        assertEquals("T0001,Modou,Fall", contenu.get(1));
        assertEquals("T0002,Awa,Sow", contenu.get(2));
    }

    @Test
    @DisplayName("echappe les champs contenant une virgule ou un guillemet")
    void exporter_echappeChampsSpeciaux(@TempDir Path dossier) throws IOException {
        Path fichier = dossier.resolve("test.csv");

        CsvExporter.exporter(fichier.toFile(),
                new String[]{"a", "b"},
                List.<String[]>of(new String[]{"Fall, Modou", "dit \"le sage\""}));

        String ligne = Files.readAllLines(fichier, StandardCharsets.UTF_8).get(1);
        assertTrue(ligne.contains("\"Fall, Modou\""));
        assertTrue(ligne.contains("\"dit \"\"le sage\"\"\""));
    }
}

package sn.l2gl.aida.daara;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sn.l2gl.aida.daara.exception.MaitreDejaExistantException;
import sn.l2gl.aida.daara.exception.MaitreIntrouvableException;
import sn.l2gl.aida.daara.exception.SuppressionImpossibleException;
import sn.l2gl.aida.daara.model.dao.ClasseDao;
import sn.l2gl.aida.daara.model.dao.MaitreDao;
import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.model.models.Niveau;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaitreDaoTest extends DaaraTestBase {

    private final MaitreDao dao = new MaitreDao();
    private final ClasseDao classeDao = new ClasseDao();

    @Test
    @DisplayName("inserer puis trouver renvoie le maitre")
    void inserer_puisTrouver() {
        dao.inserer(new Maitre("M001", "Serigne Fallou", "770000000"));

        Optional<Maitre> trouve = dao.trouver("M001");

        assertTrue(trouve.isPresent());
        assertEquals("Serigne Fallou", trouve.get().getNomComplet());
    }

    @Test
    @DisplayName("inserer un matricule existant leve MaitreDejaExistantException")
    void inserer_doublon_leveException() {
        dao.inserer(new Maitre("M001", "A", "1"));

        assertThrows(MaitreDejaExistantException.class,
                () -> dao.inserer(new Maitre("M001", "B", "2")));
    }

    @Test
    @DisplayName("trouverObligatoire leve MaitreIntrouvableException si absent")
    void trouverObligatoire_absent_leveException() {
        assertThrows(MaitreIntrouvableException.class,
                () -> dao.trouverObligatoire("INCONNU"));
    }

    @Test
    @DisplayName("rechercherParNom filtre par fragment insensible a la casse")
    void rechercherParNom_filtre() {
        dao.inserer(new Maitre("M001", "Serigne Modou", "1"));
        dao.inserer(new Maitre("M002", "Serigne Fallou", "2"));
        dao.inserer(new Maitre("M003", "Cheikh Bamba", "3"));

        assertEquals(2, dao.rechercherParNom("serigne").size());
        assertEquals(1, dao.rechercherParNom("bamba").size());
    }

    @Test
    @DisplayName("modifier persiste les changements")
    void modifier_persisteChangements() {
        dao.inserer(new Maitre("M001", "Ancien Nom", "111"));

        Maitre m = dao.trouverObligatoire("M001");
        m.setNomComplet("Nouveau Nom");
        m.setTelephone("999");
        dao.modifier(m);

        Maitre relu = dao.trouverObligatoire("M001");
        assertEquals("Nouveau Nom", relu.getNomComplet());
        assertEquals("999", relu.getTelephone());
    }

    @Test
    @DisplayName("supprimer un maitre sans classe renvoie true")
    void supprimer_sansClasse_ok() {
        dao.inserer(new Maitre("M001", "A", "1"));

        assertTrue(dao.supprimer("M001"));
        assertFalse(dao.trouver("M001").isPresent());
    }

    @Test
    @DisplayName("supprimer un maitre qui encadre une classe leve SuppressionImpossibleException")
    void supprimer_avecClasse_leveException() {
        Maitre m = dao.inserer(new Maitre("M001", "A", "1"));
        classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m));

        assertThrows(SuppressionImpossibleException.class, () -> dao.supprimer("M001"));
    }
}

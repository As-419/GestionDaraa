package sn.l2gl.aida.daara;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sn.l2gl.aida.daara.exception.ProgressionInvalideException;
import sn.l2gl.aida.daara.model.dao.ClasseDao;
import sn.l2gl.aida.daara.model.dao.MaitreDao;
import sn.l2gl.aida.daara.model.dao.ProgressionDao;
import sn.l2gl.aida.daara.model.dao.TalibeDao;
import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.model.models.Niveau;
import sn.l2gl.aida.daara.model.models.Progression;
import sn.l2gl.aida.daara.model.models.Talibe;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressionDaoTest extends DaaraTestBase {

    private final MaitreDao maitreDao = new MaitreDao();
    private final ClasseDao classeDao = new ClasseDao();
    private final TalibeDao talibeDao = new TalibeDao();
    private final ProgressionDao dao = new ProgressionDao();

    private Talibe unTalibe(String matricule) {
        Maitre m = maitreDao.trouver("M001")
                .orElseGet(() -> maitreDao.inserer(new Maitre("M001", "Serigne", "1")));
        Classe c = classeDao.trouver("CL-DEB")
                .orElseGet(() -> classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m)));
        return talibeDao.inserer(new Talibe(matricule, "Modou", "Fall",
                LocalDate.of(2012, 5, 10), "Fall", "1", c));
    }

    @Test
    @DisplayName("inserer une progression valide genere un id auto")
    void inserer_valide_genereId() {
        Talibe t = unTalibe("T0001");

        Progression p = dao.inserer(new Progression(t, "Al-Baqara", 30,
                LocalDate.of(2024, 1, 10), "Bien"));

        assertTrue(p.getId() != null && p.getId() > 0);
    }

    @Test
    @DisplayName("nombreVersets negatif leve ProgressionInvalideException")
    void inserer_versetsNegatif_leveException() {
        Talibe t = unTalibe("T0001");

        assertThrows(ProgressionInvalideException.class,
                () -> dao.inserer(new Progression(t, "Al-Baqara", -1, null, "")));
    }

    @Test
    @DisplayName("sourate vide leve ProgressionInvalideException")
    void inserer_sourateVide_leveException() {
        Talibe t = unTalibe("T0001");

        assertThrows(ProgressionInvalideException.class,
                () -> dao.inserer(new Progression(t, "   ", 5, null, "")));
    }

    @Test
    @DisplayName("listerParTalibe ne renvoie que les progressions du talibe")
    void listerParTalibe_filtre() {
        Talibe t1 = unTalibe("T0001");
        Talibe t2 = unTalibe("T0002");
        dao.inserer(new Progression(t1, "Al-Fatiha", 7, null, ""));
        dao.inserer(new Progression(t1, "Al-Baqara", 286, null, ""));
        dao.inserer(new Progression(t2, "An-Nas", 6, null, ""));

        assertEquals(2, dao.listerParTalibe("T0001").size());
        assertEquals(1, dao.listerParTalibe("T0002").size());
    }

    @Test
    @DisplayName("supprimer un talibe supprime aussi ses progressions (cascade)")
    void supprimerTalibe_cascadeProgressions() {
        Talibe t = unTalibe("T0001");
        dao.inserer(new Progression(t, "Al-Fatiha", 7, null, ""));
        dao.inserer(new Progression(t, "Al-Baqara", 286, null, ""));

        talibeDao.supprimer("T0001");

        assertEquals(0, dao.listerParTalibe("T0001").size());
    }
}

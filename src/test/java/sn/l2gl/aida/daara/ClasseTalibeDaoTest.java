package sn.l2gl.aida.daara;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sn.l2gl.aida.daara.exception.ClasseDejaExistanteException;
import sn.l2gl.aida.daara.exception.SuppressionImpossibleException;
import sn.l2gl.aida.daara.exception.TalibeDejaExistantException;
import sn.l2gl.aida.daara.model.dao.ClasseDao;
import sn.l2gl.aida.daara.model.dao.MaitreDao;
import sn.l2gl.aida.daara.model.dao.TalibeDao;
import sn.l2gl.aida.daara.model.models.Classe;
import sn.l2gl.aida.daara.model.models.Maitre;
import sn.l2gl.aida.daara.model.models.Niveau;
import sn.l2gl.aida.daara.model.models.Talibe;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasseTalibeDaoTest extends DaaraTestBase {

    private final MaitreDao maitreDao = new MaitreDao();
    private final ClasseDao classeDao = new ClasseDao();
    private final TalibeDao talibeDao = new TalibeDao();

    private Maitre unMaitre() {
        return maitreDao.inserer(new Maitre("M001", "Serigne Test", "1"));
    }

    @Test
    @DisplayName("inserer une classe avec maitre puis la retrouver")
    void classe_inserer_trouver() {
        Classe c = classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, unMaitre()));

        assertTrue(classeDao.trouver("CL-DEB").isPresent());
        assertEquals("Serigne Test", c.getMaitre().getNomComplet());
    }

    @Test
    @DisplayName("inserer un code de classe existant leve ClasseDejaExistanteException")
    void classe_doublon_leveException() {
        Maitre m = unMaitre();
        classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m));

        assertThrows(ClasseDejaExistanteException.class,
                () -> classeDao.inserer(new Classe("CL-DEB", "Autre", Niveau.AVANCE, m)));
    }

    @Test
    @DisplayName("rechercherParLibelle filtre les classes")
    void classe_rechercherParLibelle() {
        Maitre m = unMaitre();
        classeDao.inserer(new Classe("CL-DEB", "Halqa des debutants", Niveau.DEBUTANT, m));
        classeDao.inserer(new Classe("CL-AV", "Halqa avancee", Niveau.AVANCE, m));

        assertEquals(1, classeDao.rechercherParLibelle("debutant").size());
        assertEquals(2, classeDao.rechercherParLibelle("halqa").size());
    }

    @Test
    @DisplayName("supprimer une classe contenant un talibe leve SuppressionImpossibleException")
    void classe_avecTalibe_suppressionImpossible() {
        Maitre m = unMaitre();
        Classe c = classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m));
        talibeDao.inserer(new Talibe("T0001", "Modou", "Fall",
                LocalDate.of(2012, 5, 10), "Fall", "1", c));

        assertThrows(SuppressionImpossibleException.class, () -> classeDao.supprimer("CL-DEB"));
    }

    @Test
    @DisplayName("inserer un matricule de talibe existant leve TalibeDejaExistantException")
    void talibe_doublon_leveException() {
        Maitre m = unMaitre();
        Classe c = classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m));
        talibeDao.inserer(new Talibe("T0001", "Modou", "Fall",
                LocalDate.of(2012, 5, 10), "Fall", "1", c));

        assertThrows(TalibeDejaExistantException.class,
                () -> talibeDao.inserer(new Talibe("T0001", "Awa", "Sow",
                        LocalDate.of(2011, 9, 2), "Sow", "2", c)));
    }

    @Test
    @DisplayName("listerParClasse renvoie les talibes d'une classe donnee")
    void talibe_listerParClasse() {
        Maitre m = unMaitre();
        Classe deb = classeDao.inserer(new Classe("CL-DEB", "Debutants", Niveau.DEBUTANT, m));
        Classe av = classeDao.inserer(new Classe("CL-AV", "Avances", Niveau.AVANCE, m));
        talibeDao.inserer(new Talibe("T0001", "Modou", "Fall", null, "Fall", "1", deb));
        talibeDao.inserer(new Talibe("T0002", "Awa", "Sow", null, "Sow", "2", deb));
        talibeDao.inserer(new Talibe("T0003", "Ndeye", "Ba", null, "Ba", "3", av));

        assertEquals(2, talibeDao.listerParClasse("CL-DEB").size());
        assertEquals(1, talibeDao.listerParClasse("CL-AV").size());
    }
}

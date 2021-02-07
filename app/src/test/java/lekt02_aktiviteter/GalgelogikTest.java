package lekt02_aktiviteter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Eksempel på en JUnit-test, som kører på din PC, hvilket er langt hurtigere end
 * at installere og køre den på en Android-enhed
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GalgelogikTest {

  static {
    // Fix for https://stackoverflow.com/questions/60472729/robolectric-test-that-uses-okhttp-for-real-http-requests-throws-java-lang-nullpo/60472730#60472730
    System.setProperty("javax.net.ssl.trustStoreType", "JKS");
  }

  @Test
  public void afprøvMedSkovsnegl() {
    Galgelogik spil = new Galgelogik();
    spil.muligeOrd.clear();
    spil.muligeOrd.add("skovsnegl");
    spil.startNytSpil();

    spil.gætBogstav("e");
    spil.gætBogstav("s");
    assertEquals(0, spil.getAntalForkerteBogstaver());
    assertEquals("s***s*e**", spil.getSynligtOrd());

    spil.gætBogstav("q");
    assertEquals(1, spil.getAntalForkerteBogstaver());

    assertFalse(spil.erSpilletSlut());
    assertFalse(spil.erSpilletTabt());
  }

  @Test
  public void prøvHentOrdFraDr() throws Exception {
    Galgelogik spil = new Galgelogik();
    spil.hentOrdFraDr();
    assertTrue("Mere end 100 ord fra DR", spil.muligeOrd.size()>100);
  }
}
package lekt03_net;

import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import lekt02_aktiviteter.Galgelogik;

public class BenytNetOgAsyncTask extends AppCompatActivity implements OnClickListener {
  Button knap1, knap2, knap3, knap4, knap5;
  TextView textView;

  public static String hentUrl(String url) throws IOException {
    System.out.println("Henter "+url);
    BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    StringBuilder sb = new StringBuilder();
    String linje = br.readLine();
    while (linje != null) {
      sb.append(linje + "\n");
      linje = br.readLine();
      System.out.println(linje);
    }
    br.close();
    return sb.toString();
  }

  private static String findTitler(String rssdata) {
    String titler = "";
    while (true) {
      int tit1 = rssdata.indexOf("<title>") + 7;
      int tit2 = rssdata.indexOf("</title>");
      if (tit2 == -1) break; // hop ud hvis der ikke er flere titler
      if (titler.length() > 400) break; // .. eller hvis vi har nok
      String titel = rssdata.substring(tit1, tit2);
      System.out.println(titel);
      titler = titler + titel + "\n";
      rssdata = rssdata.substring(tit2 + 8); // Søg videre i teksten efter næste titel
    }
    return titler;
  }

  // Til afprøvning af logikken i standard Java (meget hurtigere)
  public static void main(String[] args) throws IOException {
    String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
    String titler = findTitler(rssdata);
    System.out.println("Fandt titler: "+titler);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

    TableLayout tl = new TableLayout(this);

    knap1 = new Button(this);
    knap1.setText("Hent i forgrunden");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Hent i forgrunden\nTjek for blokering af hovedtråd omgået\n(rigtig dårlig idé)");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Tryk her og se hvad der sker når hovedtråden blokeres og man får ANR (Application Not Responding)");
    tl.addView(knap3);

    knap4 = new Button(this);
    knap4.setText("Hent i baggrunden med AsyncTask");
    tl.addView(knap4);

    knap5 = new Button(this);
    knap5.setText("Vis cachet kopi mens der hentes i baggrunden");
    tl.addView(knap5);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);

    textView = new TextView(this);
    textView.setText("\nDemo af forskellige praksisser til netværkskommunikation");
    tl.addView(textView);

    setContentView(tl);

  }

  public void onClick(View hvadBlevDerKlikketPå) {
    setProgressBarIndeterminateVisibility(true);
    try {
      textView.setText( "Du trykkede på " + hvadBlevDerKlikketPå);
      //textView.setText( "Du trykkede på " + ((Button) hvadBlevDerKlikketPå).getText());

      if (hvadBlevDerKlikketPå == knap1) {

        // Nedenstående crasher da netværkskommunikation ikke må ske på hovedtråden
        String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
        String titler = findTitler(rssdata);
        textView.setText(titler);
        setProgressBarIndeterminateVisibility(false);

      } else if (hvadBlevDerKlikketPå == knap2) {
        // Nedenstående tillader netværkskommunikation på hovedtråden
        // fint til lige at prøve noget, men dårlig idé i længden og IKKE TILLADT i en aflevering
        StrictMode.ThreadPolicy normalThreadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build()); // FRARÅDES !!!!

        String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
        String titler = findTitler(rssdata);
        textView.setText(titler);
        setProgressBarIndeterminateVisibility(false);

        Galgelogik gl = new Galgelogik(); // lidt mere netværk, for et syns skyld
        gl.hentOrdFraDr();

        StrictMode.setThreadPolicy(normalThreadPolicy);

      } else if (hvadBlevDerKlikketPå == knap3) {

        Thread.sleep(30000); // blokér hovedtråd i 30 sekunder

      } else if (hvadBlevDerKlikketPå == knap4) {
        textView.setText("Henter...");

        new AsyncTask() {
          @Override
          protected Object doInBackground(Object... arg0) {
            try {
              Galgelogik gl = new Galgelogik(); // lidt mere netværk, for et syns skyld
              gl.hentOrdFraDr();

              String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
              String titler = findTitler(rssdata);
              return titler;
            } catch (Exception e) {
              e.printStackTrace();
              return e;
            }
          }

          @Override
          protected void onPostExecute(Object titler) {
            textView.setText("resultat: \n" + titler);
            setProgressBarIndeterminateVisibility(false);
          }
        }.execute();


      } else if (hvadBlevDerKlikketPå == knap5) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String titler = prefs.getString("titler", "(henter, vent et øjeblik)"); // Hent fra prefs
        textView.setText("henter... her er hvad jeg ved:\n" + titler);

        new AsyncTask() {
          @Override
          protected Object doInBackground(Object... arg0) {
            try {
              Galgelogik gl = new Galgelogik(); // lidt mere netværk, for et syns skyld
              gl.hentOrdFraDr();

              String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
              String titler = findTitler(rssdata);
              prefs.edit().putString("titler", titler).apply();     // Gem i prefs
              return titler;
            } catch (Exception e) {
              e.printStackTrace();
              return e;
            }
          }

          @Override
          protected void onPostExecute(Object titler) {
            textView.setText("nyeste titler: \n" + titler);
            setProgressBarIndeterminateVisibility(false);
          }
        }.execute();

      }
    } catch (Exception e) {
      e.printStackTrace();
      textView.setText("DER SKETE EN FEJL:\n");
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      textView.append(sw.toString());
      setProgressBarIndeterminateVisibility(false);
    }

  }
}

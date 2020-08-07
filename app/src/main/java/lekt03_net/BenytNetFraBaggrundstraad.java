package lekt03_net;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lekt02_aktiviteter.Galgelogik;

public class BenytNetFraBaggrundstraad extends AppCompatActivity implements OnClickListener {
  Button knap1, knap2, knap3, knap4, knap5;
  TextView textView;
  ProgressBar progressBar;

  /* Husk følgende i app/build.gradle, så vi benytter Java 8 og kan lave lambda-udtryk:
  android {
  ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
   */
  Executor bgThread = Executors.newSingleThreadExecutor(); // håndtag til en baggrundstråd
  Handler uiThread = new Handler(Looper.getMainLooper());  // håndtag til forgrundstråden


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    knap1 = new Button(this);
    knap1.setText("Blokér hovedtråden i 10 sekunder\n(man får ANR - Application Not Responding)");

    knap2 = new Button(this);
    knap2.setText("Netværk i forgrundstråden\n(det må man ikke)");

    knap3 = new Button(this);
    knap3.setText("Netværk i forgrundstråden\n(tjek for blokering af hovedtråd omgået - rigtig dårlig idé)");

    knap4 = new Button(this);
    knap4.setText("Netværk i baggrundstråd");

    knap5 = new Button(this);
    knap5.setText("Vis cachet kopi mens der hentes i baggrunden");

    tl.addView(knap1);
    tl.addView(knap2);
    tl.addView(knap3);
    tl.addView(knap4);
    tl.addView(knap5);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);

    progressBar = new ProgressBar(this);
    progressBar.setVisibility(View.GONE);
    tl.addView(progressBar);

    textView = new TextView(this);
    textView.setText("\nDemo af forskellige praksisser til netværkskommunikation");
    tl.addView(textView);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(tl);
    setContentView(scrollView);

  }

  public void onClick(View klikPåHvad) {
    setProgressBarIndeterminateVisibility(true);
    try {
      textView.setText( "Du trykkede på " + klikPåHvad);
      //textView.setText( "Du trykkede på " + ((Button) klikPåHvad).getText());
      progressBar.setVisibility(View.VISIBLE);

      if (klikPåHvad == knap1) {

        Thread.sleep(30000); // blokér hovedtråd i 30 sekunder

      } else if (klikPåHvad == knap2) {

        // Nedenstående giver NetworkOnMainThreadException - netværkskommunikation ikke må ske på hovedtråden
        String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
        String titler = findTitler(rssdata);
        textView.setText(titler);
        progressBar.setVisibility(View.GONE);

      } else if (klikPåHvad == knap3) {
        // Tillad netværkskommunikation på hovedtråden
        // Fint til lige at prøve noget, men dårlig idé i længden og IKKE TILLADT i en aflevering
        StrictMode.ThreadPolicy normalThreadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build()); // FRARÅDES !!!!

        String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
        String titler = findTitler(rssdata);
        Galgelogik gl = new Galgelogik();
        gl.hentOrdFraDr();  // lidt mere netværk, for et syns skyld

        StrictMode.setThreadPolicy(normalThreadPolicy); // nulstil ThreadPolicy

        textView.setText(titler);
        progressBar.setVisibility(View.GONE);

      } else if (klikPåHvad == knap4) {

        textView.setText("Henter...");

        bgThread.execute(() -> {
          try {
            String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
            String titler = findTitler(rssdata);
            Galgelogik gl = new Galgelogik();
            gl.hentOrdFraDr();  // lidt mere netværk, for et syns skyld

            uiThread.post(() -> {
              textView.setText("resultat: \n" + titler);
              progressBar.setVisibility(View.GONE);
            });
          } catch (Exception e) {
            e.printStackTrace();
            uiThread.post(() -> {
              textView.setText("Der opstod en fejl: \n" + e.getLocalizedMessage());
              progressBar.setVisibility(View.GONE);
            });
          }
        });


      } else if (klikPåHvad == knap5) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String gamleTitler = prefs.getString("titler", "(henter, vent et øjeblik)"); // Hent fra prefs
        textView.setText("henter... her er foreløbig de gamle titler:\n" + gamleTitler);

        bgThread.execute(() -> {
          try {
            String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
            String titler = findTitler(rssdata);
            Galgelogik gl = new Galgelogik();
            gl.hentOrdFraDr();  // lidt mere netværk, for et syns skyld

            prefs.edit().putString("titler", titler).apply(); // Gem de nyligt hentede data i prefs
            uiThread.post(() -> {
              textView.setText("nyeste titler: \n" + titler);
              progressBar.setVisibility(View.GONE);
            });
          } catch (Exception e) {
            e.printStackTrace();
            uiThread.post(() -> {
              textView.setText("Der opstod en fejl: \n" + e.getLocalizedMessage());
              progressBar.setVisibility(View.GONE);
            });
          }
        });

      }
    } catch (Exception e) {
      e.printStackTrace();
      textView.setText("DER SKETE EN FEJL:\n");
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      textView.append(sw.toString());
      progressBar.setVisibility(View.GONE);
    }
  }




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

}

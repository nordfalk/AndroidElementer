package lekt06_asynkron;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Asynk3Executor extends AppCompatActivity implements OnClickListener {
  ProgressBar progressBar;
  Button knap1, knap2, knap3, knap3annuller;
  boolean annullereret;

  Executor bgThread = Executors.newSingleThreadExecutor();
  Handler uiThread = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Viser god praksis, hvor en baggrundstråd startes og benytter en Handler til at få brugergrænsefladen opdateret fra hovedtråden");
    editText.append("\nØvelse: Tryk på flere af knapperne kort tid efter hinanden");
    editText.append("\nØvelse: Rotér skærmen. Gå ud og ind af skærmbilledet. Overlever baggrundsopgaverne? (svar: ja, men de mister forbindelsen med brugergrænsefladen så man kan ikke se dem)");
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    tl.addView(progressBar);

    knap1 = new Button(this);
    knap1.setText("Basal Executor");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Executor med løbende opdatering");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Executor med løbende opdatering og resultat");
    tl.addView(knap3);

    knap3annuller = new Button(this);
    knap3annuller.setText("Annullér");
    knap3annuller.setVisibility(View.GONE); // Skjul knappen
    tl.addView(knap3annuller);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap3annuller.setOnClickListener(this);
  }

  public void onClick(View klikPåHvad) {

    if (klikPåHvad == knap1) {

      bgThread.execute(() -> {
        uiThread.post(() -> knap1.setText("arbejder"));
        SystemClock.sleep(10000); // <2>
        uiThread.post(() -> knap1.setText("færdig!"));
      });
      knap1.setText("startet");


    } else if (klikPåHvad == knap2) {

      int antalSkridt = 100;
      bgThread.execute(() -> {
        uiThread.post(() -> knap2.setText("arbejder"));
        for (int i = 0; i < antalSkridt; i++) {  // <2>
          System.out.println("knap2 i = " + i);
          SystemClock.sleep(10000 / antalSkridt);
          int finalI = i; // da i kan ændre sig imens nedenstående kører skal den kopieres over i en final variabel
          uiThread.post(() -> {
            knap2.setText("i = " + finalI);
            progressBar.setProgress(finalI);
          });
        }
        uiThread.post(() -> knap2.setText("færdig!")); // <3>
        System.out.println("knap2 færdig");
      });
      knap2.setText("startet");


    } else if (klikPåHvad == knap3) {

      annullereret = false;
      int antalSkridt = 100;
      int ventPrSkridtMs = 50;

      bgThread.execute(() -> {
        uiThread.post(() -> knap3.setText("arbejder"));
        for (int i = 0; i < antalSkridt; i++) {
          System.out.println("knap3 i = " + i);
          SystemClock.sleep(ventPrSkridtMs);
          if (annullereret) break; // stop uden resultat

          double procent = i * 100.0 / antalSkridt;
          System.out.println("procent = "+procent);
          double resttidISekunder = (antalSkridt - i) * ventPrSkridtMs / 100 / 10.0;

          uiThread.post(() -> {
            knap3annuller.setVisibility(View.VISIBLE);
            knap3.setText("arbejder - " + procent + "% færdig, mangler " + resttidISekunder + " sekunder endnu");
            progressBar.setProgress((int) procent);
          });
        }
        System.out.println("knap3 færdig");

        uiThread.post(() -> {
          if (annullereret) knap3.setText("Annulleret før tid");
          else knap3.setText("færdig");
          knap3annuller.setVisibility(View.GONE); // Skjul knappen
        });
      });
      knap3.setText("startet");


    } else if (klikPåHvad == knap3annuller) {

      annullereret = true;

    }
  }
}

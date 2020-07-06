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

  Executor executor = Executors.newSingleThreadExecutor();
  Handler uiThread = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    tl.addView(progressBar);

    knap1 = new Button(this);
    knap1.setText("Basal AsyncTask");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("AsyncTask med løbende opdatering");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("AsyncTask med løbende opdatering og resultat");
    tl.addView(knap3);

    knap3annuller = new Button(this);
    knap3annuller.setText("Annullér asyncTask3");
    knap3annuller.setVisibility(View.GONE); // Skjul knappen
    tl.addView(knap3annuller);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap3annuller.setOnClickListener(this);
  }

  public void onClick(View v) {

    if (v == knap1) {

      knap1.setText("arbejder"); // <1>
      executor.execute(() -> {
        SystemClock.sleep(10000); // <2>
        uiThread.post(() -> knap1.setText("færdig!"));  // <3>
      });


    } else if (v == knap2) {

      int antalSkridt = 100;
      executor.execute(() -> {
        for (int i = 0; i < antalSkridt; i++) {  // <2>
          SystemClock.sleep(10000 / antalSkridt);
          int finalI = i;
          uiThread.post(() -> {
            knap2.setText("i = " + finalI);
            progressBar.setProgress(finalI);
          });
        }
        SystemClock.sleep(10000);
        uiThread.post(() -> knap1.setText("færdig!")); // <3>
      });
      knap2.setText("arbejder"); // <1>


    } else if (v == knap3) {

      annullereret = false;
      int antalSkridt = 100;
      int ventetidPrSkridtMs = 50;

      executor.execute(() -> {
        for (int i = 0; i < antalSkridt; i++) {
          SystemClock.sleep(ventetidPrSkridtMs);
          if (annullereret) break; // stop uden resultat

          double procent = i * 100.0 / antalSkridt;
          System.out.println("procent = "+procent);
          double resttidISekunder = (antalSkridt - i) * ventetidPrSkridtMs / 100 / 10.0;

          uiThread.post(() -> {
            knap3annuller.setVisibility(View.VISIBLE);
            knap3.setText("arbejder - " + procent + "% færdig, mangler " + resttidISekunder + " sekunder endnu");
            progressBar.setProgress((int) procent);
          });
        }

        uiThread.post(() -> {
          if (!annullereret) knap3.setText("Annulleret før tid");
          else knap3.setText("færdig");
          knap3annuller.setVisibility(View.GONE); // Skjul knappen
        });

      });


    } else if (v == knap3annuller) {
      annullereret = true;
    }
  }
}

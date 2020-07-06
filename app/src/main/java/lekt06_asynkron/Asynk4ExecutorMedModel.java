package lekt06_asynkron;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser hvordan en model er knyttet korrekt til en aktivitet.
 * Hvis skærmen vendes knyttes modellen korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 */
public class Asynk4ExecutorMedModel extends AppCompatActivity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;
  static Baggrundsopgave baggrundsopgave;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    editText.setId(R.id.enKnap); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(progressBar);

    knap = new Button(this);
    knap.setText("Start opgaven");
    tl.addView(knap);

    annullerknap = new Button(this);
    annullerknap.setText("Annullér opgaven");
    tl.addView(annullerknap);

    setContentView(tl);

    knap.setOnClickListener(this);
    annullerknap.setOnClickListener(this);

    // Hvis der er sket en konfigurationsændring så kan det være vi har en gammel
    // baggrundsopgave som allerede kører, og som vi nu skal observere
    if (baggrundsopgave!=null) baggrundsopgave.observer = opdatérGui;
    opdatérGui.run();
  }


  @Override
  protected void onDestroy() {
    baggrundsopgave.observer = null; // Vigtigt, ellers bliver aktiviteten hængende i hukommelsen
    super.onDestroy();
  }


  private Runnable opdatérGui = new Runnable() {
    @Override
    public void run() {
      if (baggrundsopgave == null) { // hvis opgaven ikke er startet
        annullerknap.setVisibility(View.GONE);
        return;
      }

      progressBar.setProgress((int) baggrundsopgave.procent);

      if (baggrundsopgave.annullereret) {
        knap.setText("Annulleret før tid");
        annullerknap.setVisibility(View.GONE);
        return;
      }

      String tekst = "arbejder - " + baggrundsopgave.procent + "% færdig, mangler " + baggrundsopgave.resttidISekunder + " sekunder endnu";
      Log.d("AsyncTask", tekst);
      knap.setText(tekst);
      annullerknap.setVisibility(View.VISIBLE);
    }
  };



  public void onClick(View klikPåHvad) {
    if (klikPåHvad == knap) {
      baggrundsopgave = new Baggrundsopgave();
      baggrundsopgave.observer = opdatérGui;
      baggrundsopgave.startBeregning();
    } else if (klikPåHvad == annullerknap) {
      baggrundsopgave.annullereret = true;
    }
    opdatérGui.run();
  }




  static class Baggrundsopgave {
    double procent;
    double resttidISekunder;
    boolean annullereret;
    boolean færdig;

    Executor bgThread = Executors.newSingleThreadExecutor();
    Handler uiThread = new Handler();
    Runnable observer; // reference til GUI-objekterne

    void startBeregning() {
      bgThread.execute(() -> udførBeregningBg());
    }

    private void udførBeregningBg() {
      int antalSkridt = 500;
      int ventPrSkridtMs = 50;
      for (int i = 0; i < antalSkridt; i++) {
        // simulér nogle krævende beregninger eller netværkskald
        SystemClock.sleep(ventPrSkridtMs);
        procent = i * 100.0 / antalSkridt;
        resttidISekunder = (antalSkridt - i) * ventPrSkridtMs / 100 / 10.0;

        uiThread.post(observer);
        if (annullereret) return;
      }
      færdig = true;
      uiThread.post(observer);
    }

  }
}


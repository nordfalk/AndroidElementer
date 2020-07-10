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

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser hvordan en model er knyttet korrekt til en aktivitet.
 * Hvis skærmen vendes knyttes modellen korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 * Modellen overlever at man forlader aktiviteten (trykker på tilbage) og går ind igen
 */
public class Asynk4ExecutorModel extends AppCompatActivity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;
  static MinModel minModel;  // bemærk: static - overlever så længe programmet stadig er i RAM

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Viser god praksis, hvor et model-objekt er knyttet til brugergrænsefladen");
    editText.append("\n\nØvelse: Rotér skærmen. Gå ud og ind af skærmbilledet. Overlever baggrundsopgaverne? (svar: ja, og de beholder forbindelsen med brugergrænsefladen så man se dem)");
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
    if (minModel !=null) minModel.observer = opdaterSkærm;
    opdaterSkærm.run();
  }


  @Override
  protected void onDestroy() {
    if (minModel!=null) minModel.observer = null; // Vigtigt, ellers kan aktiviteten ikke garbage collectes
    super.onDestroy();
  }


  private Runnable opdaterSkærm = new Runnable() {
    @Override
    public void run() {
      if (minModel == null) { // hvis opgaven ikke er startet
        annullerknap.setVisibility(View.GONE);
        return;
      }

      progressBar.setProgress((int) minModel.procent);
      annullerknap.setVisibility(minModel.kører ? View.VISIBLE : View.GONE);

      if (minModel.annullereret) knap.setText("Annulleret før tid");
      else if (!minModel.kører) knap.setText("Færdig");
      else knap.setText("arbejder - " + minModel.procent + "% færdig, mangler " + minModel.resttidISekunder + " sekunder endnu");
    }
  };



  public void onClick(View klikPåHvad) {
    if (klikPåHvad == knap) {
      minModel = new MinModel();
      minModel.observer = opdaterSkærm;
      minModel.startBeregning(500, 50);
    } else if (klikPåHvad == annullerknap) {
      minModel.annullereret = true;
    }
    opdaterSkærm.run();
  }




  static class MinModel {
    double procent;
    double resttidISekunder;
    boolean annullereret;
    boolean kører;

    Executor bgThread = Executors.newSingleThreadExecutor(); // håndtag til en baggrundstråd
    Handler uiThread = new Handler();                        // håndtag til forgrundstråden
    Runnable observer; // reference til brugergrænsefladen (aktiviteten observerer modellen)

    void startBeregning(int antalSkridt, int ventPrSkridtMs) {
      bgThread.execute(() -> {
        kører = true;
        for (int i = 0; i < antalSkridt; i++) {
          System.out.println("i = " + i);
          SystemClock.sleep(ventPrSkridtMs); // simulér nogle krævende beregninger eller netværkskald
          if (annullereret) break;
          procent = i * 100.0 / antalSkridt;
          resttidISekunder = (antalSkridt - i) * ventPrSkridtMs / 100 / 10.0;
          uiThread.post(observer);
        }
        kører = false;
        uiThread.post(observer);
        System.out.println("færdig");
      });
    }
  }
}


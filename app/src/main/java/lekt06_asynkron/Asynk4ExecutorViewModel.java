package lekt06_asynkron;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser hvordan en ViewModel knyttes til en aktivitet.
 * Hvis skærmen vendes knyttes modellen korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 * En ViewModel overlever ikke at man forlader aktiviteten (tryk på tilbage) og går ind igen
 */
public class Asynk4ExecutorViewModel extends AppCompatActivity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;
  MinViewModel minModel;

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

    // kræver flg bibliotek i app/build.gradle:
//    implementation 'android.arch.lifecycle:extensions:2.1.0'
//  viewModel = androidx.lifecycle.ViewModelProviders.of(this).get(MinViewModel.class);
// ny syntax:   viewModel = new ViewModelProvider(this).get(MinViewModel.class)

    // hurtigt hack hvis lifecycle:extensions ikke er inkluderet:
    minModel =  new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MinViewModel.class);

    minModel.observabelLiveData.observe(this, opdaterSkærm);
  }


  private Observer opdaterSkærm = new Observer() {
    @Override
    public void onChanged(Object o) {
      progressBar.setProgress((int) minModel.procent);
      annullerknap.setVisibility(minModel.kører ? View.VISIBLE : View.GONE);

      if (minModel.annullereret) knap.setText("Annulleret før tid");
      else if (!minModel.kører) knap.setText("Færdig");
      else knap.setText("arbejder - " + minModel.procent + "% færdig, mangler " + minModel.resttidISekunder + " sekunder endnu");
    }
  };


  public void onClick(View klikPåHvad) {
    if (klikPåHvad == knap) {
      minModel.startBeregning(500, 50);
    } else if (klikPåHvad == annullerknap) {
      minModel.annullereret = true;
    }
  }

  public static class MinViewModel extends ViewModel {
    // Data, som aktiviteten skal vise
    double procent;
    double resttidISekunder;
    boolean annullereret;
    boolean kører;

    Executor bgThread = Executors.newSingleThreadExecutor();
    // Vi bruger her LiveData som observabel til at fortælle aktiviteten når der er sket ændringer
    MutableLiveData observabelLiveData = new MutableLiveData();


    public void startBeregning(int antalSkridt, int ventPrSkridtMs) {
      bgThread.execute(() -> {
        kører = true;
        for (int i = 0; i < antalSkridt; i++) {
          System.out.println("i = " + i);
          SystemClock.sleep(ventPrSkridtMs); // simulér nogle krævende beregninger eller netværkskald
          if (annullereret) break;
          procent = i * 100.0 / antalSkridt;
          resttidISekunder = (antalSkridt - i) * ventPrSkridtMs / 100 / 10.0;
          observabelLiveData.postValue(null);
        }
        kører = false;
        observabelLiveData.postValue(null);
        System.out.println("færdig");
      });
    }

    /** Denne metode køres når brugeren afslutter aktiviteten (trykker på tilbage-knappen) */
    @Override
    protected void onCleared() {
      if (kører) annullereret = true;
    }
  }
}


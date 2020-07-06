package lekt06_asynkron;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser en AsyncTask der er knyttet korrekt til en aktivitet.
 * Hvis skærmen vendes knyttes AsyncTask'en korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 */
public class Asynk4ViewModel extends AppCompatActivity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;
  MinViewModel viewModel;


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
    viewModel =  new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MinViewModel.class);

    viewModel.observabelLiveData.observe(this, o -> {
      annullerknap.setVisibility(viewModel.kører? View.VISIBLE : View.GONE);
      knap.setText(viewModel.knapTekst);
      progressBar.setProgress(viewModel.progressBarVærdi);
    });
  }


  public void onClick(View klikPåHvad) {
    if (klikPåHvad == knap) {
      viewModel.startAsyncTask(500, 50);
    } else if (klikPåHvad == annullerknap) {
      viewModel.asyncTask.cancel(false);
    }
  }

  public static class MinViewModel extends ViewModel {
    // Data, som aktiviteten skal vise
    int progressBarVærdi;
    String knapTekst;
    boolean kører;

    // Vi bruger her LiveData som observabel for at aktiviteten ved hvornår der er sket ændringer
    MutableLiveData observabelLiveData = new MutableLiveData();

    AsyncTask asyncTask;

    public void startAsyncTask(final int antalSkridt, final int ventetidPrSkridtIMilisekunder) {
      asyncTask = new AsyncTask() {
        @Override
        protected String doInBackground(Object... param) {
          for (int i = 0; i < antalSkridt; i++) {
            SystemClock.sleep(ventetidPrSkridtIMilisekunder);
            if (isCancelled()) {
              knapTekst = "Annulleret før tid";
              kører = false;
              observabelLiveData.postValue(null);
              return null;
            }
            double procent = i * 100.0 / antalSkridt;
            double resttidISekunder = (antalSkridt - i) * ventetidPrSkridtIMilisekunder / 100 / 10.0;
            String tekst = "arbejder - " + procent + "% færdig, mangler " + resttidISekunder + " sekunder endnu";
            Log.d("AsyncTask", tekst);
            knapTekst = tekst;
            progressBarVærdi = (int) procent;
            kører = true;
            observabelLiveData.postValue(null);
          }
          knapTekst = "færdig med doInBackground()!";
          kører = false;
          observabelLiveData.postValue(null);
          return null;
        }
      }.execute();
    }

    /** Denne metode køres når brugeren afslutter aktiviteten (trykker på tilbage-knappen) */
    @Override
    protected void onCleared() {
      if (kører) asyncTask.cancel(false);
    }
  }
}


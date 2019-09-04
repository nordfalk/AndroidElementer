package lekt03_diverse;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dk.nordfalk.android.elementer.R;


public class Talegenkendelse extends AppCompatActivity implements View.OnClickListener {
  Button startTalegenkendelse;
  TextView genkendtTale;
  private ScrollView sv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startTalegenkendelse = new Button(this);
    startTalegenkendelse.setOnClickListener(this);
    startTalegenkendelse.setText("Start talegenkendelse");

    genkendtTale = new TextView(this);
    genkendtTale.setText("Genkendt tekst kommer her.\n\nDu skal tale "+Locale.getDefault().getDisplayLanguage());
    genkendtTale.setId(R.id.editText); // sæt ID så den redigerede tekst bliver genskabt ved skærmvending
    Log.d("Talegenkendelse", "onCreate Locale.getDefault()="+Locale.getDefault());

    TableLayout ll = new TableLayout(this);
    ll.addView(startTalegenkendelse);
    //ll.addView(genkendtTale, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
    ll.addView(genkendtTale);

    sv = new ScrollView(this);
    sv.addView(ll);
    setContentView(sv);
  }

  public void onClick(View view) {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Sig noget på "+Locale.getDefault().getDisplayLanguage());
    try {
      startActivityForResult(intent, 1234); // bare et eller andet tal
    } catch (ActivityNotFoundException ex) {
      ex.printStackTrace();
      genkendtTale.append("\n\nDer skete en fejl: "+ex);
    }
  }
//xxx hvorfor virker den ikke?

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
      genkendtTale.append("\n\nonActivityResult OK data="+data.getExtras());
      ArrayList<String> resultatListe = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      for (String resultat : resultatListe) {
        genkendtTale.append("\n\nGenkendt: "+resultat);
      }
    } else {
      genkendtTale.append("\n\nonActivityResult Ikke genkendt "+resultCode+" data="+data);
    }
    sv.scrollTo(0, Integer.MAX_VALUE); // rul ned til bunden
  }
}

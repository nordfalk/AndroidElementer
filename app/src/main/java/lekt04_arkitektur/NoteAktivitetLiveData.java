package lekt04_arkitektur;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

/**
 * Simpel aktivitet til at tage noter
 *
 * @author Jacob Nordfalk
 */
public class NoteAktivitetLiveData extends AppCompatActivity implements OnClickListener {

  EditText noteEditText;
  private TextView alleNoterTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    TextView textView = new TextView(this);
    textView.setText("Velkommen, " + MinApp.getData().navn + ", skriv dine noter herunder:");
    linearLayout.addView(textView);

    noteEditText = new EditText(this);
    linearLayout.addView(noteEditText);

    Button okKnap = new Button(this);
    okKnap.setText("OK");
    okKnap.setOnClickListener(this);
    linearLayout.addView(okKnap);

    alleNoterTv = new TextView(this);
    alleNoterTv.setText("");
    linearLayout.addView(alleNoterTv);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(linearLayout);
    setContentView(scrollView);

    MinApp.livedata.observe(this, new Observer<Programdata>() {
      @Override
      public void onChanged(Programdata programdata) {
        opdaterSkærm(programdata);
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Ingen afregistrering nødvendig! AppCompatActivity afregistrerer sig selv :-)
  }

  @Override
  public void onClick(View v) {
    String note = noteEditText.getText().toString();
    noteEditText.setText("");

    Programdata data = MinApp.livedata.getValue();
    data.noter.add(note);
    MinApp.livedata.setValue(data);
  }

  private void opdaterSkærm(Programdata programdata) {
    String notetekst = programdata.noter.toString().replaceAll(", ", "\n");
    alleNoterTv.setText("Noter:\n" + notetekst);
  }
}

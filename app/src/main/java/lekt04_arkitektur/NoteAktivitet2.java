package lekt04_arkitektur;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Simpel aktivitet til at tage noter
 *
 * @author Jacob Nordfalk
 */
public class NoteAktivitet2 extends AppCompatActivity implements OnClickListener, Runnable {

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

    MinApp.getData().observatører.add(this);
    run(); // opdatér skærm
  }


  @Override
  public void run() {
    String notetekst = MinApp.getData().noter.toString().replaceAll(", ", "\n");
    alleNoterTv.setText("Noter:\n" + notetekst);
  }

  @Override
  public void onClick(View v) {
    String note = noteEditText.getText().toString();
    noteEditText.setText("");
    MinApp.getData().noter.add(note);    // ændr modellen
    MinApp.getData().kaldObservatører(); // kalder run() på dataobservatør (og evt andre observatører)
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    MinApp.getData().observatører.remove(this); // ryd op (undgå hukommelseslæk)
  }
}

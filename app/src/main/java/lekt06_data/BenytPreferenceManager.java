package lekt06_data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BenytPreferenceManager extends AppCompatActivity implements View.OnClickListener {

  SharedPreferences prefs;
  TextView textView;
  EditText editText;
  Button hent;
  Button gem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    textView = new TextView(this);
    textView.setText("Skriv noget nedenfor");
    linearLayout.addView(textView);

    editText = new EditText(this);
    editText.setHint("Skriv editText her");
    linearLayout.addView(editText);

    hent = new Button(this);
    hent.setText("Hent");
    linearLayout.addView(hent);

    gem = new Button(this);
    gem.setText("Gem");
    linearLayout.addView(gem);

    setContentView(linearLayout);


    hent.setOnClickListener(this);
    gem.setOnClickListener(this);

    prefs = PreferenceManager.getDefaultSharedPreferences(this); // i et fragment, skal der stå "getActivity()" i stedet for "this"
  }

  @Override
  public void onClick(View view) {

    if (view == gem) {
      String gemTekst = editText.getText().toString();
      prefs.edit().putString("editText", gemTekst).apply();

      textView.setText("Teksten blev gemt i PreferenceManager. Prøv at hente det nu, gerne efter en programgenstart");
    }
    else if (view == hent) {
      String gemtTekst = prefs.getString("editText", "Ingen gemt editText fundet");
      editText.setText(gemtTekst);

      textView.setText("Teksten blev hentet fra PreferenceManager");
    }
  }
}
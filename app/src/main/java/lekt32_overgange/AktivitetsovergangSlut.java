package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

public class AktivitetsovergangSlut extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt32_overgange_slut);

    ViewCompat.setTransitionName(findViewById(R.id.ikon), "ikon");
    ViewCompat.setTransitionName(findViewById(R.id.enKnap), "knappen");

    String knapTeksten = getIntent().getStringExtra("knap-teksten");
    if (knapTeksten==null) knapTeksten = "Hov, fik ikke en knap-tekst?";
    ((Button) findViewById(R.id.enKnap)).setText(knapTeksten);
  }

}

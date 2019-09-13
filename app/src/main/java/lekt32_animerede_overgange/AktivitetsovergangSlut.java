package lekt32_animerede_overgange;

import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
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

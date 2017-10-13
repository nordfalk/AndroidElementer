package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

public class AktivitetsovergangSlut extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt32_overgange_slut);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      findViewById(R.id.ikon).setTransitionName("ikon");
      findViewById(R.id.enKnap).setTransitionName("knappen");
    }

    String knapTeksten = getIntent().getStringExtra("knap-teksten");
    if (knapTeksten==null) knapTeksten = "Hov, fik ikke en knap-tekst?";
    ((Button) findViewById(R.id.enKnap)).setText(knapTeksten);
  }

}

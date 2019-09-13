package lekt32_animerede_overgange;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

public class AktivitetsovergangBegynd extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt01_tre_knapper);
    findViewById(R.id.knap1).setOnClickListener(this);
    findViewById(R.id.knap2).setOnClickListener(this);
    findViewById(R.id.knap3).setOnClickListener(this);
  }

  public void onClick(View knappen) {

    Intent intent = new Intent(this, AktivitetsovergangSlut.class);
    intent.putExtra("knap-teksten", ((Button) knappen).getText());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Lav bindinger til mål-aktiviteten, så der kan laves glidende overgange
      // Navnene skal passe med det TransitionName viewsne har i mål-aktiviteten
      Pair<View, String> par1 = Pair.create(findViewById(R.id.ikon), "ikon");
      Pair<View, String> par2 = Pair.create(knappen, "knappen");

      ActivityOptionsCompat options = ActivityOptionsCompat.
              makeSceneTransitionAnimation(this, par1, par2);
      startActivity(intent, options.toBundle());
    } else {
      startActivity(intent);
    }
  }
}

package lekt10_livscyklus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

public class ManglendeKaldTilSuperklasse extends AppCompatActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText(this, "onPause() mangler at kalde videre i superklassen,", Toast.LENGTH_LONG).show();
    Toast.makeText(this, "så når du vender skærnen eller trykker tilbage så vil aktiviteten crashe", Toast.LENGTH_LONG).show();
  }

  @SuppressLint("MissingSuperCall")
  @Override
  public void onPause() {
    // dette manglende kald vil få app'en til at crashe
    //super.onResume();
  }
}

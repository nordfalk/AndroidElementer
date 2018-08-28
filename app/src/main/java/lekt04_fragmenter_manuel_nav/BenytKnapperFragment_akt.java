package lekt04_fragmenter_manuel_nav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dk.nordfalk.android.elementer.R;
import lekt04_fragmenter_manuel_nav.BenytKnapperFragment;


public class BenytKnapperFragment_akt extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt04_fragmenter_manuel_nav);

    if (savedInstanceState == null) {
      BenytKnapperFragment fragment = new BenytKnapperFragment();
      getSupportFragmentManager().beginTransaction()
              .add(R.id.fragmentindhold, fragment)  // tom container i layout
              .commit();
    }
  }
}

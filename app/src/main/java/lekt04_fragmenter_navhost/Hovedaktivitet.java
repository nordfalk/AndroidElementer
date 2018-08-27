package lekt04_fragmenter_navhost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Hovedaktivitet extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // I dette layout er der et NavHostFragment
    // (som bruger resursen navigation/lekt_04_fragnav_graph
    //  som igen starter lekt04_fragnav.Velkomst_frag
    //  som lidt senere starter lekt04_fragnav.Hovedmenu_frag)

    // Se https://developer.android.com/topic/libraries/architecture/navigation/
    setContentView(R.layout.lekt04_fragmenter_navhost);
  }
}

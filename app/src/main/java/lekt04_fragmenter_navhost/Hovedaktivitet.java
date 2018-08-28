package lekt04_fragmenter_navhost;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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

    // Hvis du ønsker at på fat i NavController og påvirke navigering fra aktiviteten kan det gøres med:
    //NavHostFragment frag = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.lekt_04_navhost_fragment);
    //NavController navController = frag.getNavController();
  }
}

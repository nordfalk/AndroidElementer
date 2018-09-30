package lekt04_fragmenter_navhost;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class HovedaktivitetBundnavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // I dette layout er der et NavHostFragment
    // (som bruger resursen navigation/lekt_04_fragnav_graph
    //  som igen starter lekt04_fragnav.Velkomst_frag
    //  som lidt senere starter lekt04_fragnav.Hovedmenu_frag)

    // Se https://developer.android.com/topic/libraries/architecture/navigation/
    setContentView(R.layout.lekt04_fragmenter_navhost);


    BottomNavigationView bottomNavigationView = findViewById(R.id.bundnavigation);
    bottomNavigationView.setVisibility(View.VISIBLE);
    bottomNavigationView.setOnNavigationItemSelectedListener(this);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    // Hvis du ønsker at på fat i NavController og påvirke navigering fra aktiviteten kan det gøres med:
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.lekt_04_navhost_fragment);
    NavController navController = navHostFragment.getNavController();

    if (item.getItemId() == R.id.javabog) {

      navController.navigate(R.id.hjælp);

    } else if (item.getItemId() == R.id.bil) {

      navController.navigate(R.id.spillet);

    } else if (item.getItemId() == R.id.indstillinger) {

      navController.navigate(R.id.indstillinger);

    } else if (item.getItemId() == R.id.afslut) {

      navController.popBackStack();

    } else {
      // Ikke håndteret - send kaldet videre til standardhåntering
      return super.onOptionsItemSelected(item);
    }
    return false;
  }
}

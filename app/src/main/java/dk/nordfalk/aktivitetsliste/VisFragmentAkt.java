package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import lekt04_fragmenter.BenytKnapperFragment;


/**
 * Aktivitet der instantierer og viser ét fragment.
 *
 * Benyttes sådan her:
 *
 * startActivity(new Intent(this, VisFragmentAkt.class)
 *      .putExtra(VisFragmentAkt.VIS_FRAGMENT_KLASSE, nr02_loginFragment.class.getName())
 */
public class VisFragmentAkt extends AppCompatActivity {

  public static final String VIS_FRAGMENT_KLASSE = "VIS_FRAGMENT_KLASSE";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    try {
      super.onCreate(savedInstanceState);
      //super.getActionBarSetDisplayHomeAsUpEnabledKompat(true);
      //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      FrameLayout fl = new FrameLayout(this);
      fl.setId(android.R.id.input);
      setContentView(fl);

      if (savedInstanceState != null) {
        return;
      }

      String klasse = getIntent().getStringExtra(VIS_FRAGMENT_KLASSE);
      if (klasse==null) {
        klasse = BenytKnapperFragment.class.getName();
      }
      Object obj = Class.forName(klasse).newInstance();
      if (obj instanceof Activity) {
        Log.d("VisFragmentAkt, ", "HOV, " + obj + " er en aktivitet - starter den");
        startActivity(new Intent(this, obj.getClass()));
        finish();
      }
      // Vis fragmentet i FrameLayoutet
      Bundle b = getIntent().getExtras();
      Log.d("VisFragmentAkt", "Viser fragment " + obj + " med arg " + b);
      Fragment f = (Fragment) Class.forName(klasse).newInstance();
      f.setArguments(b);

      getSupportFragmentManager().beginTransaction().add(android.R.id.input, f).commit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
    }
    return super.onOptionsItemSelected(item);
  }
}
package lekt04_fragmenter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import dk.nordfalk.aktivitetsliste.ZoomOutPageTransformer;
import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class HovedaktivitetMedViewpager extends AppCompatActivity {
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.viewpager_med_titel);

    viewPager = findViewById(R.id.viewPager);
    viewPager.setAdapter(new GalgelegViewPagerAdapter(getSupportFragmentManager()));
    viewPager.setPageTransformer(false, new ZoomOutPageTransformer());

    setTitle("Hovedaktivitet");
    // Man kan trykke på app-ikonet i øverste venstre hjørne
    // (og det betyder at brugeren vil navigere op i hierakiet)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed(); // brugeren vil navigere op i hierakiet
    }
    return super.onOptionsItemSelected(item);
  }

  private class GalgelegViewPagerAdapter extends FragmentPagerAdapter {
    public GalgelegViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "fane nummer " + position;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment f = null;
      if (position == 0) f = new Spillet_frag();
      else if (position == 2) f = new BenytKnapperFragment();
      else if (position == 3) f = new Spillet_frag();
      else f = new Hjaelp_frag();

//      if (position == 0) f = new Hovedmenu_frag();
//      else if (position == 5) f = new Velkomst_frag();

      Bundle b = new Bundle();
      b.putInt("position", position);
      f.setArguments(b);
      return f;
    }
  }
}

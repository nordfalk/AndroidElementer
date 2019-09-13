package lekt31_venstremenu;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import dk.nordfalk.android.elementer.R;


public class BenytVenstremenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt31_benyt_venstremenu);

    drawerLayout = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    drawerLayout.openDrawer(navigationView);
  }


  @Override
  public boolean onNavigationItemSelected(MenuItem menuItem) {
    menuItem.setChecked(true);
    drawerLayout.closeDrawers();
    Snackbar.make(drawerLayout, "Du trykkede på "+menuItem.toString(), Snackbar.LENGTH_SHORT).show();
    return true;
  }
}

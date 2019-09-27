package lekt31_venstremenu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import dk.nordfalk.android.elementer.R;


public class BenytVenstremenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
  DrawerLayout drawerLayout;  //
  Toolbar toolbar;
  NavigationView navigationView;
  View coordinatorLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt31_benyt_venstremenu);

    findViewById(R.id.fab).setOnClickListener(this);
    findViewById(R.id.knap1).setOnClickListener(this);
    findViewById(R.id.knap2).setOnClickListener(this);
    findViewById(R.id.knap3).setOnClickListener(this);

    drawerLayout = findViewById(R.id.drawer_layout);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    coordinatorLayout = findViewById(R.id.coordinatorLayout);

    drawerLayout.setScrimColor(Color.TRANSPARENT);

    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        float slideX = drawerView.getWidth() * slideOffset;
        coordinatorLayout.setTranslationX(slideX);
      }
    };
    // Man kan trykke på hamburgermenuen i øverste venstre hjørne
    // (og det betyder at brugeren vil se venstremenuen)
    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      drawerLayout.openDrawer(navigationView); // brugeren vil se venstremenuen
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem menuItem) {
    menuItem.setChecked(true);
    drawerLayout.closeDrawers();
    // Ved at bruge coordinatorLayout eller et view indeni får vi indholdet til at tilpasse sig at snackbaren fylder noget
    Snackbar.make(coordinatorLayout, "Du trykkede på "+menuItem.toString(), Snackbar.LENGTH_SHORT).show();
    return true;
  }

  @Override
  public void onClick(View v) {
    Snackbar.make(coordinatorLayout, "Du trykkede på "+v, Snackbar.LENGTH_SHORT).show();
  }
}

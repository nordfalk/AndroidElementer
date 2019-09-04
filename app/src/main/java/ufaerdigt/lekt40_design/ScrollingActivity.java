// Se http://android-developers.blogspot.de/2015/05/android-design-support-library.html
// for Design Support Library
package ufaerdigt.lekt40_design;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import dk.nordfalk.android.elementer.R;

public class ScrollingActivity extends AppCompatActivity {

  private CollapsingToolbarLayout ctl;
  private FloatingActionButton fab1, fab2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt40_scrolling);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ctl = findViewById(R.id.collapsingToolbarLayout);
    ctl.setTitle("CollapsingToolbar");


    fab1 = findViewById(R.id.fab1);
    fab2 = findViewById(R.id.fab2);

    fab1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "En Snackbar er dukker op i bunden og er synlig et par sekunder og kan swipes væk", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    ctl.setTitle("Snackbar trykket");
                  }
                }).show();
      }
    });

  }

}

package lekt03_diverse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Se https://developer.android.com/training/system-ui/immersive.html
 * @author Jacob Nordfalk
 */
public class TemaFuldskaerm extends AppCompatActivity implements OnClickListener {

  TextView forklaring;
  Button startFuldskærm, slutFuldskærm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    forklaring = new TextView(this);
    forklaring.setText("Aktiviteten er fuldskærm (se i manifestet), men systemets kontroller vises stadig.\n" +
            "De kan skjules som vist i https://developer.android.com/training/system-ui/immersive.html\n" +
            "Når de er skjult kan brugeren få adgang til dem ved at trække fra toppen af skærmen og ned.");
    Linkify.addLinks(forklaring, Linkify.WEB_URLS);
    tl.addView(forklaring);

    startFuldskærm = new Button(this);
    startFuldskærm.setText("Start immersive mode");
    tl.addView(startFuldskærm);

    slutFuldskærm = new Button(this);
    slutFuldskærm.setText("Afslut immersive mode");
    tl.addView(slutFuldskærm);
    setContentView(tl);

    startFuldskærm.setOnClickListener(this);
    forklaring.setOnClickListener(this);
    slutFuldskærm.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    View decorView = getWindow().getDecorView();

    if (hvadBlevDerKlikketPå == startFuldskærm) {

      getWindow().getDecorView().setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                      | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    } else if (hvadBlevDerKlikketPå == slutFuldskærm) {

      getWindow().getDecorView().setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
  }
}

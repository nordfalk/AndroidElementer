package lekt05_grafik;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BraetspilImageView extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

  Button knap1, knap2, knap3;
  ImageView ikon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt01_tre_knapper);
    ikon = findViewById(R.id.ikon);
    ikon.setImageResource(R.drawable.bil);
    knap1 = findViewById(R.id.knap1);
    knap2 = findViewById(R.id.knap2);
    knap3 = findViewById(R.id.knap3);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    findViewById(R.id.indhold).setOnTouchListener(this);
    Snackbar.make(ikon, "Flyt bilen rundt på skærmen - tryk på baggrunden", Snackbar.LENGTH_INDEFINITE).show();
  }

  @Override
  public void onClick(final View knap) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      ikon.animate()
              .rotation(360)
              .setDuration(500)
              .withEndAction(new Runnable() {
        @Override
        public void run() {
          ikon.animate()
                  .x(knap.getX())
                  .y(knap.getY())
                  .rotation(0)
                  .setInterpolator(new OvershootInterpolator());
        }
      });
    }
  }


  @Override
  public boolean onTouch(View v, MotionEvent e) {
    System.out.println(e);

    if (e.getAction() == MotionEvent.ACTION_DOWN) {
      ikon.animate()
              .x(e.getX()-ikon.getWidth()/2)
              .y(e.getY()-ikon.getHeight()/2)
              .scaleX(0.5f)
              .scaleY(0.2f)
              .setDuration(150)
              .setInterpolator(new AccelerateDecelerateInterpolator());
    }
    if (e.getAction() == MotionEvent.ACTION_MOVE) {
      ikon.setX(e.getX()-ikon.getWidth()/2);
      ikon.setY(e.getY()-ikon.getHeight()/2);
    }
    if (e.getAction() == MotionEvent.ACTION_UP) {
      ikon.animate()
              .scaleX(1f)
              .scaleY(1f)
              .setInterpolator(new OvershootInterpolator());
    }
    return true;
  }
}
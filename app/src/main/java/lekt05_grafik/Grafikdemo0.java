package lekt05_grafik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;


public class Grafikdemo0 extends AppCompatActivity {

  static class GrafikView extends View {

    // programmatisk konstruktør
    public GrafikView(Context a) {
      super(a);
    }

    // deklarativ konstruktør
    public GrafikView(Context a, AttributeSet at) {
      super(a, at);
    }

    @Override
    protected void onDraw(Canvas c) {
      Paint tekstStregtype = new Paint();
      tekstStregtype.setColor(Color.GREEN);
      tekstStregtype.setTextSize(24);
      tekstStregtype.setAntiAlias(true);
      c.drawText("Hej verden", 0, 20, tekstStregtype);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    GrafikView grafikView = new GrafikView(this);
    setContentView(grafikView);
  }
}

package lekt05_grafik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

/**
 * @author Jacob Nordfalk
 */
public class Grafikdemo3 extends AppCompatActivity implements OnClickListener {

  String teksten = "GrafikView";
  Button okKnap;
  EditText editText;
  View minGrafik;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Opret grafik-view
    minGrafik = new View(this) {

      @Override
      protected void onDraw(Canvas c) {
        Paint tekstStregtype = new Paint();
        tekstStregtype.setColor(Color.GREEN);
        tekstStregtype.setTextSize(24);
        c.rotate(15, 0, 0); // getWidth()/2, getHeight()/2// rotér 23 grader om midten
        c.drawText(teksten, 0, 20, tekstStregtype);
      }
    };

    TableLayout tableLayout = new TableLayout(this);

    tableLayout.addView(minGrafik);
    minGrafik.getLayoutParams().height = 60;

    editText = new EditText(this);
    editText.setText("Skriv noget tekst her");
    editText.setSingleLine(true);

    tableLayout.addView(editText);

    okKnap = new ButtonPaaHovedet(this);
    okKnap.setText("OK");
    tableLayout.addView(okKnap);


    ButtonPaaHovedet annullerKnap = new ButtonPaaHovedet(this);
    annullerKnap.setText("Annuller!");
    tableLayout.addView(annullerKnap);


    setContentView(tableLayout);

    okKnap.setOnClickListener(this);
    annullerKnap.setOnClickListener(this);
  }

  public void onClick(View klikPåHvad) {
    System.out.println("Der blev klikket på " + klikPåHvad);
    teksten = editText.getText().toString();
    minGrafik.invalidate();
  }

  /**
   * @author j
   */
  public static class ButtonPaaHovedet extends AppCompatButton {

    public ButtonPaaHovedet(Context c) {
      super(c);
    }

    Paint p = new Paint();

    @Override
    protected void onDraw(Canvas c) {
      c.save();
      c.rotate(160, getWidth() / 2, getHeight() / 2);
      c.scale(1f, 1.2f);
      super.onDraw(c);
      c.restore();
      c.drawText("på hovedet", 5, getHeight() / 2, p);
    }
  }
}

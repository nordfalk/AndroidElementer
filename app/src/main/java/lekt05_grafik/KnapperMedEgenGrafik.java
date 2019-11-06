package lekt05_grafik;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class KnapperMedEgenGrafik extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    tl.setPadding(10,5,0,0);

    tl.addView(tv("Dette eksempel viser forskellige muligheder for knapper med egen grafik.\n"));
    Button button1, button2;

    tl.addView(tv("Alle views kan man sætte til at vise en anden baggrund:"));
    tl.addView(tr(tv("Her er en normal knap  "), tv("Her en knap med egen baggrund")));
    button1 = new Button(this);
    button2 = new Button(this);
    button2.setBackgroundResource(R.drawable.bil);
    button1.setText("hejsa");
    button2.setText("hejsa");
    tl.addView(tr(button1, button2));
    tl.addView(tv("MEN brugeren kan ikke se når de trykkes ned (feedback mangler - knappen skifter ikke farve)!\n"));

    tl.addView(tv("\nDu kan i stedet knytte grafikken til venstre/højre/top eller bunden af en knap:"));
    tl.addView(tr(tv("Bil i top"), tv("Bil til venstre (uden tekst)")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.bil,0,0);
    button2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bil,0,0,0);
    button1.setText("hejsa");
    button2.setText("");

    tl.addView(tv("\n\nHvis du selv vil designe knappen er den rigtige løsning at lave en xml baggrund drawable med en 'ripple' effekt fra API v21 " +
            "(og tidligere at lave knapgrafik til normal og nedtrykket og så kombinere disse i en 'selector' drawable)\n"));
    tl.addView(tr(tv("Knap med selector"), tv("... disablet")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setBackgroundResource(R.drawable.knapbg_bil);
    button2.setBackgroundResource(R.drawable.knapbg_bil);
    button2.setEnabled(false);
    button1.setText("hejsa");
    button2.setText("hejsa");
    //  alle tilstande: normal, fokus, nedtrykket (og evt disablet) og kombinationerne,

    tl.addView(tv("\n\nFor ensfarvede knapper kan du bruge 'shape'.\n"));
    tl.addView(tr(tv("Blå knap"), tv("... disablet")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setBackgroundResource(R.drawable.knapbg_blaa);
    button2.setBackgroundResource(R.drawable.knapbg_blaa);
    button2.setEnabled(false);
    button1.setText("hejsa");
    button2.setText("hejsa");



    tl.addView(tv("\nSe nogle eksempler (fra Android) på 'selector' drawables herunder.\nBemærk at de skifter farve når de trykkes ned:"));
    tl.addView(tr(tv("btn_star"), tv("btn_radio")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setBackgroundResource(android.R.drawable.btn_star);
    button2.setBackgroundResource(android.R.drawable.btn_radio);
    button1.setText("hejsa");
    button2.setText("hejsa");

    tl.addView(tr(tv("btn_plus"), tv("btn_minus")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setBackgroundResource(android.R.drawable.btn_plus);
    button2.setBackgroundResource(android.R.drawable.btn_minus);
    button1.setText("hejsa");
    button2.setText("hejsa");

    tl.addView(tr(tv("btn_default"), tv("btn_dropdown")));
    tl.addView(tr(button1 = new Button(this), button2 = new Button(this)));
    button1.setBackgroundResource(android.R.drawable.btn_default);
    button2.setBackgroundResource(android.R.drawable.btn_dropdown);
    button1.setText("hejsa");
    button2.setText("hejsa");

    tl.addView(tv("Hvis man har mange grafikker som man ønsker giver feedback ved at skifte farve ved tryk " +
            "kan det være omstændeligt at lave alle de selectors der skal til.\n\n" +
            "Nedenstående eksempler viser billeder der fungerer som knapper.\n"
            + "Men hvis man vil slippe for knap-rammen så kan man ikke se at knapppen bliver trykket ned.\n"
            + "Det kan løses ved at farve billedet når det berøres ('trykkes ned').\n"));

    OnTouchListener farvKnapNårDenErTrykketNed = new OnTouchListener() {
      public boolean onTouch(View view, MotionEvent me) {
        Log.d("onTouch()", me.toString());
        ImageView ib = (ImageView) view;
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
          //log("farve "+me);
          ib.setColorFilter(0xFFA0A0A0, PorterDuff.Mode.MULTIPLY);
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
          //log("ikke farve "+me);
          ib.setColorFilter(null);
        }
        return false;
      }
    };
    OnFocusChangeListener farvKnapNårFokus = new OnFocusChangeListener() {
      public void onFocusChange(View v, boolean hasFocus) {
        Log.d("onFocusChange()", "hasFocus=" + hasFocus);
        ImageView ib = (ImageView) v;
        if (hasFocus) {
          ib.setColorFilter(0xFFC0C0C0, PorterDuff.Mode.MULTIPLY);
        } else {
          ib.setColorFilter(null);
        }
      }
    };

    tl.addView(tr(tv("ImageButton"), tv("Med farvefilter")));

    ImageButton imageButton1 = new ImageButton(this);
    imageButton1.setImageResource(R.drawable.bil);

    ImageButton imageButton2 = new ImageButton(this);
    imageButton2.setImageResource(R.drawable.bil);
    imageButton2.setOnTouchListener(farvKnapNårDenErTrykketNed);
    imageButton2.setOnFocusChangeListener(farvKnapNårFokus);

    tl.addView(tr(imageButton1, imageButton2));


    tl.addView(tr(tv("IB uden baggrund"), tv("Med farvefilter")));

    imageButton1 = new ImageButton(this);
    imageButton1.setImageResource(R.drawable.bil);
    imageButton1.setBackgroundDrawable(null);

    imageButton2 = new ImageButton(this);
    imageButton2.setImageResource(R.drawable.bil);
    imageButton2.setBackgroundDrawable(null);
    imageButton2.setOnTouchListener(farvKnapNårDenErTrykketNed);

    tl.addView(tr(imageButton1, imageButton2));


    tl.addView(tr(tv("ImageView"), tv("Med farvefilter")));

    ImageView knap5 = new ImageView(this);
    knap5.setImageResource(R.drawable.bil);

    ImageView knap6 = new ImageView(this);
    knap6.setImageResource(R.drawable.bil);
    knap6.setOnTouchListener(farvKnapNårDenErTrykketNed);

    tl.addView(tr(knap5, knap6));


    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }


  /**
   * Laver et TextView med en tekst
   */
  private TextView tv(String s) {
    TextView tv = new TextView(this);
    tv.setText(s);
    return tv;
  }

  /**
   * Laver en TableRow
   */
  private TableRow tr(View knap1, View knap2) {
    TableRow tr = new TableRow(this);
    // Da TableRow altid bruger MATCH_PARENT i bredden er vi nødt til at pakke knapperne ind i et linearlayout
    LinearLayout ll1 = new LinearLayout(this);
    ll1.addView(knap1);
    tr.addView(ll1);
    LinearLayout ll2 = new LinearLayout(this);
    ll2.addView(knap2);
    tr.addView(ll2);
    return tr;
  }
}

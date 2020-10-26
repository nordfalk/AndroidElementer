package lekt06_asynkron;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

/**
 * Demonstrerer hvordan tråde kan benyttes - og ikke benyttes - direkte med Javas Thread-klasse
 */
public class Asynk2Thread extends AppCompatActivity implements OnClickListener {
  Button knap1, knap2, knap3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    EditText editText = new EditText(this);
    editText.setText("Demonstrerer hvordan tråde kan benyttes - og ikke benyttes - direkte med Javas Thread-klasse");
    editText.append("\nPrøv at redigere her efter du har trykket på knapperne.");
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    tl.addView(editText);

    knap1 = new Button(this);
    knap1.setText("Synkront");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Asynkront hvor baggrundstråden forsøger at opdatere brugergrænsefladen (crasher)");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Asynkront hvor baggrundstråden sender en Runnable tilbage til udførelse på hovedtråden");
    tl.addView(knap3);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
  }

  public void onClick(View v) {
    if (v == knap1) {
      knap1.setText("arbejder");  // <1>
      SystemClock.sleep(10000);
      knap1.setText("arbejder mere...");
      SystemClock.sleep(10000);
      knap1.setText("og endnu mere... men det vises ikke...");
      SystemClock.sleep(10000);
      knap1.setText("færdig!");

    } else if (v == knap2) {
      knap2.setText("arbejder\nmen det crasher da kun hovedtråden må opdatere brugergrænsefladen");  // <2>
      Runnable r = new Runnable() {
        public void run() {
          SystemClock.sleep(10000);
          knap2.setText("færdig!"); // Fejl - kun hovedtråden må opdatere brugergrænsefladen!
        }
      };
      Thread t = new Thread(r);
      t.start();
    } else if (v == knap3) {

      knap3.setText("arbejder");  // <3>
      System.out.println("arbejder");
      Runnable r = new Runnable() {
        public void run() {
          SystemClock.sleep(10000);
          System.out.println("færdig!");
          Runnable r2 = () -> knap3.setText("færdig!");
          runOnUiThread(r2); // beder hovedtråden om at køre r2
          // Her kunne uiThread.post(r2); også anvendes, se nedenfor
        }
      };
      new Thread(r).start();
    }
  }
  //Handler uiThread = new Handler(Looper.getMainLooper()); // brug evt denne i stedet for runOnUiThread(r2);
}
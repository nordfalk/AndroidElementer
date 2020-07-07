package lekt06_asynkron;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;

/**
 * Viser, hvordan en Handler kan sende opgaver til hovedtråden
 * @author Jacob Nordfalk
 */
@SuppressWarnings("NonAsciiCharacters")
public class Asynk1Handler extends AppCompatActivity implements OnClickListener {

  Handler uiThread = new Handler();
  Runnable opgave;
  Button knap1, knap2, knapAnnuller;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    TableLayout tl = new TableLayout(this);
    EditText editText = new EditText(this);
    editText.setText("Viser, hvordan en Handler kan sende opgaver til hovedtråden");
    editText.append("\nPrøv at redigere her efter du har trykket på knapperne");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    knap1 = new Button(this);
    knap1.setText("Opgave sendes til senere udførelse");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Brugerfladen opdateres løbende fra GUI-tråden");
    tl.addView(knap2);

    knapAnnuller = new Button(this);
    knapAnnuller.setText("Annuller");

    tl.addView(knapAnnuller);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knapAnnuller.setOnClickListener(this);
  }

  public void onClick(View v) {

    if (v == knap1) {

      opgave = new Runnable() {                // syntax for lokal indre anonym klasse
        public void run() {
          knap1.setText("færdig!");
        }
      };
      opgave = () -> knap1.setText("færdig!"); // samme kode. bare med lambda-syntax

      uiThread.postDelayed(opgave, 10000); // udfør opgaven om 10 sekunder
      knap1.setText("arbejder...");            // dette bliver udført omgående, før ovenstående

    } else if (v == knap2) {

      knap2.setText("arbejder...");
      opgave = new Runnable() {
        int antalSekunderGået = 0;
        @Override
        public void run() {
          if (antalSekunderGået++ < 10) {
            knap2.setText(antalSekunderGået + "...");
            uiThread.postDelayed(this, 1000); // udfør denne Runnable igen om 1 sekund
          } else {
            knap2.setText("færdig!"); // Der er gået 10 sekunder
          }
        }
      };
      uiThread.postDelayed(opgave, 1000); // udfør om 1 sekund

    } else if (v == knapAnnuller) {

      uiThread.removeCallbacks(opgave);
      knap2.setText("annulleret");

    }
  }
}

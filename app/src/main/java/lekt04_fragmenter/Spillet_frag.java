package lekt04_fragmenter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import lekt02_aktiviteter.Galgelogik;

/**
 * @author Jacob Nordfalk
 */
public class Spillet_frag extends Fragment implements View.OnClickListener {

  Galgelogik logik = new Galgelogik();
  private TextView info;
  private Button spilKnap;
  private EditText et;
  private TextView korrektAnimation;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("Velkomst_frag", "fragmentet blev vist!");

    // Programmatisk layout
    TableLayout tl = new TableLayout(getActivity());

    info = new TextView(getActivity());
    info.setText("Velkommen til mit fantastiske spil." +
            "\nDu skal gætte dette ord: "+logik.getSynligtOrd() +
            "\nSkriv et bogstav herunder og tryk 'Spil'.\n");
    if (getArguments()!=null) {
      String velkomst = getArguments().getString("velkomst");
      if (velkomst!=null) info.append(velkomst);
    }
    tl.addView(info);

    et = new EditText(getActivity());
    et.setHint("Skriv et bogstav her.");
    tl.addView(et);

    korrektAnimation = new TextView(getActivity());
    korrektAnimation.setText("korrektAnimation");
    tl.addView(korrektAnimation);
    korrektAnimation.setTextColor(Color.GREEN);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      korrektAnimation.setElevation(100); // læg viewet forrest så det ikke dækkes af andre views
    }


    spilKnap = new Button(getActivity());
    spilKnap.setText("Spil");
    spilKnap.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
    tl.addView(spilKnap);

    spilKnap.setOnClickListener(this);

    return tl;
  }

  @Override
  public void onClick(View v) {
    String bogstav = et.getText().toString();
    if (bogstav.length() != 1) {
      et.setError("Skriv præcis ét bogstav");
      return;
    }
    logik.gætBogstav(bogstav);
    et.setText("");
    et.setError(null);

    if (logik.erSidsteBogstavKorrekt()) {
      korrektAnimation.setText(" Hurra, " +bogstav + " er rigtigt!");
      korrektAnimation.setTranslationY(0);
      korrektAnimation.setAlpha(1);
       korrektAnimation.animate().translationY(-400).alpha(0).setDuration(2000);
    }

    opdaterSkærm();
  }


  private void opdaterSkærm() {
    info.setText("Gæt ordet: " + logik.getSynligtOrd());
    info.append("\n\nDu har " + logik.getAntalForkerteBogstaver() + " forkerte:" + logik.getBrugteBogstaver());

    if (logik.erSpilletVundet()) {
      info.append("\nDu har vundet");
    }
    if (logik.erSpilletTabt()) {
      info.setText("Du har tabt, ordet var : " + logik.getOrdet());
    }
  }
}

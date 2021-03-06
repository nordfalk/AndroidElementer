package lekt04_fragmenter_navhost;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;
import dk.nordfalk.android.elementer.R;

public class Hovedmenu_frag extends Fragment implements View.OnClickListener {
  Button hjaelpKnap, indstillingerKnap, spilKnap;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.lekt01_tre_knapper, container, false);

    hjaelpKnap = rod.findViewById(R.id.knap1);
    hjaelpKnap.setText("Hjælp");

    indstillingerKnap = rod.findViewById(R.id.knap2);
    indstillingerKnap.setText("Indstillinger");

    spilKnap = rod.findViewById(R.id.knap3);
    spilKnap.setText("Spil");

    hjaelpKnap.setOnClickListener(this);
    indstillingerKnap.setOnClickListener(this);
    spilKnap.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {
    if (v == hjaelpKnap) {

      Navigation.findNavController(v).navigate(R.id.til_hjælp);

    } else if (v == indstillingerKnap) {

      Navigation.findNavController(v).navigate(R.id.til_indstillinger);

    } else if (v == spilKnap) {

      Bundle argumenter = new Bundle();
      argumenter.putString("velkomst", "\n\nHalløj!! Denne tekst kommer fra "+getClass());
      Navigation.findNavController(v).navigate(R.id.til_spillet, argumenter);

    }
  }
}

package lekt32_animerede_overgange;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

public class FragmentovergangSlut extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rod = inflater.inflate(R.layout.lekt32_overgange_slut, container, false);

    ViewCompat.setTransitionName(rod.findViewById(R.id.ikon), "ikon");
    ViewCompat.setTransitionName(rod.findViewById(R.id.enKnap), "knappen");


    String knapTeksten = "Hov, fik ikke en knap-tekst?";
    if (getArguments()!=null) knapTeksten = getArguments().getString("knap-teksten", knapTeksten);
    ((Button) rod.findViewById(R.id.enKnap)).setText(knapTeksten);

    return rod;
  }
}

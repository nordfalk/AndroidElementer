package lekt32_animerede_overgange;

import android.os.Bundle;
import androidx.transition.AutoTransition;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

public class FragmentovergangBegynd extends Fragment implements View.OnClickListener {
  private View rod, knap1, knap2, knap3, ikon;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rod = inflater.inflate(R.layout.lekt01_tre_knapper, container, false);
    ikon = rod.findViewById(R.id.ikon);
    knap1 = rod.findViewById(R.id.knap1);
    knap2 = rod.findViewById(R.id.knap2);
    knap3 = rod.findViewById(R.id.knap3);
    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    // Alle views der muligvis skal animeres skal have et navn i onCreateView()
    ViewCompat.setTransitionName(ikon, "ikon");
    ViewCompat.setTransitionName(knap1, "knap1");
    ViewCompat.setTransitionName(knap2, "knap2");
    ViewCompat.setTransitionName(knap3, "knap3");
    return rod;
  }

  public void onClick(View klikPåHvad) {

    FragmentovergangSlut slutfragment = new FragmentovergangSlut();

    // Langsomme overgange så vi kan se hvad der sker. Slet evt .setDuration(1000)
    slutfragment.setEnterTransition(new AutoTransition().setDuration(1000));
    setExitTransition(new AutoTransition().setDuration(1000));

    // De enkelte views (SharedElement) der skal animeres
    slutfragment.setSharedElementEnterTransition(new AutoTransition().setDuration(1000));
    slutfragment.setSharedElementReturnTransition(new AutoTransition().setDuration(1000));

    Bundle argumenter = new Bundle();
    argumenter.putCharSequence("knap-teksten", ((Button) klikPåHvad).getText());
    slutfragment.setArguments(argumenter);

    // Lav bindinger til mål-aktiviteten, så der kan laves glidende overgange
    // Navnene her skal passe med det TransitionName viewsne har i mål-aktiviteten
    getFragmentManager().beginTransaction()
            .addSharedElement(ikon, "ikon")
            .addSharedElement(klikPåHvad, "knappen")
            .replace(R.id.fragmentindhold, slutfragment)
            .addToBackStack(null)
            .commit();
  }
}

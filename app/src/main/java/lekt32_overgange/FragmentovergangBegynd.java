package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.transition.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

  public void onClick(View knappen) {

    FragmentovergangSlut slutfragment = new FragmentovergangSlut();

    // Langsom overgang så vi kan se hvad der sker. Slet evt .setDuration(1000)
    slutfragment.setEnterTransition(new Fade().setDuration(1000));
    setExitTransition(new Fade().setDuration(1000));

    // De enkelte views (haredElement) skal have animeret en række egenskaber:
    TransitionSet transitionSet = new TransitionSet().setDuration(1000)
            .addTransition(new ChangeBounds())
            .addTransition(new ChangeTransform())
            .addTransition(new ChangeImageTransform());

    slutfragment.setSharedElementEnterTransition(transitionSet);
    slutfragment.setSharedElementReturnTransition(transitionSet);

    Bundle argumenter = new Bundle();
    argumenter.putCharSequence("knap-teksten", ((Button) knappen).getText());
    slutfragment.setArguments(argumenter);

    // Lav bindinger til mål-aktiviteten, så der kan laves glidende overgange
    // Navnene her skal passe med det TransitionName viewsne har i mål-aktiviteten
    getFragmentManager().beginTransaction()
            .addSharedElement(ikon, "ikon")
            .addSharedElement(knappen, "knappen")
            .replace(R.id.fragmentindhold, slutfragment)
            .addToBackStack(null)
            .commit();
  }
}

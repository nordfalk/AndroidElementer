package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
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
    ikon = (ImageView) rod.findViewById(R.id.ikon);
    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap3 = (Button) rod.findViewById(R.id.knap3);
    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    return rod;
  }

  public void onClick(View trykketPåKnap) {

    // Lav bindinger til mål-aktiviteten, så der kan laves glidende overgange
    // Navnene skal passe med det TransitionName viewsne har i mål-aktiviteten

    FragmentovergangSlut f = new FragmentovergangSlut();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Transition overgang = TransitionInflater.from(getActivity()).inflateTransition(R.transition.lekt32_overgange_trans);
      f.setSharedElementEnterTransition(overgang);

      //this.setSharedElementReturnTransition(overgang);
      this.setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

      //f.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

      ikon.setTransitionName("ikon");
      trykketPåKnap.setTransitionName("knappen");
    }

    getFragmentManager().beginTransaction()
            .replace(R.id.fragmentindhold, f)
            .addToBackStack("Overgange")
            .addSharedElement(ikon, "ikon")
            .addSharedElement(trykketPåKnap, "knappen")
            .setTransition(R.transition.lekt32_overgange_trans)
            .commit();
  }
}

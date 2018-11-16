package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.nordfalk.android.elementer.R;

public class FragmentovergangSlut extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.lekt32_overgange_slut, container, false);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      view.findViewById(R.id.ikon).setTransitionName("ikon");
      view.findViewById(R.id.enKnap).setTransitionName("knappen");
      //Transition overgang = TransitionInflater.from(getActivity()).inflateTransition(R.transition.lekt32_overgange_trans);
      //setSharedElementReturnTransition(overgang);
    }

    return view;
  }
}

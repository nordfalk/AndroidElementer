package lekt04_fragnav;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import dk.nordfalk.android.elementer.R;
import lekt04_fragmenter.Hovedmenu_frag;

public class Velkomst_frag extends Fragment implements Runnable {

  Handler handler = new Handler();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("Velkomst_frag", "fragmentet blev vist!");

    ImageView iv = new ImageView(getActivity());
    iv.setImageResource(R.drawable.logo);
    iv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.egen_anim));

    // Hvis savedInstanceState ikke er null er fragmentet ved at blive genstartet
    if (savedInstanceState == null) {
      handler.postDelayed(this, 3000); // <1> Kør run() om 3 sekunder
    }

    return iv;
  }

  public void run() {
    if (getView()==null) return;
    Navigation.findNavController(getView()).popBackStack();
    Navigation.findNavController(getView()).navigate(R.id.til_hovedmenu);
  }
}
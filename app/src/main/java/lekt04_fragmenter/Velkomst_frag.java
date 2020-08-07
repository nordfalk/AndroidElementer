package lekt04_fragmenter;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

public class Velkomst_frag extends Fragment implements Runnable {

  Handler handler = new Handler(Looper.getMainLooper());

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("Velkomst_frag", "fragmentet blev vist!");

    ImageView iv = new ImageView(getActivity());
    iv.setImageResource(R.drawable.logo);
    iv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.egen_anim));
    // En lettere måde at lave små animationer på:
    //iv.animate().rotation(360*2).setDuration(2000);

    // Hvis savedInstanceState ikke er null er fragmentet ved at blive genstartet
    if (savedInstanceState == null) {
      handler.postDelayed(this, 3000); // <1> Kør run() om 3 sekunder
    }

    return iv;
  }

  public void run() {
    if (getActivity()==null) return; // Hvis brugeren er hoppet ud af aktiviteten
    Fragment fragment = new Hovedmenu_frag();
    getFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fragmentindhold, fragment)  // tom container i layout
            .commit();
  }
}
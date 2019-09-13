package lekt32_animerede_overgange;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;

public class VisFragmentovergange extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt04_fragmenter_manuel_nav);

    // Vi sætter blå baggrund for at undgå at baggrunden kortvarigt bliver rødlig -
    // fordi vi fader imellem to blå fragmenter
    findViewById(R.id.fragmentindhold).setBackgroundColor(0xff333399);

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.fragmentindhold, new FragmentovergangBegynd())
            .commit();
  }
}

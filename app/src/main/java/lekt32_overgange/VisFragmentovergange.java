package lekt32_overgange;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;

public class VisFragmentovergange extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt04_fragmenter_manuel_nav);

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.fragmentindhold, new FragmentovergangBegynd())
            .commit();
  }
}

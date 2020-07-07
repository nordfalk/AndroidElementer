package lekt01_views;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;
import dk.nordfalk.android.elementer.databinding.Lekt01TreKnapperBinding;

/**
 * Deklarativt layout med automatisk viewbinding anbefales til større projekter.
 * Se https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
 * @author Jacob Nordfalk
 */
public class BenytKnapperDeklViewbinding extends AppCompatActivity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Lekt01TreKnapperBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = Lekt01TreKnapperBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.knap1.setOnClickListener(this);
    binding.knap2.setOnClickListener(this);
    binding.knap3.setOnClickListener(this);
  }

  public void onClick(View klikPåHvad) {
    System.out.println("Der blev trykket på en knap");

    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    if (klikPåHvad == binding.knap1) {

      binding.knap1.setText("Du trykkede på mig. Tak! \n" + etTal);

    } else if (klikPåHvad == binding.knap2) {

      binding.knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);

    } else if (klikPåHvad == binding.knap3) {

      binding.knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);
      // Erstat logoet med en bil
      ImageView ikon = findViewById(R.id.ikon);
      ikon.setImageResource(R.drawable.bil);

    }

  }
}

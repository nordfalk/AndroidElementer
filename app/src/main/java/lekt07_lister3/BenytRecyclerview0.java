package lekt07_lister3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import dk.nordfalk.android.elementer.R;

public class BenytRecyclerview0 extends AppCompatActivity {

  String[] landeArray = {"Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
          "Tyskland", "Østrig", "Belgien", "Holland", "Italien", "Grækenland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};
  // Vi laver en arrayliste så vi kan fjerne/indsætte elementer
  ArrayList<String> lande = new ArrayList<>(Arrays.asList(landeArray));

  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    setContentView(recyclerView);
  }

  RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
      return new RecyclerView.ViewHolder(itemView) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
      TextView overskrift = vh.itemView.findViewById(R.id.listeelem_overskrift);
      TextView beskrivelse = vh.itemView.findViewById(R.id.listeelem_beskrivelse);
      ImageView billede = vh.itemView.findViewById(R.id.listeelem_billede);
      overskrift.setText(lande.get(position));

      beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
      if (position % 3 == 2) {
        billede.setImageResource(android.R.drawable.sym_action_call);
      } else {
        billede.setImageResource(android.R.drawable.sym_action_email);
      }
    }
  };
}
